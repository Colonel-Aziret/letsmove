package com.letsmove.service;

import com.letsmove.dao.CommentsTourRepository;
import com.letsmove.entity.CommentsTour;
import com.letsmove.entity.Tour;
import com.letsmove.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentsTourService {
    @Autowired
    private CommentsTourRepository commentsTourRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TourService tourService;

    public List<CommentsTour> getAllCommentsTour(Tour id) {
        return commentsTourRepository.findAllByTourID(id);
    }

    public void save(String text, Integer tourId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users users = userService.findByLogin(authentication.getName());
        CommentsTour commentsTour = new CommentsTour();
        commentsTour.setTourID(tourService.getTourById(tourId));
        commentsTour.setUsersID(users);
        commentsTour.setComment(text);
        commentsTour.setCreatedDate(new Date());
        commentsTourRepository.save(commentsTour);
    }
}
