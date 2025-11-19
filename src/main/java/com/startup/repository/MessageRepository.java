package com.startup.repository;

import com.startup.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MessageRepository {
    @Autowired
    private JdbcTemplate jdbc;

    private RowMapper<Message> mapper = new RowMapper<Message>() {
        @Override
        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
            Message m = new Message();
            m.setId(rs.getLong("id"));
            m.setProjectId(rs.getLong("project_id"));
            m.setClientId(rs.getLong("client_id"));
            m.setMessage(rs.getString("message"));
            return m;
        }
    };

    public int save(Message m) {
        return jdbc.update("INSERT INTO messages (project_id, client_id, message) VALUES (?, ?, ?)",
                m.getProjectId(), m.getClientId(), m.getMessage());
    }

    public List<Message> findByProjectId(Long projectId) {
        return jdbc.query("SELECT * FROM messages WHERE project_id = ? ORDER BY created_at DESC", mapper, projectId);
    }
}
