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

    public void save(Personal personal) {
        personalRepository.save(personal);
    }

    public void approvePersonal(Personal personal) {
        personal.setApproved(true);
        personal.setRejected(false);
        personal.setRejectReason(null);
        personalRepository.save(personal);

    }

    public void rejectPersonal(Personal personal, String reason) {
        personal.setApproved(false);
        personal.setRejected(true);
        personal.setRejectReason(reason);
        personalRepository.save(personal);
    }

    public void delete(User user) {
        Personal personal = findByUser(user);
        if (personal != null) {
            personalRepository.delete(personal);
        }
    }

}