package com.warpedcitadel.contentmanagementservice.trending.dto;

import com.warpedcitadel.contentmanagementservice.trending.model.TrendingModel;

public record TrendingDto(
        SlicedResponse<TrendingModel> listGames
) {}
