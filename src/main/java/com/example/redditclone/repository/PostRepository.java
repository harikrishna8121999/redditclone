package com.example.redditclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.redditclone.model.Post;
import com.example.redditclone.model.SubReddit;
import com.example.redditclone.model.User;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(SubReddit subreddit);

    List<Post> findByUser(User user);
}
