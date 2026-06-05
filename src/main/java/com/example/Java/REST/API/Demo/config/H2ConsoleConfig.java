package com.example.Java.REST.API.Demo.config;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2ConsoleConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2WebConsoleServer() throws SQLException {
        return Server.createWebServer("-web", "-webAllowOthers", "-webPort", "9098");
    }
}
