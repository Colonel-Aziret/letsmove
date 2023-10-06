package com.letsmove.service;

import com.letsmove.dao.CommentsPlaceRepository;
import com.letsmove.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class CommentsPlaceService {
    @Autowired
    private CommentsPlaceRepository commentsPlaceRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PlaceService placeService;

    public List<CommentsPlace> getAllCommentsPlace(Place id) {
        return commentsPlaceRepository.findCommentsPlacesByPlaceID(id);
    }

    public void save(String commentText, Integer placeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users users = userService.findByLogin(authentication.getName());
        CommentsPlace commentsPlace = new CommentsPlace();
        commentsPlace.setPlaceID(placeService.getPlaceById(placeId));
        commentsPlace.setUsersID(users);
        commentsPlace.setCreatedDate(new Date());
        commentsPlace.setComment(commentText);
        commentsPlaceRepository.save(commentsPlace);
    }
}
