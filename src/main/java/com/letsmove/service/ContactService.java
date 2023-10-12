package com.letsmove.service;

import com.letsmove.dao.ContactRepository;
import com.letsmove.dao.GuidesRepository;
import com.letsmove.entity.Contact;
import com.letsmove.entity.Guides;
import com.letsmove.entity.Users;
import com.letsmove.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;


    public Contact save(Contact contact) {

        return contactRepository.save(contact);

    }
}
