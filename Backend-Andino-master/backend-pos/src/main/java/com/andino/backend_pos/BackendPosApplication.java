package com.andino.backend_pos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class BackendPosApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendPosApplication.class, args);
	}

}
