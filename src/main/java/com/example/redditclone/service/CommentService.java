package com.example.redditclone.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.redditclone.dto.CommentsDto;
import com.example.redditclone.dto.SubredditDto;
import com.example.redditclone.exception.PostNotFoundException;

import com.example.redditclone.model.Comment;
import com.example.redditclone.model.NotificationEmail;
import com.example.redditclone.model.Post;
import com.example.redditclone.model.User;
import com.example.redditclone.repository.CommentRepository;
import com.example.redditclone.repository.PostRepository;
import com.example.redditclone.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static java.util.stream.Collectors.toList;

import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {

	 private static final String POST_URL = "";

	    //private final CommentMapper commentMapper;
	    private final PostRepository postRepository;
	    private final CommentRepository commentRepository;
	    private final UserRepository userRepository;
	    private final AuthService authService;
	    private final MailContentBuilder mailContentBuilder;
	    private final MailService mailService;
	    private ModelMapper modelmapper;
	    

	    public void createComment(CommentsDto commentsDto) {
	        Post post = postRepository.findById(commentsDto.getPostId())
	                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
	       // Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
	        Comment comment = new Comment();
	        comment.setText(commentsDto.getText());
	        comment.setPost(post);
	        comment.setUser(authService.getCurrentUser());
	        comment.setCreatedDate(Instant.now());
	        commentRepository.save(comment);

	        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
	        sendCommentNotification(message, post.getUser());
	    }

	    public List<CommentsDto> getCommentByPost(Long postId) {
	        Post post = postRepository.findById(postId)
	                .orElseThrow(() -> new PostNotFoundException(postId.toString()));
	        return commentRepository.findByPost(post)
	                .stream()
	                .map(comment -> modelmapper.map(comment, CommentsDto.class))
	                .collect(toList());
	    }

	    public List<CommentsDto> getCommentsByUser(String userName) {
	        User user = userRepository.findByUsername(userName)
	                .orElseThrow(() -> new UsernameNotFoundException(userName));
	        return commentRepository.findAllByUser(user)
	                .stream()
	                .map(comment -> modelmapper.map(comment, CommentsDto.class))
	                .collect(toList());
	    }

	    private void sendCommentNotification(String message, User user) {
	        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
	    }
}
