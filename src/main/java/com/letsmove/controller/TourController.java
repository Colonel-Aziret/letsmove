package com.letsmove.controller;

import com.letsmove.entity.CommentsTour;
import com.letsmove.entity.Tour;
import com.letsmove.entity.Users;
import com.letsmove.enums.Role;
import com.letsmove.enums.Status;
import com.letsmove.service.CommentsTourService;
import com.letsmove.service.TourService;
import com.letsmove.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;


@Controller
public class TourController {
    @Autowired
    private TourService tourService;
    @Autowired
    private CommentsTourService commentsTourService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/add_tour", method = RequestMethod.GET)
    public ModelAndView addTour() {
        ModelAndView modelAndView = new ModelAndView("addTour");
        modelAndView.addObject("tour", new Tour());
        return modelAndView;
    }


    @PostMapping(value = "/save_tour")
    public String saveTour(@ModelAttribute(name = "tour") Tour tour) {
        try {
            tourService.save(tour);
            return "guideMain";
        } catch (Exception e) {
            return "redirect:/add_tour";
        }
    }

    @RequestMapping(value = "/check_tour", method = RequestMethod.GET)
    public ModelAndView checkTour() {
        ModelAndView modelAndView = new ModelAndView("checkTour");
        ArrayList<Tour> allNewTour = tourService.getAllNewTour();
        modelAndView.addObject("allNewTour", allNewTour);
        ArrayList<Status> status = new ArrayList<Status>(Arrays.asList(Status.values()));
        modelAndView.addObject("statuses", status);
        return modelAndView;
    }


    @PostMapping(value = "/save_active_tour")
    public String addActiveTour(@RequestParam(name = "tourId") Integer tourId, @RequestParam(name = "status") String status) {
        tourService.updateTourStatus(tourId, status);
        return "redirect:/check_tour";
    }

    @RequestMapping(value = "/get_all_tour", method = RequestMethod.GET)
    public ModelAndView getAllTour() {
        ModelAndView modelAndView = new ModelAndView("Tours");
        ArrayList<Tour> allActiveTour = tourService.getAllActiveTour();
        modelAndView.addObject("allActiveTour", allActiveTour);
        return modelAndView;
    }


    @GetMapping(value = "/look_tour")
    public ModelAndView getBookTour(@RequestParam(name = "tourId") Integer tourId) {
        ModelAndView modelAndView = new ModelAndView("getTour");
        Tour tour = tourService.getTourById(tourId);
        CommentsTour commentsTour = new CommentsTour();
        commentsTour.setTourID(tour);
        modelAndView.addObject("tour", tour);
        modelAndView.addObject("commentsTour", commentsTour);
        modelAndView.addObject("allComments", commentsTourService.getAllCommentsTour(tour));
        return modelAndView;
    }

    @PostMapping(value = "/getTour")
    public String getTour(@RequestParam(name = "tourId") Integer id) {
        tourService.bookTour(id);
        return "redirect:/look_tour?tourId=" + id;
    }

    @RequestMapping(value = "/get_all_author_tour", method = RequestMethod.GET)
    public ModelAndView getAllAuthorTour() {
        ModelAndView modelAndView = new ModelAndView("AllAuthorTour");
        ArrayList<Tour> allAuthorTour = (ArrayList<Tour>) tourService.getAllAuthorTour();
        modelAndView.addObject("allAuthorTour", allAuthorTour);
        return modelAndView;
    }

    @RequestMapping(value = "/delete_all_tour", method = RequestMethod.GET)
    public ModelAndView deleteAllTour() {
        ModelAndView modelAndView = new ModelAndView("AllAuthorTour");
        ArrayList<Tour> allAuthorTour = tourService.getAllActiveTour();
        modelAndView.addObject("allAuthorTour", allAuthorTour);
        return modelAndView;
    }

    @PostMapping(value = "/delete_tour")
    public String changeRating(@RequestParam(name = "tourId") Integer tourId) {
        tourService.deleteTour(tourId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users users = userService.findByLogin(authentication.getName());
        if (users.getRole().equals(Role.GUIDE)) {
            return "redirect:/get_all_author_tour";
        } else {
            return "redirect:/get_all_tour";
        }
    }

}
