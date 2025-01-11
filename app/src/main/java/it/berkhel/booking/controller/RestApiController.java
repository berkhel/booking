package it.berkhel.booking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.dto.DtoMapper;
import it.berkhel.booking.dto.PurchaseDto;
import it.berkhel.booking.dto.TicketDto;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;

@RestController
public class RestApiController {

    private final ForBooking bookingManager;

    private final DtoMapper dtoMapper;

    public RestApiController(ForBooking bookingManager, DtoMapper dtoMapper){
        this.bookingManager = bookingManager;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping(value = "/booking", produces = "application/json")
    public PurchaseDto book(@RequestBody List<TicketDto> dtoTickets) throws Exception {
        List<Ticket> tickets = dtoTickets.stream().map(dtoMapper::toObject).toList();
        return dtoMapper.toDto(bookingManager.purchase(tickets));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Body Request not valid") // 409
    @ExceptionHandler(Exception.class)
    public void badRequest() {
        // Nothing to do
    }
    
}
