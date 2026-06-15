package com.warpedcitadel.contentmanagementservice.trending.dto;

import java.util.HashMap;

public record GameGenresDto(
        HashMap <Integer, String> genres
) {}
