package com.soat.posvendaauto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PosVendaAutoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PosVendaAutoApplication.class, args);
    }

}
