package com.warpedcitadel.contentmanagementservice.payload;

import java.time.Instant;
import java.util.Map;

public record GenericApiErrorResponse(
        String title,
        int status,
        Map<String, String> error,
        String instance,
        Instant timestamp
) {}
