package com.example.redditclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.redditclone.model.SubReddit;

import java.util.Optional;

public interface SubredditRepository extends JpaRepository<SubReddit, Long> {
    Optional<SubReddit> findByName(String subredditName);
}
