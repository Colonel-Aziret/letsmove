package com.letsmove.controller;

import com.letsmove.entity.Manager;
import com.letsmove.entity.Place;
import com.letsmove.service.ManagerService;
import com.letsmove.service.PlaceService;
import com.letsmove.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


@Controller
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    @Autowired
    private PlaceService placeService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/manager_register", method = RequestMethod.GET)
    public ModelAndView managerRegister() {
        ModelAndView modelAndView = new ModelAndView("managerRegistration");
        modelAndView.addObject("manager", new Manager());
        return modelAndView;
    }


    @PostMapping(value = "/manager_registration")
    public String managerRegistration(@ModelAttribute(name = "manager") Manager manager) {
        this.managerService.save(manager);
        return "redirect:/login";
    }

    @RequestMapping(value = "/managerMain", method = RequestMethod.GET)
    public String managerMain(@RequestParam(name = "selectedCity", required = false) String selectedCity, Model model) {
        // Fetch all places or filtered places based on the selectedCity
        List<Place> allActivePlace;

        if (selectedCity != null && !selectedCity.isEmpty()) {
            // Filter places by the selected city
            allActivePlace = placeService.getByCity(selectedCity);
        } else {
            // Fetch all places if no city is selected
            allActivePlace = placeService.getAllActivePlace();
        }

        model.addAttribute("allActivePlace", allActivePlace);
        return "managerMain";
    }


}
