package com.warpedcitadel.contentmanagementservice.trending;

import com.warpedcitadel.contentmanagementservice.profile.util.CloudFrontCookieMaker;
import com.warpedcitadel.contentmanagementservice.trending.dto.GameGenresDto;
import com.warpedcitadel.contentmanagementservice.trending.dto.SearchAttributesDto;
import com.warpedcitadel.contentmanagementservice.trending.dto.SlicedResponse;
import com.warpedcitadel.contentmanagementservice.trending.dto.TrendingGamesDto;
import com.warpedcitadel.contentmanagementservice.trending.model.SearchAttributesModel;
import com.warpedcitadel.contentmanagementservice.trending.model.TrendingGamesModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TrendingService {

    private final TrendingRepository trendingRepository;
    private final CloudFrontCookieMaker cloudFrontCookieMaker; // Make this class universal

    public TrendingService(TrendingRepository trendingRepository, CloudFrontCookieMaker cloudFrontCookieMaker) {
        this.trendingRepository = trendingRepository;
        this.cloudFrontCookieMaker = cloudFrontCookieMaker;
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

        attributesModel.setMostRecent(attributesDto.mostRecent());
        attributesList.add(attributesModel.getMostRecent());

        attributesModel.setGameType(attributesDto.gameType());
        attributesList.add(attributesModel.getGameType());

        attributesList.add(limit + 1);
        attributesList.add(offSet);

        List<TrendingGamesModel> trendingGameList = trendingRepository.getTrendingGames(pageable, attributesList);


        for (int i = 0; trendingGameList.size() > i; i++) {

            String gameProfileUUID = trendingGameList.getFirst().getGameProfileUUID();
            String coverImg = trendingGameList.get(i).getCoverImg();

            String result = generateGameImageUrl(gameProfileUUID, coverImg);

            trendingGameList.get(i).setCoverImgUUID(result);
        }

        boolean hasNext = trendingGameList.size() > pageable.getPageSize();

        if (hasNext) {
            trendingGameList.remove(trendingGameList.size() - 1);
        }

        SliceImpl paginatedList = new SliceImpl<>(trendingGameList, pageable, hasNext);

        SlicedResponse<TrendingGamesModel> filterData = new SlicedResponse<>(paginatedList);
        return new TrendingGamesDto(filterData);
    }


    protected GameGenresDto getGameCategories() {

        HashMap<Integer, String> genres = trendingRepository.getGameGenres();

        return new GameGenresDto(
                genres
        );
    }


    private String generateGameImageUrl(String gameProfileUUID, String coverImg) {

        String imageUrl = "https://www.warpedcitadel.com/images/games/" + gameProfileUUID +
                    "/gameImages/" + coverImg;

        String coverImgURl = cloudFrontCookieMaker.generateSignedUrl(imageUrl);

        return coverImgURl;
    }
}
