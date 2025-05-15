package com.gymtutor.gymtutor.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Service
public class PersonalService {

    @Autowired
    private PersonalRepository personalRepository;

    @Autowired
    private UserService userService;

    public Personal findById(int personalId) {
        return personalRepository.findById(personalId).orElse(null);
    }

    public Personal findByUser(User user) {
        Optional<Personal> optionalPersonal = personalRepository.findByUser(user);
        return optionalPersonal.orElse(null);
    }

    public void save(Personal personal) {
        personalRepository.save(personal);
    }

    public List<Personal> findAllCrefReviewRequests() {
        return personalRepository.findByIsApprovedFalseAndIsRejectedFalse();
    }

    @Transactional
    public void approvePersonal(Personal personal) {
        personal.setApproved(true);
        personal.setRejected(false);
        personal.setRejectReason(null);
        personalRepository.save(personal);

        // Altera o papel do usuário para PERSONAL
        userService.changeRole(personal.getUser(), RoleName.PERSONAL);
    }

    @Transactional
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