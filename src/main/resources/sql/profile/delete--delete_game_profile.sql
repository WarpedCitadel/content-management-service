DELETE FROM wc01.game_profile
WHERE id = (SELECT
				gp.id
			FROM wc01.game_profile gp
			INNER JOIN wc01.app_user au
				ON gp.app_user_id = au.id
			WHERE au.user_uuid = ?::UUID
				AND gp.game_profile_uuid = ?::UUID
			);