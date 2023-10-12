package com.letsmove.controller;

import com.letsmove.entity.Contact;
import com.letsmove.entity.Guides;
import com.letsmove.service.ContactService;
import com.letsmove.service.GuidesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class ContactController {
    @Autowired
    private ContactService contactService;

    @PostMapping(value = "/")
    public String guidesRegistration(@ModelAttribute(name = "contacts") Contact contact) throws IOException {
        this.contactService.save(contact);
        return "redirect:/";
    }
}
