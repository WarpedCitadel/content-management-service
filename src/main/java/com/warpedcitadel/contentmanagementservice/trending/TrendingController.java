package com.warpedcitadel.contentmanagementservice.trending;


import com.warpedcitadel.contentmanagementservice.payload.ApiResponse;
import com.warpedcitadel.contentmanagementservice.trending.dto.GameGenresDto;
import com.warpedcitadel.contentmanagementservice.trending.dto.SearchAttributesDto;
import com.warpedcitadel.contentmanagementservice.trending.dto.TrendingGamesDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.Instant;

@RestController
@RequestMapping(path = "/main", version="1.0")
public class TrendingController {

    private final TrendingService trendingService;

    public TrendingController(TrendingService trendingService) {
        this.trendingService = trendingService;
    }

    @GetMapping("/GetTrendingGames")
    public ResponseEntity<ApiResponse<TrendingGamesDto>> getTrendingGames(SearchAttributesDto attributes,
                                                                Pageable pageable, WebRequest request) {

        TrendingGamesDto data = trendingService.getGameProfiles(pageable, attributes);
        ApiResponse<TrendingGamesDto> response = new ApiResponse<>("Trending games",
                HttpStatus.OK.value(),
                data,
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/GetGameCategories")
    public ResponseEntity<ApiResponse<GameGenresDto>> getGameGenres(WebRequest request) {

        GameGenresDto categories = trendingService.getGameCategories();
        ApiResponse<GameGenresDto> response = new ApiResponse<>("Game Genres",
                HttpStatus.OK.value(),
                categories,
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }
}
