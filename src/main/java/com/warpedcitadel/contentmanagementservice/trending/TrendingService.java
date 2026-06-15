package com.warpedcitadel.contentmanagementservice.trending;

import com.warpedcitadel.contentmanagementservice.trending.dto.SearchAttributesDto;
import com.warpedcitadel.contentmanagementservice.trending.dto.SlicedResponse;
import com.warpedcitadel.contentmanagementservice.trending.dto.TrendingDto;
import com.warpedcitadel.contentmanagementservice.trending.model.SearchAttributesModel;
import com.warpedcitadel.contentmanagementservice.trending.model.TrendingModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrendingService {

    private final TrendingRepository trendingRepository;

    public TrendingService(TrendingRepository trendingRepository) {
        this.trendingRepository = trendingRepository;
    }


    protected TrendingDto getGameProfiles(Pageable pageable, SearchAttributesDto attributesDto) {

        int offSet = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();

        if (limit >= 51) {
            throw new IllegalArgumentException("Content requested too large");
        }

        SearchAttributesModel attributesModel =
                new SearchAttributesModel(
                        attributesDto.title(),
                        attributesDto.genre()
                );

        List<Object> attributesList = new ArrayList<>();

        if (attributesModel.getTitle() != null &&
            !attributesModel.getTitle().isEmpty()) {
            attributesList.add(attributesModel.getTitle().concat("%"));
        } else {
            attributesModel.setTitle("%");
            attributesList.add(attributesModel.getTitle());
        }

        attributesList.add(attributesModel.getGenre());

        attributesList.add(limit + 1);
        attributesList.add(offSet);

        Slice<TrendingModel> trendingGameList = trendingRepository.getTrendingGames(pageable, attributesList);
        SlicedResponse<TrendingModel> filterData = new SlicedResponse<>(trendingGameList);
        return new TrendingDto(filterData);
    }
}
