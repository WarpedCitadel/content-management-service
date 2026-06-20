package com.warpedcitadel.contentmanagementservice.profile.dto;

import java.util.List;

public record GameProfileDetailsDto(
        String gameProfileUUID,
        String title,
        String description,
        String genreType,
        String gameType,
        List<String> platformOS,
        String createdDtm,
        String coverImg,
        List<String> gameImg,
        String displayName,
        String userUUID
) {}
