package com.letsmove.controller;

import com.letsmove.entity.Guides;
import com.letsmove.entity.Manager;
import com.letsmove.entity.Place;
import com.letsmove.service.GuidesService;
import com.letsmove.service.PlaceService;
import com.letsmove.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;


@Controller
public class GuidesController {
    @Autowired
    private GuidesService guidesService;
    @Autowired
    private UserService userService;
    @Autowired
    private PlaceService placeService;

    @RequestMapping(value = "/guides_register", method = RequestMethod.GET)
    public ModelAndView guidesRegister() {
        ModelAndView modelAndView = new ModelAndView("guidesRegistration");
        modelAndView.addObject("guides", new Guides());
        return modelAndView;
    }


    @PostMapping(value = "/guides_registration")
    public String guidesRegistration(@ModelAttribute(name = "guides") Guides guides) throws IOException {
        this.guidesService.save(guides);
        return "redirect:/login";
    }


    @RequestMapping(value = "/guideMain", method = RequestMethod.GET)
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
        return "guideMain";
    }


}
