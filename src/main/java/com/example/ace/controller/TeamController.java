package com.example.ace.controller;

import com.example.ace.domain.dto.NotesDto;
import com.example.ace.domain.entity.Notes;
import com.example.ace.domain.entity.Team;
import com.example.ace.domain.entity.User;
import com.example.ace.service.NoteService;
import com.example.ace.service.InvitesService;
import com.example.ace.service.TeamService;
import com.example.ace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class TeamController {
    private final NoteService noteService;
    private final InvitesService invitesService;
    private final TeamService teamService;
    private final UserService userService;
    private static String transferEmail;

    @Autowired
    public TeamController(NoteService noteService, InvitesService invitesService, TeamService teamService, UserService userService) {
        this.noteService = noteService;
        this.invitesService = invitesService;
        this.teamService = teamService;
        this.userService = userService;
    }

    @GetMapping("/home/team")
    public String team(Principal principal, Model model) {
        try {
            String invitations = invitesService.getTeamNameByInvitedUser(principal.getName());
            if (!(invitations == null)) {
                model.addAttribute("invitations", invitations);
            }
            String teamName = userService.findUserByEmail(principal.getName()).getTeam().getTeamName();
            if (teamName == null) {
                model.addAttribute("team", "You have no Team! Create or Join");
                model.addAttribute("block", "btn disabled");
            }
        } catch (Exception ignored) {
        }
        return "team";
    }

    @GetMapping("/home/team/join")
    public String joinIntoTeam(Principal principal) {
        teamService.addComrades(principal);
        return "team";
    }

    @GetMapping("/home/team/reg-team")
    public String registrationTeam() {
        return "reg-team";
    }

    @PostMapping("/home/team/reg-team")
    public String registrationTeamPost(@RequestParam("team_name") String teamName, Principal principal) {
        teamService.createTeam(teamName, principal);
        return "add-comrades";
    }

    @GetMapping("/home/team/find-comrades")
    public String findComradesPost() {
        return "add-comrades";
    }

    @PostMapping("/home/team/find-comrades")
    public String findComradesPost(@RequestParam("user") String email, Model model) {
        try {
            User user = userService.findUserByEmail(email);
            transferEmail = user.getEmail();
            model.addAttribute("findUser", user.getEmail());
        } catch (UsernameNotFoundException e) {
            model.addAttribute("exception", "User not found!");
            model.addAttribute("type", "danger");
        }
        return "add-comrades";
    }

    @GetMapping("/home/team/invite")
    public String inviteIntoTeam(Principal principal) {
        invitesService.saveInvite(transferEmail, principal);
        return "add-comrades";
    }

    @GetMapping("/home/team/your-team")
    public String yourTeam(Principal principal, Model model) {
        User user = userService.findUserByEmail(principal.getName());
        Team team = user.getTeam();
        model.addAttribute("comrades", team.getUsers());
        return "your-team";
    }

    @GetMapping("/home/team/your-team/{comradeEmail}")
    public String comradeNotes(@PathVariable String comradeEmail, Model model) {
        User user = userService.findUserByEmail(comradeEmail);
        List<Notes> personalNotes = noteService.findAllForCurrentUserEmail(user.getEmail());
        model.addAttribute("notes", personalNotes);
        model.addAttribute("notesDto", new NotesDto());
        model.addAttribute("comradeEmail", comradeEmail);
        return "notes/notes";
    }
}
