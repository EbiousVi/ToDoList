package com.example.ace.repository;


import com.example.ace.domain.entity.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Notes, Long> {
    List<Notes> findAllByUserEmail(String email);
}
