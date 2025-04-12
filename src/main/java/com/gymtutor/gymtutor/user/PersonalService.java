package com.gymtutor.gymtutor.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonalService {

    @Autowired
    private PersonalRepository personalRepository;

    public Personal findByUser(User user) {
        Optional<Personal> optionalPersonal = personalRepository.findByUser(user);
        return optionalPersonal.orElse(null);
    }
}