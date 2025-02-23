-- Mock data generated mostly via chat gpt

-- Insert Language
INSERT INTO languages (name)
VALUES
    ('English'),
    ('Spanish'),
    ('French'),
    ('German'),
    ('Italian'),
    ('Portuguese'),
    ('Russian'),
    ('Chinese'),
    ('Japanese'),
    ('Korean'),
    ('Arabic'),
    ('Hindi'),
    ('Turkish'),
    ('Dutch'),
    ('Hebrew');

-- Insert genres
INSERT INTO genres (name)
VALUES
    ('Rock'),
    ('Pop'),
    ('Jazz'),
    ('Hip-Hop'),
    ('Classical'),
    ('Electronic'),
    ('Reggae'),
    ('Blues'),
    ('Metal'),
    ('Country'),
    ('Folk'),
    ('Punk'),
    ('R&B'),
    ('Alternative'),
    ('Soul');

-- Insert users
INSERT INTO users (display_name, password, salt, username)
VALUES
    ('Admin', 'MSvtrJnTyYJSJjFy2tM8GjpCyOSCs+gmTrb8pXV3JRQ=', 'oCBArC64Z1HTlAws2tPVzQ==', '');

-- Insert songs
INSERT INTO songs (file_name, name, uploader_id, genre_id, language_id)
VALUES
    ('bohemian_rhapsody.mp3', 'Bohemian Rhapsody', 1, 1, 1),  -- Rock, English
    ('hotel_california.mp3', 'Hotel California', 1, 1, 1),  -- Rock, English
    ('stairway_to_heaven.mp3', 'Stairway to Heaven', 1, 1, 1),  -- Rock, English
    ('imagine.mp3', 'Imagine', 1, 2, 1),  -- Pop, English
    ('smells_like_teen_spirit.mp3', 'Smells Like Teen Spirit', 1, 14, 1),  -- Alternative, English
    ('billie_jean.mp3', 'Billie Jean', 1, 2, 1),  -- Pop, English
    ('hey_jude.mp3', 'Hey Jude', 1, 1, 1),  -- Rock, English
    ('like_a_rolling_stone.mp3', 'Like a Rolling Stone', 1, 1, 1),  -- Rock, English
    ('sweet_child_o_mine.mp3', 'Sweet Child o’ Mine', 1, 1, 1),  -- Rock, English
    ('thriller.mp3', 'Thriller', 1, 2, 1),  -- Pop, English
    ('rolling_in_the_deep.mp3', 'Rolling in the Deep', 1, 2, 1),  -- Pop, English
    ('wonderwall.mp3', 'Wonderwall', 1, 14, 1),  -- Alternative, English
    ('bohemian_like_you.mp3', 'Bohemian Like You', 1, 14, 1),  -- Alternative, English
    ('purple_rain.mp3', 'Purple Rain', 1, 15, 1),  -- Soul, English
    ('lose_yourself.mp3', 'Lose Yourself', 1, 4, 1);  -- Hip-Hop, English
INSERT INTO songs (file_name, name, uploader_id, genre_id, language_id)
VALUES
    ('haperach_bagan.mp3', 'הפרח בגני', 1, 7, 15),  -- Reggae, Hebrew (Zohar Argov)
    ('ten_li_yad.mp3', 'תן לי יד', 1, 13, 15),  -- R&B, Hebrew (Kobi Peretz)
    ('shirat_hastalav.mp3', 'שירת הסטלבים', 1, 1, 15),  -- Reggae, Hebrew (Hatikva 6)
    ('od_yevo_shalom.mp3', 'עוד יבוא שלום עלינו', 1, 11, 15),  -- Folk, Hebrew (Popular Israeli song)
    ('mashina_nitzotz.mp3', 'ניצוץ', 1, 14, 15);  -- Alternative, Hebrew (Mashina)

-- Insert playlists
INSERT INTO playlists (is_modifiable, is_private, owner_id, name)
VALUES
    (true, false, 1, 'Driving playlist'),
    (true, false, 1, 'Chill');

-- Insert playlist songs
INSERT INTO playlist_songs (playlist_id, song_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 3),
    (2, 2),
    (2, 5);