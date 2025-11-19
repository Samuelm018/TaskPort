package com.startup.controller;

import com.startup.model.Message;
import com.startup.model.Project;
import com.startup.repository.MessageRepository;
import com.startup.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ClientController {
    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private MessageRepository messageRepo;

    @PostMapping("")
    public ResponseEntity<?> createProject(HttpSession session, @RequestBody Map<String, String> body) {
        Object cid = session.getAttribute("clientId");
        if (cid == null) return ResponseEntity.status(401).body("Unauthorized");
        Long clientId = (cid instanceof Long) ? (Long) cid : Long.parseLong(String.valueOf(cid));
        try {
            Project p = new Project();
            p.setClientId(clientId);
            p.setTitle(body.get("title"));
            p.setDescription(body.get("description"));
            p.setCostMin(Double.parseDouble(body.getOrDefault("costMin", "0")));
            p.setCostMax(Double.parseDouble(body.getOrDefault("costMax", "0")));
            String d = body.get("deadline");
            if (d != null && !d.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                p.setDeadline(sdf.parse(d));
            }
            projectRepo.save(p);
            return ResponseEntity.ok(Map.of("message", "Project submitted"));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Invalid data");
        }
    }

    @GetMapping("/my")
    public ResponseEntity<?> myProjects(HttpSession session) {
        Object cid = session.getAttribute("clientId");
        if (cid == null) return ResponseEntity.status(401).body("Unauthorized");
        Long clientId = (cid instanceof Long) ? (Long) cid : Long.parseLong(String.valueOf(cid));
        List<Project> list = projectRepo.findByClientId(clientId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<?> projectMessages(HttpSession session, @PathVariable Long id) {
        Object cid = session.getAttribute("clientId");
        if (cid == null) return ResponseEntity.status(401).body("Unauthorized");
        List<Message> messages = messageRepo.findByProjectId(id);
        if (messages.isEmpty()) return ResponseEntity.ok(Map.of("messages", List.of()));
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(HttpSession session, @PathVariable Long id) {
        Object cid = session.getAttribute("clientId");
        if (cid == null) return ResponseEntity.status(401).body("Unauthorized");
        Long clientId = (cid instanceof Long) ? (Long) cid : Long.parseLong(String.valueOf(cid));
        Project p = projectRepo.findById(id);
        if (p == null || !p.getClientId().equals(clientId)) return ResponseEntity.status(403).body("Forbidden");
        projectRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("message","Deleted"));
    }
}
