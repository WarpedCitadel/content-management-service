WITH sel_game_profile_cte AS (
    SELECT id
    FROM wc01.app_user
    WHERE user_uuid = ?::UUID
),
ins_game_profile_cte AS (
    INSERT INTO wc01.game_profile (
        app_user_id,
        title,
        short_desc,
        description,
        game_genre_id,
        game_type_id
    )
    SELECT
        id,
        ?,
        ?,
        ?,
        ?,
        ?
    FROM sel_game_profile_cte
    RETURNING id, game_profile_uuid
),
ins_platform_os_cte AS (
    INSERT INTO wc01.game_platform (
        platform_id,
        game_profile_id
    )
    SELECT
        unnest( ?::INT[]),
        id
    FROM ins_game_profile_cte
)
SELECT game_profile_uuid
FROM ins_game_profile_cte;