package com.warpedcitadel.contentmanagementservice.trending.dto;

import com.warpedcitadel.contentmanagementservice.trending.model.TrendingGamesModel;

public record TrendingGamesDto(
        SlicedResponse<TrendingGamesModel> listGames
) {}
