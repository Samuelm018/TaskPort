package com.startup.repository;

import com.startup.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ClientRepository {
    @Autowired
    private JdbcTemplate jdbc;

    private RowMapper<Client> mapper = new RowMapper<Client>() {
        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            Client c = new Client();
            c.setId(rs.getLong("id"));
            c.setUsername(rs.getString("username"));
            c.setGmail(rs.getString("gmail"));
            c.setPasswordHash(rs.getString("password_hash"));
            return c;
        }
    };

    public int save(Client c) {
        return jdbc.update("INSERT INTO client (username, gmail, password_hash) VALUES (?, ?, ?)",
                c.getUsername(), c.getGmail(), c.getPasswordHash());
    }

    public Client findByGmail(String gmail) {
        List<Client> list = jdbc.query("SELECT * FROM client WHERE gmail = ?", mapper, gmail);
        return list.isEmpty() ? null : list.get(0);
    }

    public Client findById(Long id) {
        List<Client> list = jdbc.query("SELECT * FROM client WHERE id = ?", mapper, id);
        return list.isEmpty() ? null : list.get(0);
    }
}
