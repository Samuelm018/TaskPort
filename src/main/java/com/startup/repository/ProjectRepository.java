package com.startup.repository;

import com.startup.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class ProjectRepository {
    @Autowired
    private JdbcTemplate jdbc;

    private RowMapper<Project> mapper = new RowMapper<Project>() {
        @Override
        public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
            Project p = new Project();
            p.setId(rs.getLong("id"));
            p.setClientId(rs.getLong("client_id"));
            p.setTitle(rs.getString("title"));
            p.setDescription(rs.getString("description"));
            p.setCostMin(rs.getDouble("cost_min"));
            p.setCostMax(rs.getDouble("cost_max"));
            p.setDeadline(rs.getDate("deadline"));
            p.setStatus(rs.getString("status"));
            return p;
        }
    };

    public int save(Project p) {
        return jdbc.update("INSERT INTO projects (client_id, title, description, cost_min, cost_max, deadline, status) VALUES (?, ?, ?, ?, ?, ?, 'PENDING')",
                p.getClientId(), p.getTitle(), p.getDescription(), p.getCostMin(), p.getCostMax(), p.getDeadline());
    }

    public List<Project> findByClientId(Long clientId) {
        return jdbc.query("SELECT * FROM projects WHERE client_id = ? ORDER BY created_at DESC", mapper, clientId);
    }

    public List<Project> findPending() {
        return jdbc.query("SELECT p.*, c.username as client_name FROM projects p JOIN client c ON p.client_id = c.id WHERE p.status = 'PENDING' ORDER BY p.created_at DESC", mapper);
    }

    public List<Project> findAccepted() {
        return jdbc.query("SELECT p.*, c.username as client_name FROM projects p JOIN client c ON p.client_id = c.id WHERE p.status = 'ACCEPTED' ORDER BY p.created_at DESC", mapper);
    }

    public Project findById(Long id) {
        List<Project> list = jdbc.query("SELECT * FROM projects WHERE id = ?", mapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public int deleteById(Long id) {
        return jdbc.update("DELETE FROM projects WHERE id = ?", id);
    }

    public int updateStatus(Long id, String status) {
        return jdbc.update("UPDATE projects SET status = ? WHERE id = ?", status, id);
    }
}
