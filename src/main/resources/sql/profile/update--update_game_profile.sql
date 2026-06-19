WITH sel_game_profile_cte AS (
	SELECT gp.id
	FROM wc01.game_profile gp
		INNER JOIN wc01.app_user au
    		ON au.id = gp.app_user_id
	WHERE user_uuid = ?::UUID
	AND game_profile_uuid = ?::UUID
),
upt_game_profile_cte AS (
    UPDATE wc01.game_profile gp
    SET
        title = ?,
        short_desc = ?,
        description = ?,
        game_genre_id = ?,
        game_type_id = ?
    FROM sel_game_profile_cte sgp
    WHERE gp.id = sgp.id
    RETURNING gp.id
),
sel_platform_os_cte AS (
    SELECT unnest(?::INT[]) AS update_platform_id
),
del_game_profile_cte AS (
    DELETE FROM wc01.game_platform gpt
    USING upt_game_profile_cte ugp
    WHERE gpt.game_profile_id = ugp.id
      AND gpt.platform_id NOT IN
      (SELECT
        update_platform_id
       FROM sel_platform_os_cte
      )
),
ins_game_profile_cte AS (
    INSERT INTO wc01.game_platform (
    	platform_id,
    	game_profile_id
    	)
    SELECT spo.update_platform_id, ugp.id
    FROM sel_platform_os_cte spo
    	CROSS JOIN upt_game_profile_cte ugp
    ON CONFLICT (platform_id, game_profile_id) DO NOTHING
    RETURNING platform_id
)
SELECT * FROM ins_game_profile_cte;