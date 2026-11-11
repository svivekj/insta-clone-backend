-- 1. Users Table
CREATE TABLE
    users (
        id BIGSERIAL PRIMARY KEY,
        username VARCHAR(50) NOT NULL UNIQUE,
        email VARCHAR(100) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        profile_picture_url VARCHAR(500),
        bio VARCHAR(150)
    );

-- 2. User Followers Self-Referencing Join Table (Matches User.java @ManyToMany)
CREATE TABLE
    user_followers (
        user_id BIGINT NOT NULL,
        follower_id BIGINT NOT NULL,
        PRIMARY KEY (user_id, follower_id), --question 
        CONSTRAINT fk_user_follower_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
        CONSTRAINT fk_user_follower_follower FOREIGN KEY (follower_id) REFERENCES users (id) ON DELETE CASCADE
    );

-- 3. Posts Table
CREATE TABLE
    posts (
        id BIGSERIAL PRIMARY KEY,
        caption TEXT,
        image_url VARCHAR(500) NOT NULL,
        user_id BIGINT NOT NULL,
        created_at TIMESTAMP
        WITH
            TIME ZONE DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_post_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
    );

-- 4. Likes Table
CREATE TABLE
    likes (
        id BIGSERIAL PRIMARY KEY,
        user_id BIGINT NOT NULL,
        post_id BIGINT NOT NULL,
        created_at TIMESTAMP
        WITH
            TIME ZONE DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_like_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
            CONSTRAINT fk_like_post FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
            CONSTRAINT unique_user_post_like UNIQUE (user_id, post_id)
    );

-- 5. Comments Table
CREATE TABLE
    comments (
        id BIGSERIAL PRIMARY KEY,
        text TEXT NOT NULL,
        created_at TIMESTAMP
        WITH
            TIME ZONE DEFAULT CURRENT_TIMESTAMP,
            user_id BIGINT NOT NULL,
            post_id BIGINT NOT NULL,
            CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
            CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE
    );