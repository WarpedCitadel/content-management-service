package com.warpedcitadel.contentmanagementservice.profile.util;

import com.warpedcitadel.contentmanagementservice.profile.dto.CloudFrontCookie;
import com.warpedcitadel.contentmanagementservice.profile.dto.GameProfileDetailsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.cloudfront.model.CannedSignerRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Service
public class CloudFrontService {

    @Value("${cloudfront.private-key}")
    private String privateKeyPath;
    @Value("${cloud.aws.keypair}")
    private String keyPair;

    private final String cloudFrontDomain = "https://www.warpedcitadel.com/";
    private static final Logger log = LoggerFactory.getLogger(CloudFrontService.class);


    public CloudFrontCookie generateSignedCookie(GameProfileDetailsDto gameProfileDetailsDto) {
        try {
            Instant expiration =
                    Instant.now().plus(Duration.ofHours(2));
            String resource =
                    cloudFrontDomain +
                            "games/" +
                            gameProfileDetailsDto.gameProfileUUID() + "/files/*"; // needs to be narrowed down
            String policy =
                    createPolicy(resource, expiration);
            String signature =
                    sign(policy);
            return new CloudFrontCookie(
                    cloudFrontBase64(policy.getBytes(StandardCharsets.UTF_8)),
                    signature,
                    keyPair
            );
        } catch (RuntimeException exception) {
            log.error("Failed to create CloudFront cookies for storage Object ({}) Reason: ({})",
                    gameProfileDetailsDto.gameProfileUUID(), exception.toString());
            throw new RuntimeException("Failed to generate cookies");
        }
    }


    public String generateSignedUrl(String objectKey) {
        try {
            Path key = Paths.get(privateKeyPath);
            String encodedKey = UriUtils.encodePath(objectKey, StandardCharsets.UTF_8);
            CannedSignerRequest request =
                    CannedSignerRequest.builder()
                            .resourceUrl(cloudFrontDomain + encodedKey)
                            .privateKey(key)
                            .keyPairId(keyPair)
                            .expirationDate(
                                    Instant.now().plus(Duration.ofHours(2)))
                            .build();
            return CloudFrontUtilities.create()
                    .getSignedUrlWithCannedPolicy(request)
                    .url();
        } catch (Exception exception) {
            log.error("Failed to create presigned URL for storage Object ({}) Reason: ({})",
                    objectKey, exception.toString());
        }
        return null;
    }


    private String createPolicy(
            String resource,
            Instant expiration) {
        return """
        {
          "Statement":[
            {
              "Resource":"%s",
              "Condition":{
                "DateLessThan":{
                  "AWS:EpochTime":%d
                }
              }
            }
          ]
        }
        """.formatted(
                resource,
                expiration.getEpochSecond());
    }


    private String sign(String policy) {
        try {
            Signature signer =
                    Signature.getInstance("SHA1withRSA");
            signer.initSign(loadPrivateKey());
            signer.update(
                    policy.getBytes(StandardCharsets.UTF_8));
            return cloudFrontBase64(
                    signer.sign());
        } catch (Exception exception) {
            log.error("Failed to create CloudFront signature for storage Object Reason: ({})",
                    exception.toString());
            throw new RuntimeException("Failed to sign cookies");
        }
    }


    private PrivateKey loadPrivateKey() {
        try {
            String key =
                    Files.readString(
                            Paths.get(privateKeyPath));
            key = key
                    .replace(
                            "-----BEGIN PRIVATE KEY-----",
                            "")
                    .replace(
                            "-----END PRIVATE KEY-----",
                            "")
                    .replaceAll("\\s", "");
            byte[] decoded =
                    Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec spec =
                    new PKCS8EncodedKeySpec(decoded);
            return KeyFactory
                    .getInstance("RSA")
                    .generatePrivate(spec);
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException exception) {
            log.error("Failed to load CloudFront keys for CloudFront Reason: ({})",
                    exception.toString());
            throw new RuntimeException("Failed to create CloudFront cookies for storage Object");
        }
    }


    private String cloudFrontBase64(byte[] bytes) {
        return Base64.getEncoder()
                .encodeToString(bytes)
                .replace('+', '-')
                .replace('=', '_')
                .replace('/', '~');
    }
}