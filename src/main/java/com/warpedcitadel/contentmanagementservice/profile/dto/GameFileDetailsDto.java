package com.warpedcitadel.contentmanagementservice.profile.dto;

import java.util.HashMap;

public record GameFileDetailsDto(
        String BrowserGameURL,
        HashMap<String, String> files
) {}
