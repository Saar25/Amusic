-- Mock data generated via chat gpt

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

-- Insert songs
INSERT INTO songs (file_name, name, uploader_id, genre_id, language_id)
VALUES
    ('bohemian_rhapsody.mp3', 'Bohemian Rhapsody', 7, 1, 1),  -- Rock, English
    ('hotel_california.mp3', 'Hotel California', 7, 1, 1),  -- Rock, English
    ('stairway_to_heaven.mp3', 'Stairway to Heaven', 7, 1, 1),  -- Rock, English
    ('imagine.mp3', 'Imagine', 7, 2, 1),  -- Pop, English
    ('smells_like_teen_spirit.mp3', 'Smells Like Teen Spirit', 7, 14, 1),  -- Alternative, English
    ('billie_jean.mp3', 'Billie Jean', 7, 2, 1),  -- Pop, English
    ('hey_jude.mp3', 'Hey Jude', 7, 1, 1),  -- Rock, English
    ('like_a_rolling_stone.mp3', 'Like a Rolling Stone', 7, 1, 1),  -- Rock, English
    ('sweet_child_o_mine.mp3', 'Sweet Child o’ Mine', 7, 1, 1),  -- Rock, English
    ('thriller.mp3', 'Thriller', 7, 2, 1),  -- Pop, English
    ('rolling_in_the_deep.mp3', 'Rolling in the Deep', 7, 2, 1),  -- Pop, English
    ('wonderwall.mp3', 'Wonderwall', 7, 14, 1),  -- Alternative, English
    ('bohemian_like_you.mp3', 'Bohemian Like You', 7, 14, 1),  -- Alternative, English
    ('purple_rain.mp3', 'Purple Rain', 7, 15, 1),  -- Soul, English
    ('lose_yourself.mp3', 'Lose Yourself', 7, 4, 1);  -- Hip-Hop, English
INSERT INTO songs (file_name, name, uploader_id, genre_id, language_id)
VALUES
    ('haperach_bagan.mp3', 'הפרח בגני', 7, 7, 15),  -- Reggae, Hebrew (Zohar Argov)
    ('ten_li_yad.mp3', 'תן לי יד', 7, 13, 15),  -- R&B, Hebrew (Kobi Peretz)
    ('shirat_hastalav.mp3', 'שירת הסטלבים', 7, 7, 15),  -- Reggae, Hebrew (Hatikva 6)
    ('od_yevo_shalom.mp3', 'עוד יבוא שלום עלינו', 7, 11, 15),  -- Folk, Hebrew (Popular Israeli song)
    ('mashina_nitzotz.mp3', 'ניצוץ', 7, 14, 15);  -- Alternative, Hebrew (Mashina)

