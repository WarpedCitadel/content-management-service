package com.warpedcitadel.contentmanagementservice.profile;

import com.warpedcitadel.contentmanagementservice.profile.dto.GameProfileDto;
import com.warpedcitadel.contentmanagementservice.profile.model.GameProfileModel;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }


    protected String createGameProfile(GameProfileDto gameProfileDto) {

        GameProfileModel gameProfileModel = new GameProfileModel();

        // Todo: validate the data before it is inserted into the database
        gameProfileModel.setUserUUID(gameProfileDto.userUUID());
        gameProfileModel.setTitle(gameProfileDto.title());
        gameProfileModel.setShortDesc(gameProfileDto.shortDesc());
        gameProfileModel.setDescription(gameProfileDto.description());
        gameProfileModel.setGameGenre(gameProfileDto.gameGenre());
        gameProfileModel.setGameType(gameProfileDto.gameType());
        gameProfileModel.setPlatformOS(gameProfileDto.platformOS());

        String gameProfileUUID = profileRepository.createGameProfile(gameProfileModel);
        if (gameProfileUUID == null) {

            throw new RuntimeException("Failed to retrieve game profile data");
        }

        return gameProfileUUID;
    }


    protected void updateGameProfile(GameProfileDto gameProfileDto) {

        GameProfileModel gameProfileModel = new GameProfileModel();

        // Todo: validate the data before it is inserted into the database
        gameProfileModel.setUserUUID(gameProfileDto.userUUID());
        gameProfileModel.setGameProfileUUID(gameProfileDto.gameProfileUUID());
        gameProfileModel.setTitle(gameProfileDto.title());
        gameProfileModel.setShortDesc(gameProfileDto.shortDesc());
        gameProfileModel.setDescription(gameProfileDto.description());
        gameProfileModel.setGameGenre(gameProfileDto.gameGenre());
        gameProfileModel.setGameType(gameProfileDto.gameType());
        gameProfileModel.setPlatformOS(gameProfileDto.platformOS());

        profileRepository.updateGameProfile(gameProfileModel);

    }
}
