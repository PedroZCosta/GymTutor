package com.gymtutor.gymtutor.commonusers.profile.social;

import com.gymtutor.gymtutor.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRelationRepository extends JpaRepository<UserRelationModel, UserRelationKey> {

    // Conexões onde o usuário é o remetente
    List<UserRelationModel> findBySenderAndAcceptedTrue(User sender);

    // Conexões onde o usuário é o destinatário
    List<UserRelationModel> findByReceiverAndAcceptedTrue(User receiver);

    // Conexões onde o usuário é o destinatário e a solicitação é pendente
    List<UserRelationModel> findByReceiverAndAcceptedFalse(User receiver);

}