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
INSERT INTO songs (file_name, name, uploader_id, genre_id, language_id)
VALUES
    ('tmp.wav', 'Bohemian Rhapsody', 1, 1, 1),  -- Rock, English
    (NULL, 'Hotel California', 1, 1, 1),  -- Rock, English
    (NULL, 'Stairway to Heaven', 1, 1, 1),  -- Rock, English
    (NULL, 'Imagine', 1, 2, 1),  -- Pop, English
    (NULL, 'Smells Like Teen Spirit', 1, 14, 1),  -- Alternative, English
    (NULL, 'Billie Jean', 1, 2, 1),  -- Pop, English
    (NULL, 'Hey Jude', 1, 1, 1),  -- Rock, English
    (NULL, 'Like a Rolling Stone', 1, 1, 1),  -- Rock, English
    (NULL, 'Sweet Child o’ Mine', 1, 1, 1),  -- Rock, English
    (NULL, 'Thriller', 1, 2, 1),  -- Pop, English
    (NULL, 'Rolling in the Deep', 1, 2, 1),  -- Pop, English
    (NULL, 'Wonderwall', 1, 14, 1),  -- Alternative, English
    (NULL, 'Bohemian Like You', 1, 14, 1),  -- Alternative, English
    (NULL, 'Purple Rain', 1, 15, 1),  -- Soul, English
    (NULL, 'Lose Yourself', 1, 4, 1);  -- Hip-Hop, English
INSERT INTO songs (file_name, name, uploader_id, genre_id, language_id)
VALUES
    (NULL, 'הפרח בגני', 1, 7, 15),  -- Reggae, Hebrew (Zohar Argov)
    (NULL, 'תן לי יד', 1, 13, 15),  -- R&B, Hebrew (Kobi Peretz)
    (NULL, 'שירת הסטלבים', 1, 1, 15),  -- Reggae, Hebrew (Hatikva 6)
    (NULL, 'עוד יבוא שלום עלינו', 1, 11, 15),  -- Folk, Hebrew (Popular Israeli song)
    (NULL, 'ניצוץ', 1, 14, 15);  -- Alternative, Hebrew (Mashina)

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