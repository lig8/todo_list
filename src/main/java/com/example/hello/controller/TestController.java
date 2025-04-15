package com.example.hello.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TestController(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/api/test-db")
    public Map<String, Object> testDatabase() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 测试数据库连接
            String dbUrl = dataSource.getConnection().getMetaData().getURL();
            result.put("连接状态", "成功");
            result.put("数据库URL", dbUrl);

            // 创建表（如果不存在）
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS todo_list (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "value VARCHAR(255) NOT NULL, " +
                    "is_completed BOOLEAN NOT NULL DEFAULT FALSE, " +
                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            result.put("表创建", "成功");
            
            return result;
        } catch (SQLException e) {
            result.put("连接状态", "失败");
            result.put("错误信息", e.getMessage());
            return result;
        }
    }
} 