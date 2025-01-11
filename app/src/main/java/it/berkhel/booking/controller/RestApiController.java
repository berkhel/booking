package it.berkhel.booking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.SoldoutException;
import it.berkhel.booking.dto.DtoMapper;
import it.berkhel.booking.dto.PurchaseDto;
import it.berkhel.booking.dto.TicketDto;
import it.berkhel.booking.entity.Ticket;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@RestController
public class RestApiController {

    private final ForBooking bookingManager;

    private final DtoMapper dtoMapper;

    public RestApiController(ForBooking bookingManager, DtoMapper dtoMapper){
        this.bookingManager = bookingManager;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping(value = "/booking", produces = "application/json")
    public PurchaseDto book(@Valid @RequestBody(required = true) List<TicketDto> dtoTickets) throws Exception {
        List<Ticket> tickets = dtoTickets.stream().map(dtoMapper::toObject).toList();
        return dtoMapper.toDto(bookingManager.purchase(tickets));
    }

    @ExceptionHandler({
        BadPurchaseRequestException.class,
        SoldoutException.class})
    public ErrorResponse domainErrorRequest(Exception ex) {
        return ErrorResponse.builder(ex,HttpStatus.BAD_REQUEST,ex.getMessage()).build();
     }

    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        HandlerMethodValidationException.class,
        ConstraintViolationException.class,
        EntityNotFoundException.class})
    public ErrorResponse badRequest(Exception ex) {
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, "Request not valid").build();
     }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Something gone wrong, contact the administrator") 
    @ExceptionHandler(Exception.class)
    public void internalServerError(Exception ex) {
        System.out.println("Exception class: " + ex.getClass().getName());
        System.out.println("Exception message: " + ex.getMessage());
     }
    
}
