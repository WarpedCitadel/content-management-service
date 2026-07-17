package com.warpedcitadel.contentmanagementservice.profile.dto;

import java.util.List;

public record GameProfileImageDto(
        String coverImages,
        List<String> gameImages
) {}
