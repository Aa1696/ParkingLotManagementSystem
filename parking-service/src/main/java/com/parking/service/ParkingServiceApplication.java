package com.parking.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Parking Service — core domain service managing parking lots, floors,
 * spots, vehicle entry/exit, tickets, invoices, and pricing.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ParkingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkingServiceApplication.class, args);
    }
}
