#!/bin/bash
# 停止已运行的Spring Boot进程
# Run Spring Boot with debug enabled

# Clean old compiled files first
./mvnw clean

# Run with debug enabled
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Ddebug=true -Dfile.encoding=UTF-8 -Dspring.output.ansi.enabled=always"
