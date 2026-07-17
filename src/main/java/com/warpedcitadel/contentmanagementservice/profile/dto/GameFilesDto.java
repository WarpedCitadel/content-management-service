package com.warpedcitadel.contentmanagementservice.profile.dto;

public record GameFilesDto(
        String filename,
        int platformOS,
        String fileURL
) {}
