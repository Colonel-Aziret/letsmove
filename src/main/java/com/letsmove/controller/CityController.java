package com.letsmove.controller;

import com.letsmove.entity.City;
import com.letsmove.entity.Users;
import com.letsmove.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class CityController {
    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/add_city", method = RequestMethod.GET)
    public ModelAndView addCity() {
        ModelAndView modelAndView = new ModelAndView("addCity");
        modelAndView.addObject("city", new City());
        return modelAndView;
    }

    @RequestMapping(value = "/city_information", method = RequestMethod.GET)
    public ModelAndView infoCity() {
        ModelAndView modelAndView = new ModelAndView("main");
        City city = cityService.findByName("Бишкек");
        modelAndView.addObject("city", city);
        return modelAndView;
    }


    @PostMapping(value = "/save_city")
    public String saveCity(@ModelAttribute(name = "city") City city) {
        this.cityService.save(city);
        return "rev";

    }
}
