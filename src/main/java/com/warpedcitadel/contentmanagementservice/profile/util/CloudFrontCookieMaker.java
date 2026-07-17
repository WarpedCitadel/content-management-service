package com.warpedcitadel.contentmanagementservice.profile.util;

import com.warpedcitadel.contentmanagementservice.profile.dto.CloudFrontCookie;
import com.warpedcitadel.contentmanagementservice.profile.dto.GameProfileDetailsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.cloudfront.model.CannedSignerRequest;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Service
public class CloudFrontCookieMaker {

    @Value("${cloudfront.private-key}")
    private String privateKeyPath;

    @Value("${cloud.aws.keypair}")
    private String keyPair;

    private String cloudFrontDomain = "https://www.warpedcitadel.com/";

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

        } catch (Exception exception) {

            throw new RuntimeException("Failed generating CloudFront cookie", exception);
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

            System.out.println("Failed to generate Presigned URL");
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

            throw new RuntimeException("Failed to create signature for cookies", exception);
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
        } catch (Exception exception) {

            throw new RuntimeException("Failed to load keys", exception);
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