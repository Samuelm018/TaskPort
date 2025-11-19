package com.startup.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.startup.model.Admin;
import com.startup.model.Message;
import com.startup.model.Project;
import com.startup.repository.AdminRepository;
import com.startup.repository.MessageRepository;
import com.startup.repository.ProjectRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private AdminRepository adminRepo;

    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody Map<String, String> body, HttpSession session) {
        String gmail = body.get("gmail");
        String password = body.get("password");
        if (gmail == null || password == null) return ResponseEntity.badRequest().body("Missing fields");
        Admin a = adminRepo.findByGmail(gmail);
        if (a == null) return ResponseEntity.status(401).body("Incorrect gmail or password");
        if (!BCrypt.checkpw(password, a.getPasswordHash())) return ResponseEntity.status(401).body("Incorrect gmail or password");
        session.setAttribute("adminId", a.getId());
        session.setAttribute("adminName", a.getName());
        return ResponseEntity.ok(Map.of("name", a.getName()));
    }

    @GetMapping("/pending")
    public ResponseEntity<?> pendingProjects(HttpSession session) {
        Object aid = session.getAttribute("adminId");
        if (aid == null) return ResponseEntity.status(401).body("Unauthorized");
        List<Project> list = projectRepo.findPending();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptProject(HttpSession session, @PathVariable Long id) {
        Object aid = session.getAttribute("adminId");
        if (aid == null) return ResponseEntity.status(401).body("Unauthorized");
        Project p = projectRepo.findById(id);
        if (p == null) return ResponseEntity.badRequest().body("Unknown project");
        projectRepo.updateStatus(id, "ACCEPTED");
        return ResponseEntity.ok(Map.of("message", "Accepted"));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectProject(HttpSession session, @PathVariable Long id) {
        Object aid = session.getAttribute("adminId");
        if (aid == null) return ResponseEntity.status(401).body("Unauthorized");
        Project p = projectRepo.findById(id);
        if (p == null) return ResponseEntity.badRequest().body("Unknown project");
        projectRepo.updateStatus(id, "REJECTED");
        return ResponseEntity.ok(Map.of("message", "Rejected"));
    }

    @PostMapping("/{id}/message")
    public ResponseEntity<?> sendMessage(HttpSession session, @PathVariable Long id, @RequestBody Map<String, String> body) {
        Object aid = session.getAttribute("adminId");
        if (aid == null) return ResponseEntity.status(401).body("Unauthorized");
        Project p = projectRepo.findById(id);
        if (p == null) return ResponseEntity.badRequest().body("Unknown project");
        String text = body.get("message");
        if (text == null) return ResponseEntity.badRequest().body("Missing message");
        Message m = new Message();
        m.setProjectId(id);
        m.setClientId(p.getClientId());
        m.setMessage(text);
        messageRepo.save(m);
        return ResponseEntity.ok(Map.of("message","Message sent"));
    }

    @GetMapping("/accepted")
    public ResponseEntity<?> acceptedProjects(HttpSession session) {
        Object aid = session.getAttribute("adminId");
        if (aid == null) return ResponseEntity.status(401).body("Unauthorized");
        List<Project> list = projectRepo.findAccepted();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeProject(HttpSession session, @PathVariable Long id) {
        Object aid = session.getAttribute("adminId");
        if (aid == null) return ResponseEntity.status(401).body("Unauthorized");
        Project p = projectRepo.findById(id);
        if (p == null) return ResponseEntity.badRequest().body("Unknown project");
        projectRepo.updateStatus(id, "COMPLETED");
        return ResponseEntity.ok(Map.of("message", "Completed"));
    }
}
