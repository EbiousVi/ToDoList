package com.example.ace.service;

import com.example.ace.domain.entity.Invites;
import com.example.ace.domain.entity.Team;
import com.example.ace.domain.entity.User;
import com.example.ace.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;

@Service
public class TeamService {
    private final UserService userService;
    private final TeamRepository teamRepository;
    private final InvitesService invitesService;

    @Autowired
    public TeamService(UserService userService, TeamRepository teamRepository, InvitesService invitesService) {
        this.userService = userService;
        this.teamRepository = teamRepository;
        this.invitesService = invitesService;
    }

    public void createTeam(String teamName, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Team team = new Team();
        team.setTeamName(teamName);
        team.setCreator(principal.getName());
        team.setUsers(Collections.singletonList(user));
        user.setTeam(team);
        teamRepository.save(team);
    }

    public void addComrades(Principal principal) {
        Team team = teamRepository.findByTeamName(invitesService.getTeamNameByInvitedUser(principal.getName())).get();
        User user = userService.findUserByEmail(principal.getName());
        user.setTeam(team);
        userService.updateUser(user);
        Invites invites = invitesService.findInviteByInvitedUserEmailAndTeamName(user.getEmail(), team.getTeamName());
        invitesService.deleteInvite(invites);
    }
}
