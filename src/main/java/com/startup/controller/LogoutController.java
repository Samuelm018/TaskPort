package com.startup.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {
    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        try {
            session.invalidate();
        } catch (Exception ignored) {}
        return ResponseEntity.ok().build();
    }
}
