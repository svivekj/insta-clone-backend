-- Clear existing data and reset ID sequences safely
TRUNCATE TABLE comments,
likes,
user_followers,
posts,
users RESTART IDENTITY CASCADE;

-- 1. Insert 3 Sample Users
INSERT INTO
    users (
        username,
        email,
        password,
        bio,
        profile_picture_url
    )
VALUES
    (
        'alex_dev',
        'alex@meta.com',
        'password123',
        'Building Spring Boot & React Apps',
        'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150'
    ),
    (
        'sarah_design',
        'sarah@meta.com',
        'password123',
        'UI/UX Designer • Visual Thinker',
        'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150'
    ),
    (
        'liam_code',
        'liam@meta.com',
        'password123',
        'Full-stack & Coffee enthusiast ☕',
        'https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150'
    );

-- 2. Insert Sample Posts
INSERT INTO
    posts (caption, image_url, user_id)
VALUES
    (
        'Working on a new Spring Boot 4 & React project! 🚀',
        'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=800',
        1
    ),
    (
        'Just finished the new design system dark mode UI. Thoughts? 🎨',
        'https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=800',
        2
    );

-- 3. Insert Sample Likes 
INSERT INTO
    likes (post_id, user_id)
VALUES
    (1, 2),
    (1, 3),
    (2, 1);

-- 4. Insert Sample Comments
INSERT INTO
    comments (text, post_id, user_id)
VALUES
    (
        'Looks incredible! Can’t wait to see the final UI.',
        1,
        2
    ),
    (
        'Clean architecture setup. Love to see Flyway in action!',
        1,
        3
    );

-- 5. Insert Follower Relationships 
INSERT INTO
    user_followers (user_id, follower_id)
VALUES
    (1, 2),
    (1, 3),
    (2, 3);