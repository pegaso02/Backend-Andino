package com.andino.backend_pos;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

    @Component
    public class EnvPrinter {

        @PostConstruct
        public void printEnvVars() {
            System.out.println("🔍 Verificando variables de entorno:");
            System.out.println("DB_URL: " + System.getenv("DB_URL"));
            System.out.println("DB_USERNAME: " + System.getenv("DB_USERNAME"));
            System.out.println("DB_PASSWORD: " + System.getenv("DB_PASSWORD"));
            System.out.println("SPRING_SECURITY_USER: " + System.getenv("SPRING_SECURITY_USER"));
            System.out.println("SPRING_SECURITY_PASSWORD: " + System.getenv("SPRING_SECURITY_PASSWORD"));
        }
    }
