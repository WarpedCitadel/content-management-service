package com.warpedcitadel.contentmanagementservice.profile;

import com.warpedcitadel.contentmanagementservice.profile.dto.*;
import com.warpedcitadel.contentmanagementservice.profile.model.GameProfileDetailsModel;
import com.warpedcitadel.contentmanagementservice.profile.model.GameProfileModel;
import com.warpedcitadel.contentmanagementservice.profile.util.CloudFrontCookieMaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ProfileService {

    @Value("${cloud.aws.region}")
    private String region;

    @Value("${aws.game-bucket.name}")
    private String gameBucketName;

    @Value("${aws.image-bucket.name}")
    private String imageBucketName;


    private final ProfileRepository profileRepository;
    private final S3Client s3Client;
    private final CloudFrontCookieMaker cloudFrontCookieMaker;

    public ProfileService(ProfileRepository profileRepository, S3Client s3Client, CloudFrontCookieMaker cloudFrontCookieMaker) {
        this.profileRepository = profileRepository;
        this.s3Client = s3Client;
        this.cloudFrontCookieMaker = cloudFrontCookieMaker;
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


    protected GameProfileDetailsDto getGameProfile(String gameProfileUUID) {

        GameProfileDetailsModel gameProfileModel = profileRepository.getGameProfile(gameProfileUUID);

        if (gameProfileModel == null) {
            throw new RuntimeException("Request game profile does not exist!: " + gameProfileUUID);
        }

        GameFileDetailsDto gameFiles = new GameFileDetailsDto(
                generateHtmlGameUrl(gameProfileModel),
                generateFileUrl(gameProfileModel)
        );

        GameProfileImageDto gameImages = new GameProfileImageDto(
                generateGameCoverUrl(gameProfileModel),
                generateGameImageUrl(gameProfileModel)
        );

        GameProfileDetailsDto gameProfile =
                new GameProfileDetailsDto(
                        gameProfileModel.getGameProfileUUID(),
                        gameProfileModel.getTitle(),
                        gameProfileModel.getDescription(),
                        gameProfileModel.getGenreType(),
                        gameProfileModel.getGameType(),
                        gameProfileModel.getPlatformOS(),
                        gameProfileModel.getCreatedDtm(),
                        gameImages,
                        gameFiles,
                        gameProfileModel.getDisplayName(),
                        gameProfileModel.getUserUUID()
                );

        return gameProfile;
    }


    protected void deleteGameProfile(DeleteGameProfileDto gameProfileDto) {

        GameProfileModel gameProfileModel = new GameProfileModel();

        // Todo: validate the data before it is inserted into the database
        gameProfileModel.setUserUUID(gameProfileDto.userUUID());
        gameProfileModel.setGameProfileUUID(gameProfileDto.gameProfileUUID());

        String imagePrefix = "images/games/" + gameProfileModel.getGameProfileUUID() + "/";
        String gamePrefix = "games/" + gameProfileModel.getGameProfileUUID() + "/";

        deleteS3Objects(imageBucketName, imagePrefix);
        deleteS3Objects(gameBucketName, gamePrefix);

        profileRepository.deleteGameProfile(gameProfileModel);
    }

    private void deleteS3Objects(String bucketName, String prefix) {

        String continuationToken = null;

        do {

            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .continuationToken(continuationToken)
                    .build();

            ListObjectsV2Response objectList = s3Client.listObjectsV2(listRequest);
            List<ObjectIdentifier> deletionList = new ArrayList<>();

            for (S3Object s3Object : objectList.contents()) {
                deletionList.add(ObjectIdentifier.builder().key(s3Object.key()).build());
            }

            if (!deletionList.isEmpty()) {
                DeleteObjectsRequest deleteS3Objects = DeleteObjectsRequest.builder()
                        .bucket(bucketName)
                        .delete(builder -> builder.objects(deletionList))
                        .build();

                s3Client.deleteObjects(deleteS3Objects);
            }

            continuationToken = objectList.nextContinuationToken();
        } while (continuationToken != null);
    }

    // ## HELPER FUNCTIONS ##
    private HashMap<String, String> generateFileUrl(GameProfileDetailsModel gameProfileDetailsModel) {

        HashMap<String, String> gameFiles = new HashMap<>();

        if (gameProfileDetailsModel.getGameFileDetailsModel().getFileName() != null) {

            try {

                for (int i = 0; gameProfileDetailsModel.getGameFileDetailsModel().getFileName().size() > i; i++) {

                    String gameUrl = "https://www.warpedcitadel.com/games/" + gameProfileDetailsModel.getGameProfileUUID() +
                            "/files/" + gameProfileDetailsModel.getGameFileDetailsModel().getFileName().get(i);
                    String gameFileName = gameProfileDetailsModel.getGameFileDetailsModel().getFileName().get(i);

                    gameFiles.put(gameFileName, cloudFrontCookieMaker.generateSignedUrl(gameUrl));
                }
            } catch (Exception exception) {

                throw new RuntimeException("Failed to generate file url for game: " + gameProfileDetailsModel.getTitle());
            }
        }

        return gameFiles;
    }


    private String generateHtmlGameUrl(GameProfileDetailsModel gameProfileDetailsModel) {

        try {

            if (gameProfileDetailsModel.getGameFileDetailsModel().getBrowserFileName() != null) {

                String result = findFilesByExtension(gameProfileDetailsModel);
                String gameUrl = "https://www.warpedcitadel.com/" + result;

                return gameUrl;
            }

            return null;
        } catch (Exception exception) {

            throw new RuntimeException("Failed to generate html url for game: " + gameProfileDetailsModel.getTitle());
        }
    }


    private String findFilesByExtension(GameProfileDetailsModel gameProfileDetailsModel) {

        String filePath = "games/" + gameProfileDetailsModel.getGameProfileUUID() + "/files/" +
                gameProfileDetailsModel.getGameFileDetailsModel().getBrowserFileName();

        String prefix = filePath.endsWith("/") ? filePath : filePath + "/";

        List<String> fileKeys = new ArrayList<>();

        try {

            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(gameBucketName)
                    .prefix(prefix)
                    .build();

            ListObjectsV2Iterable responses = s3Client.listObjectsV2Paginator(request);

            responses.contents().stream()
                    .map(s3Object -> s3Object.key())
                    .filter(key -> key.toLowerCase().endsWith(".html"))
                    .forEach(htmlKey -> {
                        fileKeys.add(htmlKey);
                    });

            return fileKeys.getFirst();

        } catch (Exception exception) {

            throw new RuntimeException("Failed to retrieve game .html file");
        }
    }

    private String generateGameCoverUrl(GameProfileDetailsModel gameProfileDetailsModel) {

        String imageUrl = "https://www.warpedcitadel.com/images/games/" + gameProfileDetailsModel.getGameProfileUUID() +
                "/gameImages/" + gameProfileDetailsModel.getGameProfileImagesModel().getCoverImg();

        return cloudFrontCookieMaker.generateSignedUrl(imageUrl);
    }

    private List<String> generateGameImageUrl(GameProfileDetailsModel gameProfileDetailsModel) {

        List<String> gameImageUrls = new ArrayList<>();

        for (int i = 0; gameProfileDetailsModel.getGameProfileImagesModel().getGameImg().size() > i; i++) {

            String imageUrl = "https://www.warpedcitadel.com/images/games/" + gameProfileDetailsModel.getGameProfileUUID() +
                    "/gameImages/" + gameProfileDetailsModel.getGameProfileImagesModel().getGameImg().get(i);

            gameImageUrls.add(cloudFrontCookieMaker.generateSignedUrl(imageUrl));
        }

        return gameImageUrls;
    }
}
