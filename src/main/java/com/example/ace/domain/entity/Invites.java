package com.example.ace.domain.entity;

import javax.persistence.*;

@Entity
@Table(name = "invites")
public class Invites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invite_id")
    private Long id;

    @Column(name = "invite_teamName", nullable = false)
    private String teamName;

    @Column(name = "invite_invited_user_email", nullable = false)
    private String invitedUserEmail;

    @Column(name = "invite_sender_user_email", nullable = false, unique = true)
    private String senderUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getInvitedUserEmail() {
        return invitedUserEmail;
    }

    public void setInvitedUserEmail(String invitedUserEmail) {
        this.invitedUserEmail = invitedUserEmail;
    }

    public String getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(String senderUser) {
        this.senderUser = senderUser;
    }
}
