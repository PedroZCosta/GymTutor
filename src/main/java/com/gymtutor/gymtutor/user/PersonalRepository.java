package com.gymtutor.gymtutor.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PersonalRepository extends JpaRepository<Personal, Integer> {

    Optional<Personal> findByUser(User user);

    List<Personal> findByIsApprovedFalseAndIsRejectedFalse();

}