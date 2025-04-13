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

-- Insert roles
INSERT INTO roles (type)
VALUES
    ('admin');

-- Insert user roles
INSERT INTO user_roles (user_id, role_id)
VALUES
    (1, 1);

-- Insert songs
INSERT INTO songs (name, file_name, media_type, length_millis, uploader_id, genre_id, language_id)
VALUES
    ('Bohemian Rhapsody', 'bohemian_rhapsody.wav', 'audio/wav', 354812, 1, 1, 1),  -- Rock, English
    ('Hotel California', 'hotel_california.wav', 'audio/wav', 390266, 1, 1, 1),  -- Rock, English
    ('Stairway to Heaven', 'stairway_to_heaven.wav', 'audio/wav', 482394, 1, 1, 1),  -- Rock, English
    ('Imagine', 'imagine.wav','audio/wav', 183533, 1, 2, 1),  -- Pop, English
    ('Smells Like Teen Spirit', NULL, NULL, NULL, 1, 14, 1),  -- Alternative, English
    ('Billie Jean', NULL, NULL, NULL, 1, 2, 1),  -- Pop, English
    ('Hey Jude', NULL, NULL, NULL, 1, 1, 1),  -- Rock, English
    ('Like a Rolling Stone', NULL, NULL, NULL, 1, 1, 1),  -- Rock, English
    ('Sweet Child o’ Mine', NULL, NULL, NULL, 1, 1, 1),  -- Rock, English
    ('Thriller', NULL, NULL, NULL, 1, 2, 1),  -- Pop, English
    ('Rolling in the Deep', NULL, NULL, NULL, 1, 2, 1),  -- Pop, English
    ('Wonderwall', NULL, NULL, NULL, 1, 14, 1),  -- Alternative, English
    ('Bohemian Like You', NULL, NULL, NULL, 1, 14, 1),  -- Alternative, English
    ('Purple Rain', NULL, NULL, NULL, 1, 15, 1),  -- Soul, English
    ('Lose Yourself', NULL, NULL, NULL, 1, 4, 1),  -- Soul, English
    ('Temporary', 'temporary_01.wav', 'audio/wav', 5943, 1, NULL, NULL),
    ('montage', 'montage.wav', 'audio/wav', 94650, 1, 6, 15);
INSERT INTO songs (name, file_name, media_type, length_millis, uploader_id, genre_id, language_id)
VALUES
    ('הפרח בגני',
    NULL, NULL, NULL, 1, 7, 15),  -- Reggae, Hebrew (Zohar Argov)
    ('תן לי יד',
    NULL, NULL, NULL, 1, 13, 15),  -- R&B, Hebrew (Kobi Peretz)
    ('שירת הסטלבים',
    NULL, NULL, NULL, 1, 1, 15),  -- Reggae, Hebrew (Hatikva 6)
    ('עוד יבוא שלום עלינו',
    NULL, NULL, NULL, 1, 11, 15),  -- Folk, Hebrew (Popular Israeli song)
    ('ניצוץ',
    NULL, NULL, NULL, 1, 14, 15);  -- Alternative, Hebrew (Mashina)

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