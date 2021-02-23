package com.example.ace.repository;

import com.example.ace.domain.entity.Invites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitesRepository extends JpaRepository<Invites, Long> {
    Optional<Invites> findByInvitedUserEmail(String invitedUserEmail);
    Optional<Invites> findByInvitedUserEmailAndTeamName(String invitedUserEmail, String teamName);
}
