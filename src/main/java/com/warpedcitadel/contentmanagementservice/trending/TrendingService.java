package com.warpedcitadel.contentmanagementservice.trending;

import com.warpedcitadel.contentmanagementservice.trending.dto.GameGenresDto;
import com.warpedcitadel.contentmanagementservice.trending.dto.SearchAttributesDto;
import com.warpedcitadel.contentmanagementservice.trending.dto.SlicedResponse;
import com.warpedcitadel.contentmanagementservice.trending.dto.TrendingGamesDto;
import com.warpedcitadel.contentmanagementservice.trending.model.SearchAttributesModel;
import com.warpedcitadel.contentmanagementservice.trending.model.TrendingGamesModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TrendingService {

    private final TrendingRepository trendingRepository;

    public TrendingService(TrendingRepository trendingRepository) {
        this.trendingRepository = trendingRepository;
    }


    protected TrendingGamesDto getGameProfiles(Pageable pageable, SearchAttributesDto attributesDto) {

        int offSet = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();

        if (limit >= 51) {
            throw new IllegalArgumentException("Content requested too large");
        }

        SearchAttributesModel attributesModel = new SearchAttributesModel();

        List<Object> attributesList = new ArrayList<>();

        if (attributesDto.title() != null &&
            !attributesDto.title().isEmpty()) {
            attributesModel.setTitle(attributesDto.title().concat("%"));
            attributesList.add(attributesModel.getTitle());
        } else {
            attributesModel.setTitle("%");
            attributesList.add(attributesModel.getTitle());
        }

        attributesModel.setGenre(attributesDto.genre());
        attributesList.add(attributesModel.getGenre());

        attributesModel.setPlatformOS(attributesDto.platformOS());
        attributesList.add(attributesModel.getPlatformOS());

        attributesList.add(limit + 1);
        attributesList.add(offSet);

        Slice<TrendingGamesModel> trendingGameList = trendingRepository.getTrendingGames(pageable, attributesList);
        SlicedResponse<TrendingGamesModel> filterData = new SlicedResponse<>(trendingGameList);
        return new TrendingGamesDto(filterData);
    }


    protected GameGenresDto getGameCategories() {

        HashMap<Integer, String> genres = trendingRepository.getGameGenres();

        return new GameGenresDto(
                genres
        );
    }
}
