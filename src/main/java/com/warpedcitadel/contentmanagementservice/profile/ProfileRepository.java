package com.warpedcitadel.contentmanagementservice.profile;

import com.warpedcitadel.contentmanagementservice.profile.model.GameProfileDetailsModel;
import com.warpedcitadel.contentmanagementservice.profile.model.GameProfileModel;
import com.warpedcitadel.contentmanagementservice.util.SQLFileReader;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProfileRepository {

    private final DataSource database;

    private final SQLFileReader loadSQL = new SQLFileReader();

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

            while (resultSet.next()) {

            gameProfileUUID = resultSet.getString("game_profile_uuid");
            }

        } catch (SQLException exception) {

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

        } catch (SQLException exception) {

            throw new RuntimeException("Failed to update game profile for profile id: "
                    + gameProfileModel.getGameProfileUUID());
        }
    }


    protected GameProfileDetailsModel getGameProfile(String gameProfileUUID) {

        String selectSql = loadSQL.loadSQL("/profile/select--get_game_profile.sql");

        try (Connection connection = database.getConnection();
        PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

            selectStatement.setString(1, gameProfileUUID);

            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {

                GameProfileDetailsModel gameProfile = new GameProfileDetailsModel();

                gameProfile.setGameProfileUUID(resultSet.getString("game_profile_uuid"));
                gameProfile.setTitle(resultSet.getString("title"));
                gameProfile.setDescription(resultSet.getString("description"));
                gameProfile.setGenreType(resultSet.getString("genre_type"));
                gameProfile.setGameType(resultSet.getString("game_type_name"));
                gameProfile.setCreatedDtm(resultSet.getString("created_dtm"));
                gameProfile.setCoverImg(resultSet.getString("cover_img_uuid"));
                gameProfile.setDisplayName(resultSet.getString("display_name"));
                gameProfile.setUserUUID(resultSet.getString("user_uuid"));

                List<String> platformOSList = new ArrayList<>(4);
                Array osArray = resultSet.getArray("platform_os");

                if (osArray != null) {
                    String[] osList = (String[]) osArray.getArray();

                    for (String osType : osList) {
                        platformOSList.add(osType);
                    }
                    gameProfile.setPlatformOS(platformOSList);
                }

                List<String> gameImgList = new ArrayList<>(5);
                Array imgUUIDArray = resultSet.getArray("game_img_uuid");

                if (imgUUIDArray != null) {
                    Object[] imgList = (Object[]) imgUUIDArray.getArray();

                    for (Object imgUUID : imgList) {
                        gameImgList.add(imgUUID.toString());
                    }
                    gameProfile.setGameImg(gameImgList);
                }

                return gameProfile;
            }

        } catch (SQLException exception) {

            throw new RuntimeException("Failed to retrieve game profile id: " + gameProfileUUID);
        }

        return null;
    }
}
