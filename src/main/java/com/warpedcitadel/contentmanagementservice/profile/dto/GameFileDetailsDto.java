package com.warpedcitadel.contentmanagementservice.profile.dto;

import java.util.HashMap;

public record GameFileDetailsDto(
        String browserGameURL,
        HashMap<String, String> files
) {}
