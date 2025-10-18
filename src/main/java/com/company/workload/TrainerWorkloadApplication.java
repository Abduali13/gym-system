package com.company.workload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages = {"com.company.workload", "com.company.gym_system.util"})
@EnableDiscoveryClient
public class TrainerWorkloadApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TrainerWorkloadApplication.class);
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("spring.application.name", "workload-service");
        defaults.put("management.endpoints.web.exposure.include", "health,info");
        app.setDefaultProperties(defaults);
        app.run(args);
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> workloadPortCustomizer() {
        return factory -> factory.setPort(8081);
    }
}
