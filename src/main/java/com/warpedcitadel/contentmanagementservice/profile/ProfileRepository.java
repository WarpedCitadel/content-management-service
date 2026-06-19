package com.warpedcitadel.contentmanagementservice.profile;

import com.warpedcitadel.contentmanagementservice.profile.model.GameProfileModel;
import com.warpedcitadel.contentmanagementservice.util.SQLFileReader;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

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

            throw new RuntimeException("Failed to update game profile");
        }
    }
}
