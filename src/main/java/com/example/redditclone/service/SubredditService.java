package com.example.redditclone.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.redditclone.dto.SubredditDto;
import com.example.redditclone.exception.SpringRedditException;
import com.example.redditclone.exception.SubredditNotFoundException;

import com.example.redditclone.model.SubReddit;
import com.example.redditclone.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import static java.time.Instant.now;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class SubredditService {
	
	private final SubredditRepository subredditRepository;
	
	private ModelMapper modelmapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        SubReddit save = subredditRepository.save(modelmapper.map(subredditDto, SubReddit.class));
        subredditDto.setId(save.getId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subReddit -> modelmapper.map(subReddit, SubredditDto.class))
                .collect(toList());
    }

    public SubredditDto getSubreddit(Long id) {
        SubReddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));
        return modelmapper.map(subreddit, SubredditDto.class);
    }
}
