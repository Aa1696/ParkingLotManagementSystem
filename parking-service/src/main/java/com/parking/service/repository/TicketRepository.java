package com.parking.service.repository;

import com.parking.common.enums.TicketStatus;
import com.parking.service.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByLicensePlateAndStatus(String licensePlate, TicketStatus status);
}
