package com.parking.service.controller;

import com.parking.common.dto.TicketDTO;
import com.parking.common.dto.VehicleEntryRequest;
import com.parking.service.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entry")
public class EntryGateController {

    private final TicketService ticketService;

    public EntryGateController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/{gateId}")
    public ResponseEntity<TicketDTO> processEntry(
            @PathVariable int gateId,
            @RequestBody VehicleEntryRequest request) {
        
        TicketDTO ticket = ticketService.processEntry(gateId, request);
        return new ResponseEntity<>(ticket, HttpStatus.CREATED);
    }
}
