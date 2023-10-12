package com.letsmove.dao;

import com.letsmove.entity.Contact;
import com.letsmove.entity.Guides;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
}
