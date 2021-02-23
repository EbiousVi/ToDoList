package com.example.ace.domain.entity;

import javax.persistence.*;

@Entity
@Table(name = "notes")
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "notes_note1", columnDefinition = "TEXT")
    private String note1;
    @Column(name = "notes_note2", columnDefinition = "TEXT")
    private String note2;
    @Column(name = "notes_note3", columnDefinition = "TEXT")
    private String note3;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public Notes() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Notes(String note1) {
        this.note1 = note1;
    }

    public Long getId() {
        return id;
    }

    public String getNote1() {
        return note1;
    }

    public String getNote2() {
        return note2;
    }

    public String getNote3() {
        return note3;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNote1(String deadline) {
        this.note1 = deadline;
    }

    public void setNote2(String results) {
        this.note2 = results;
    }

    public void setNote3(String links) {
        this.note3 = links;
    }
}
