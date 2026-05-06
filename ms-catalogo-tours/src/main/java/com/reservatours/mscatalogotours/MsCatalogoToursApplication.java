package com.reservatours.mscatalogotours;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MsCatalogoToursApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsCatalogoToursApplication.class, args);
    }
}
