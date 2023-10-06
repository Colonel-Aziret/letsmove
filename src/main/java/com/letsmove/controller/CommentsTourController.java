package com.letsmove.controller;

import com.letsmove.entity.CommentsPlace;
import com.letsmove.entity.CommentsTour;
import com.letsmove.entity.Place;
import com.letsmove.entity.Tour;
import com.letsmove.service.CommentsTourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;


@Controller
public class CommentsTourController {
    @Autowired
    private CommentsTourService commentsTourService;

//    @PostMapping(value = "/save_tour_comment")
//    public String saveTourComment(@ModelAttribute(name = "commentsTour") CommentsTour commentsTour, @RequestParam(name = "tourID") Integer tourId) {
//        commentsTourService.save(commentsTour, tourId);
//        return "redirect:/look_tour?tourId=" + tourId;
//    }

    @PostMapping(value = "/save_tour_comment")
    public String savePlaceComment(@RequestParam(name = "commentText") String commentText, @RequestParam(name = "tourId") Integer tourId) {
        // Create and save the comment
        commentsTourService.save(commentText, tourId);

        return "redirect:/look_tour?tourId=" + tourId;
    }
}
