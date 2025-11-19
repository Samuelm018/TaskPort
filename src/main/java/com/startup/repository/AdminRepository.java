package com.startup.repository;

import com.startup.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AdminRepository {
    @Autowired
    private JdbcTemplate jdbc;

    private RowMapper<Admin> mapper = new RowMapper<Admin>() {
        @Override
        public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
            Admin a = new Admin();
            a.setId(rs.getLong("id"));
            a.setName(rs.getString("name"));
            a.setGmail(rs.getString("gmail"));
            a.setPasswordHash(rs.getString("password_hash"));
            return a;
        }
    };

    public Admin findByGmail(String gmail) {
        List<Admin> list = jdbc.query("SELECT * FROM admin WHERE gmail = ?", mapper, gmail);
        return list.isEmpty() ? null : list.get(0);
    }

    public int save(Admin a) {
        return jdbc.update("INSERT INTO admin (name, gmail, password_hash) VALUES (?, ?, ?)", a.getName(), a.getGmail(), a.getPasswordHash());
    }
}
