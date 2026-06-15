package com.warpedcitadel.contentmanagementservice.trending;

import com.warpedcitadel.contentmanagementservice.trending.model.TrendingGamesModel;
import com.warpedcitadel.contentmanagementservice.util.SQLFileReader;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class TrendingRepository {

    private final DataSource database;

    private final SQLFileReader loadSQL = new SQLFileReader();

    public TrendingRepository(DataSource database) {
        this.database = database;
    }


    protected Slice<TrendingGamesModel> getTrendingGames(Pageable pageable, List<Object> attributesList) {

        String selectSQL = loadSQL.loadSQL("/trending/select--get_trending_games.sql");

        List<TrendingGamesModel> trendingGameList = new ArrayList<>();

        try (Connection connection = database.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {

            int request;
            for (request = 0; attributesList.size() > request; request++) {

                if (attributesList.get(request) != null && !attributesList.isEmpty()) {
                    selectStatement.setObject(request + 1, attributesList.get(request));
                } else {
                    selectStatement.setObject(request + 1, null);
                }
            }

            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {

                TrendingGamesModel game = new TrendingGamesModel(
                        resultSet.getString("file_uuid"),
                        resultSet.getString("img_uuid"),
                        resultSet.getString("title"),
                        resultSet.getString("short_desc"),
                        resultSet.getString("genre_type"),
                        resultSet.getString("created_dtm")
                );

                trendingGameList.add(game);
            }

            boolean hasNext = trendingGameList.size() > pageable.getPageSize();

            if (hasNext) {
                trendingGameList.remove(trendingGameList.size() - 1);
            }

            return new SliceImpl<>(trendingGameList, pageable, hasNext);

        } catch (SQLException exception) {
            throw new RuntimeException("Failed to retrieve list of trending games");
        }
    }


    protected HashMap<Integer, String> getGameGenres() {

        String selectSql = loadSQL.loadSQL("/trending/select--get_game_genres.sql");

        HashMap<Integer, String> genres = new HashMap<>(10);

        try (Connection connection = database.getConnection();
             Statement selectStatement = connection.createStatement();
                ResultSet resultset = selectStatement.executeQuery(selectSql)) {

                while (resultset.next()) {

                    int id = resultset.getInt("id");
                    String genre = resultset.getString("genre_type");

                    genres.put(id, genre);
                }
        } catch (SQLException exception) {

            throw new RuntimeException("Failed to retrieve a list of genres");
        }

        return genres;
    }
}
