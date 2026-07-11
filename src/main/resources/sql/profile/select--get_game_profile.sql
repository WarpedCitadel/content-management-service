WITH sel_img_uuid_cte AS (
	SELECT
		game_profile_id,
		array_agg(gi.file_name)
			filter (WHERE gi.iscover = FALSE)
				AS game_img
	FROM wc01.game_image gi
	GROUP BY
		game_profile_id
),
sel_game_file_cte AS (
		SELECT
		gf.game_profile_id,
		array_agg(gf.file_name)
			filter (WHERE gf.isbrowser = FALSE)
			AS file_name,
		array_agg(gf.file_uuid)
		filter (WHERE gf.isbrowser = FALSE)
			AS file_uuid,
		array_agg(gf.status_type_id)
		filter (WHERE gf.isbrowser = FALSE)
	FROM wc01.game_file gf
	where gf.status_type_id = 4
	GROUP BY gf.game_profile_id
),
sel_platform_cte AS (
	SELECT
		gp.game_profile_id,
		array_agg(p.platform_type)
			AS platform_os
	FROM wc01.game_platform gp
		INNER JOIN wc01.platform p
	ON gp.platform_id = p.id
	GROUP BY gp.game_profile_id
)
SELECT DISTINCT
	gp.game_profile_uuid,
	gp.title,
	gp.description,
	gg.genre_type,
	gt.game_type_name,
	sp.platform_os,
	gp.created_dtm,
	gf.file_uuid as browser_game,
	sg.file_name,
	gi.file_name AS cover_img,
	si.game_img,
	COALESCE(aup.display_name, au.username)
		as display_name,
	au.user_uuid
FROM wc01.game_profile gp
INNER JOIN sel_game_file_cte sg
	ON gp.id = sg.game_profile_id
INNER JOIN wc01.game_file gf
	ON gp.id = gf.game_profile_id
	and isbrowser = true
	and gf.status_type_id = 4
INNER JOIN wc01.app_user au
	ON gp.app_user_id = au.id
INNER JOIN wc01.game_genre gg
	ON gp.game_genre_id = gg.id
INNER JOIN wc01.game_type gt
	ON gp.game_type_id = gt.id
LEFT JOIN sel_platform_cte sp
	ON gp.id = sp.game_profile_id
INNER JOIN wc01.game_image gi
	ON gp.id = gi.game_profile_id
	AND iscover = TRUE
LEFT JOIN sel_img_uuid_cte si
	ON gp.id = si.game_profile_id
LEFT JOIN wc01.app_user_profile aup
    ON au.id = aup.app_user_id
WHERE gp.game_profile_uuid = ?::UUID;