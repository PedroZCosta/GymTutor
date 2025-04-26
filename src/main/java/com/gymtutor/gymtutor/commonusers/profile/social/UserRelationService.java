package com.gymtutor.gymtutor.commonusers.profile.social;

import com.gymtutor.gymtutor.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserRelationService {

    @Autowired
    private UserRelationRepository relationRepo;

    public List<User> getAllConnectedUsers(User loggedUser) {
        List<UserRelationModel> sent = relationRepo.findBySenderAndAcceptedTrue(loggedUser);
        List<UserRelationModel> received = relationRepo.findByReceiverAndAcceptedTrue(loggedUser);

        List<User> connections = new ArrayList<>();

        sent.forEach(r -> connections.add(r.getReceiver()));
        received.forEach(r -> connections.add(r.getSender()));

        return connections;
    }

    public List<UserRelationModel> getPendingRequests(User loggedUser) {
        return relationRepo.findByReceiverAndAcceptedFalse(loggedUser);
    }

    public Optional<UserRelationModel> findRelation(int senderId, int receiverId) {
        UserRelationKey key = new UserRelationKey(senderId, receiverId);
        return relationRepo.findById(key);
    }

    public void save(UserRelationModel relation) {
        relationRepo.save(relation);
    }

    public void delete(UserRelationModel relation) {
        relationRepo.delete(relation);
    }

    //Mapeia a relação das solicitações
    public Map<Integer, RelationStatus> mapUserRelationStatus(User loggedUser, List<User> usersToCheck) {
        Map<Integer, RelationStatus> map = new HashMap<>();

        for (User user : usersToCheck) {
            if (user.getUserId() == loggedUser.getUserId()) continue;

            var relation = relationRepo.findById(new UserRelationKey(loggedUser.getUserId(), user.getUserId()));
            if (relation.isPresent()) {
                if (relation.get().isAccepted()) {
                    map.put(user.getUserId(), RelationStatus.CONNECTED);
                    continue;
                } else {
                    map.put(user.getUserId(), RelationStatus.SENT_REQUEST);
                    continue;
                }
            }

            // Verifica se ele te mandou solicitação
            var reverse = relationRepo.findById(new UserRelationKey(user.getUserId(), loggedUser.getUserId()));
            if (reverse.isPresent()) {
                if (reverse.get().isAccepted()) {
                    map.put(user.getUserId(), RelationStatus.CONNECTED);
                } else {
                    map.put(user.getUserId(), RelationStatus.RECEIVED_REQUEST);
                }
            } else {
                map.put(user.getUserId(), RelationStatus.NONE);
            }
        }

        return map;
    }


}