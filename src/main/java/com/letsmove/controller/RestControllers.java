package com.letsmove.controller;

import com.letsmove.entity.CommentsPlace;
import com.letsmove.entity.CommentsTour;
import com.letsmove.entity.Place;
import com.letsmove.entity.Tour;
import com.letsmove.service.CommentsPlaceService;
import com.letsmove.service.CommentsTourService;
import com.letsmove.service.PlaceService;
import com.letsmove.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestControllers {
    @Autowired
    private PlaceService placeService;

    @Autowired
    private TourService tourService;
    @Autowired
    private CommentsPlaceService commentsPlaceService;

    @Autowired
    private CommentsTourService commentsTourService;
    @GetMapping("/get_comments/{placeID}")
    @ResponseBody
    public List<CommentsPlace> getCommentsForPlace(@PathVariable(name = "placeID") Integer placeId) {
        Place place = placeService.getPlaceById(placeId);
        CommentsPlace commentsPlace = new CommentsPlace();
        commentsPlace.setPlaceID(place);
        ArrayList<CommentsPlace> commentsPlaces = (ArrayList<CommentsPlace>) commentsPlaceService.getAllCommentsPlace(place);
        return commentsPlaces;
    }







    @GetMapping(value = "/get_comments_tour/{tourID}")
    @ResponseBody
    public List<CommentsTour> getCommentsForTour(@PathVariable(name = "tourID") Integer tourID) {
        Tour tour = tourService.getTourById(tourID);
        CommentsTour commentsTour = new CommentsTour();
        commentsTour.setTourID(tour);

        return commentsTourService.getAllCommentsTour(tour);
    }


}
