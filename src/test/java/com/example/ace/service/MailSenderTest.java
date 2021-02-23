package com.example.ace.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailSenderTest {
    @Autowired
    private MailSender mailSender;

    @Test
    void send() {
        mailSender.send("kapral026@gmail.com", "CODE", "HELLO FROM");
    }
}