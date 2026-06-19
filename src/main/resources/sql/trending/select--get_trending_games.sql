SELECT DISTINCT
	gp.id,
	gp.game_profile_uuid,
	f.img_uuid,
	f.title,
	f.short_desc,
	g.genre_type,
	f.platform_os,
	gp.created_dtm
FROM wc01.game_profile gp
    JOIN wc01.fnc_search_trending_games_select(     ?,
                                          		    ?,
                                          		    ?,
                                          		    ?,
                                          		    ?
    ) f
    ON gp.id = f.game_profile_id
LEFT JOIN wc01.game_genre g
    ON g.id = f.game_genre_id
WHERE 1=1
ORDER BY f.title ASC
LIMIT COALESCE( ?, 30) OFFSET COALESCE( ?, 0);