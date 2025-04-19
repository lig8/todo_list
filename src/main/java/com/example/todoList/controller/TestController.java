package com.example.todoList.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 提供测试数据库连接等功能
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 测试数据库连接
     * @return 连接状态和基本信息
     */
    @GetMapping("/db")
    public ResponseEntity<Map<String, Object>> testDbConnection() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 测试数据库连接
            boolean isConnected = dataSource.getConnection().isValid(5);
            response.put("connected", isConnected);
            
            // 获取数据库基本信息
            if (isConnected) {
                response.put("databaseProduct", dataSource.getConnection().getMetaData().getDatabaseProductName());
                response.put("databaseVersion", dataSource.getConnection().getMetaData().getDatabaseProductVersion());
                response.put("url", dataSource.getConnection().getMetaData().getURL());
                response.put("username", dataSource.getConnection().getMetaData().getUserName());
                
                // 获取数据库当前时间
                String dbTime = jdbcTemplate.queryForObject("SELECT NOW()", String.class);
                response.put("currentDbTime", dbTime);
                
                // 尝试获取表的数量
                Integer tableCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE()", 
                    Integer.class);
                response.put("tableCount", tableCount);
            }
            
            logger.info("数据库连接测试成功: {}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("数据库连接测试失败", e);
            response.put("connected", false);
            response.put("error", e.getMessage());
            response.put("errorType", e.getClass().getSimpleName());
            
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 