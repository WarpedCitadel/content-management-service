package com.warpedcitadel.contentmanagementservice.profile;

import com.warpedcitadel.contentmanagementservice.enums.PlatformOS;
import com.warpedcitadel.contentmanagementservice.profile.model.GameFileDetailsModel;
import com.warpedcitadel.contentmanagementservice.profile.model.GameProfileDetailsModel;
import com.warpedcitadel.contentmanagementservice.profile.model.GameProfileImagesModel;
import com.warpedcitadel.contentmanagementservice.profile.model.GameProfileModel;
import com.warpedcitadel.contentmanagementservice.util.SQLFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProfileRepository {

    private final DataSource database;

    private final SQLFileReader loadSQL = new SQLFileReader();
    private static final Logger log = LoggerFactory.getLogger(ProfileRepository.class);

    public ProfileRepository(DataSource database) {
        this.database = database;
    }


    protected String createGameProfile(GameProfileModel gameProfileModel) {
        String insertSql = loadSQL.loadSQL("/profile/insert--create_game_profile.sql");
        String gameProfileUUID = null;
        try (Connection connection = database.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
            Array osArray = connection.createArrayOf("integer", gameProfileModel.getPlatformOS());
            insertStatement.setString(1, gameProfileModel.getUserUUID());
            insertStatement.setString(2, gameProfileModel.getTitle());
            insertStatement.setString(3, gameProfileModel.getShortDesc());
            insertStatement.setString(4, gameProfileModel.getDescription());
            insertStatement.setInt(5, gameProfileModel.getGameGenre());
            insertStatement.setInt(6, gameProfileModel.getGameType());
            insertStatement.setArray(7, osArray);
            ResultSet resultSet = insertStatement.executeQuery();
            if (resultSet.next()) {
                gameProfileUUID = resultSet.getString("game_profile_uuid");
                log.info("Successfully created game profile, titled ({}) with Game profile ID: ({}) User profile ID: ({})",
                        gameProfileModel.getTitle(), gameProfileUUID, gameProfileModel.getUserUUID());
            }
        } catch (SQLException exception) {
            log.error("Failed to create game profile for user profile ID ({}) with game profile ID: ({}) Reason: ({})",
                    gameProfileModel.getUserUUID(), gameProfileUUID, exception.toString());
            throw new RuntimeException("Failed to create game profile");
        }
        return gameProfileUUID;
    }


    protected void updateGameProfile(GameProfileModel gameProfileModel) {
        String updateSql = loadSQL.loadSQL("/profile/update--update_game_profile.sql");
        try (Connection connection = database.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
            Array osArray = connection.createArrayOf("integer", gameProfileModel.getPlatformOS());
            updateStatement.setString(1, gameProfileModel.getUserUUID());
            updateStatement.setString(2, gameProfileModel.getGameProfileUUID());
            updateStatement.setString(3, gameProfileModel.getTitle());
            updateStatement.setString(4, gameProfileModel.getShortDesc());
            updateStatement.setString(5, gameProfileModel.getDescription());
            updateStatement.setInt(6, gameProfileModel.getGameGenre());
            updateStatement.setInt(7, gameProfileModel.getGameType());
            updateStatement.setArray(8, osArray);
            updateStatement.execute();
            log.info("Successfully updated game profile, titled ({}) with Game profile ID: ({}) User profile ID: ({})",
                    gameProfileModel.getTitle(), gameProfileModel.getGameProfileUUID(), gameProfileModel.getUserUUID());
        } catch (SQLException exception) {
            log.error("Failed to update game profile for user profile ID ({}) with game profile ID: ({}) Reason: ({})",
                    gameProfileModel.getUserUUID(), gameProfileModel.getGameProfileUUID(), exception.toString());
            throw new RuntimeException("Failed to update game profile");
        }
    }


    protected GameProfileDetailsModel getGameProfile(String gameProfileUUID) {
        String selectSql = loadSQL.loadSQL("/profile/select--get_game_profile.sql");
        try (Connection connection = database.getConnection();
        PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
            selectStatement.setString(1, gameProfileUUID);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                GameProfileDetailsModel gameProfile = new GameProfileDetailsModel();
                GameProfileImagesModel gameImages = new GameProfileImagesModel();
                GameFileDetailsModel gameFiles = new GameFileDetailsModel();
                gameProfile.setGameProfileUUID(resultSet.getString("game_profile_uuid"));
                gameProfile.setTitle(resultSet.getString("title"));
                gameProfile.setDescription(resultSet.getString("description"));
                gameProfile.setGenreType(resultSet.getString("genre_type"));
                gameProfile.setGameType(resultSet.getString("game_type_name"));
                gameProfile.setCreatedDtm(resultSet.getString("created_dtm"));
                gameImages.setCoverImg(resultSet.getString("cover_img"));
                gameProfile.setDisplayName(resultSet.getString("display_name"));
                gameProfile.setUserUUID(resultSet.getString("user_uuid"));

                List<String> fileNameList = new ArrayList<>();
                Array fileNameArray = resultSet.getArray("file_name");
                if (fileNameArray != null) {
                    String[] fileNames = (String[]) fileNameArray.getArray();
                    for (String fileName : fileNames) {
                        fileNameList.add(fileName);
                    }
                    gameFiles.setFileName(fileNameList);
                }

                List<Integer> fileOSList = new ArrayList<>();
                Array fileOSArray = resultSet.getArray("file_os");
                if (fileNameArray != null) {
                    Short[] fileOS = (Short[]) fileOSArray.getArray();
                    for (int file : fileOS) {
                        fileOSList.add(file);
                    }
                    gameFiles.setFileOS(fileOSList);
                }

                List<Integer> platformOSList = new ArrayList<>(PlatformOS.values().length);
                Array osArray = resultSet.getArray("platform_os");
                if (osArray != null) {
                    Integer[] osList = (Integer[]) osArray.getArray();

                    for (Integer osType : osList) {
                        platformOSList.add(osType);
                    }
                    gameProfile.setPlatformOS(platformOSList);
                }

                List<String> gameImgList = new ArrayList<>();
                Array imgUUIDArray = resultSet.getArray("game_img");
                if (imgUUIDArray != null) {
                    String[] imgList = (String[]) imgUUIDArray.getArray();

                    for (String imgUUID : imgList) {
                        gameImgList.add(imgUUID);
                    }
                    gameImages.setGameImg(gameImgList);
                }

                gameProfile.setGameFileDetailsModel(gameFiles);
                gameProfile.setGameProfileImagesModel(gameImages);
                return gameProfile;
            }

        } catch (SQLException exception) {
            log.error("Failed to get game profile with ID: ({}) Reason: ({})",
                    gameProfileUUID, exception.toString());
            throw new RuntimeException("Failed to retrieve requested game");
        }
        return null;
    }


    protected void deleteGameProfile(GameProfileModel gameProfileModel) {

        String deleteSql = loadSQL.loadSQL("/profile/delete--delete_game_profile.sql");

        try (Connection connection = database.getConnection();
        PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {

            deleteStatement.setString(1, gameProfileModel.getUserUUID());
            deleteStatement.setString(2, gameProfileModel.getGameProfileUUID());

            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Failed to delete game profile with uuid: " + gameProfileModel.getGameProfileUUID());
            }

        } catch (SQLException exception) {
            log.error("Failed to delete game profile title ({}) with ID: ({}) from user ID: ({}) Reason: ({})",
                   gameProfileModel.getTitle(), gameProfileModel.getGameProfileUUID(),
                    gameProfileModel.getUserUUID(), exception.toString());
            throw new RuntimeException("Failed to perform game profile deletion");
        }
    }
}
