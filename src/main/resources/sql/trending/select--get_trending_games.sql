SELECT
	af.file_uuid,
	f.img_uuid,
	f.title,
	f.short_desc,
	g.genre_type,
	f.created_dtm
FROM wc01.app_file af
    JOIN wc01.fnc_search_trending_games_select(     ?,
                                          		    ?
    ) f
    	ON af.id = f.file_id
   	LEFT JOIN wc01.genre g
   		ON g.id = f.genre_id
WHERE 1=1
ORDER BY f.title ASC
LIMIT COALESCE( ?, 30) OFFSET COALESCE ( ?, 0);