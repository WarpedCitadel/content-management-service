package com.warpedcitadel.contentmanagementservice.profile.dto;


public record CloudFrontCookie(
        String policy,
        String signature,
        String keyPairId
) {}
