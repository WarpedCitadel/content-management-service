package com.warpedcitadel.contentmanagementservice.trending;

import com.warpedcitadel.contentmanagementservice.trending.model.TrendingModel;
import com.warpedcitadel.contentmanagementservice.util.SQLFileReader;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TrendingRepository {

    private final DataSource database;

    private final SQLFileReader loadSQL = new SQLFileReader();

    public TrendingRepository(DataSource database) {
        this.database = database;
    }


    protected Slice<TrendingModel> getTrendingGames(Pageable pageable, List<Object> attributesList) {

        String selectSQL = loadSQL.loadSQL("/trending/select--get_trending_games.sql");

        List<TrendingModel> trendingGameList = new ArrayList<>();

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

                TrendingModel game = new TrendingModel(
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
}
