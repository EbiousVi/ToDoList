package com.example.ace.service;

import com.example.ace.domain.entity.Invites;
import com.example.ace.domain.entity.Team;
import com.example.ace.domain.entity.User;
import com.example.ace.repository.InvitesRepository;
import com.example.ace.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvitesService {
    private final UserService userService;
    private final InvitesRepository invitesRepository;

    @Autowired
    public InvitesService(UserService userService, InvitesRepository invitesRepository) {
        this.userService = userService;
        this.invitesRepository = invitesRepository;
    }

    public void saveInvite(String invitedUserEmail, Principal principal) {
        Invites invites = new Invites();
        invites.setInvitedUserEmail(invitedUserEmail);
        invites.setSenderUser(principal.getName());
        invites.setTeamName(userService.findUserByEmail(principal.getName()).getTeam().getTeamName());
        invitesRepository.save(invites);
    }

    public String getTeamNameByInvitedUser(String email) {
        Optional<Invites> invite = invitesRepository.findByInvitedUserEmail(email);
        if (invite.isPresent()) {
            return invite.get().getTeamName();
        } else {
         return "NO TEAM";
        }
    }

    public void deleteInvite(Invites invite) {
        invitesRepository.delete(invite);
    }

    public Invites findInviteByInvitedUserEmailAndTeamName(String invitedUserEmail, String teamName) {
        return invitesRepository.findByInvitedUserEmailAndTeamName(invitedUserEmail, teamName).get();
    }
}
