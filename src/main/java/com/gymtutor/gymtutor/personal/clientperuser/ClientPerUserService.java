package com.gymtutor.gymtutor.personal.clientperuser;

import com.gymtutor.gymtutor.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientPerUserService {

    @Autowired
    private ClientPerUserRepository clientPerUserRepository;

    @Autowired
    private UserRepository userRepository;

    // Vincular cliente a personal
    @Transactional
    public void linkClientToPersonal(int personalId, int clientId){
        var personal = userRepository.findById(personalId)
                .orElseThrow(() -> new IllegalArgumentException("Personal não encontrado"));

        var client = userRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        var id = new ClientPerUserRelationKey();
        id.setPersonalId(personalId);
        id.setClientId(clientId);

        Optional<ClientPerUserModel> existingLink = clientPerUserRepository.findById(id);
        if (existingLink.isPresent()) {
            throw new IllegalStateException("Este cliente já está vinculado a este personal.");
        }

        ClientPerUserModel link = new ClientPerUserModel();
        link.setId(id);
        link.setPersonal(personal);
        link.setClient(client);

        clientPerUserRepository.save(link);

    }

    // Desvincular cliente a personal
    @Transactional
    public void unlinkClientToPersonal(int personalId, int clientId){
        Optional<ClientPerUserModel> existingLink = clientPerUserRepository.findByPersonalUserIdAndClientUserId(personalId, clientId);

        if(existingLink.isEmpty()){
            throw new IllegalStateException("Este cliente não está vinculado à você.");
        }

        clientPerUserRepository.delete(existingLink.get());
    }

    public List<ClientPerUserModel> findByPersonalId(int personalId) {
        return clientPerUserRepository.findByPersonalUserId(personalId);
    }

}
