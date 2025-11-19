package com.startup.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.startup.model.Client;
import com.startup.repository.ClientRepository;

@RestController
@RequestMapping("/api/client")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private ClientRepository clientRepo;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body, HttpSession session) {
        String username = body.get("username");
        String gmail = body.get("gmail");
        String password = body.get("password");
        if (username == null || gmail == null || password == null) return ResponseEntity.badRequest().body("Missing fields");
        try {
            if (clientRepo.findByGmail(gmail) != null) return ResponseEntity.badRequest().body("Gmail already registered");
            
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
            Client c = new Client();
            c.setUsername(username);
            c.setGmail(gmail);
            c.setPasswordHash(hashed);
            clientRepo.save(c);
            
            // fetch saved client to get id and set session
            Client saved = clientRepo.findByGmail(gmail);
            session.setAttribute("clientId", saved.getId());
            session.setAttribute("username", saved.getUsername());
            
            Map<String, Object> res = new HashMap<>();
            res.put("username", saved.getUsername());
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            log.error("Error during signup", ex);
            return ResponseEntity.status(500).body("Signup failed: " + ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpSession session) {
        String gmail = body.get("gmail");
        String password = body.get("password");
        if (gmail == null || password == null) return ResponseEntity.badRequest().body("Missing fields");
        try {
            Client c = clientRepo.findByGmail(gmail);
            if (c == null) return ResponseEntity.status(401).body("Incorrect gmail or password");
            if (!BCrypt.checkpw(password, c.getPasswordHash())) return ResponseEntity.status(401).body("Incorrect gmail or password");
            session.setAttribute("clientId", c.getId());
            session.setAttribute("username", c.getUsername());
            Map<String, Object> res = new HashMap<>();
            res.put("username", c.getUsername());
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            log.error("Error during client login", ex);
            return ResponseEntity.status(500).body("Login failed: " + ex.getMessage());
        }
    }
}
