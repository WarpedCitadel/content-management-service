package com.warpedcitadel.contentmanagementservice.profile.dto;

import java.util.List;

public record GameFileDetailsDto(
        String browserGameURL,
        List<GameFilesDto> files
) {}
