package com.parking.service.controller;

import com.parking.common.dto.InvoiceDTO;
import com.parking.service.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exit")
public class ExitGateController {

    private final InvoiceService invoiceService;

    public ExitGateController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/{gateId}/ticket/{ticketId}")
    public ResponseEntity<InvoiceDTO> processExit(
            @PathVariable int gateId,
            @PathVariable Long ticketId) {
        
        InvoiceDTO invoice = invoiceService.processExit(gateId, ticketId);
        return ResponseEntity.ok(invoice);
    }
}
