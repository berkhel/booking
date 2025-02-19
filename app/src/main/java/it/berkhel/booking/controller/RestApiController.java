package it.berkhel.booking.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.actionport.ForEvents;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.ConcurrentPurchaseException;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventAlreadyExistsException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;
import it.berkhel.booking.dto.DtoMapper;
import it.berkhel.booking.dto.EventDto;
import it.berkhel.booking.dto.PurchaseDto;
import it.berkhel.booking.dto.TicketDto;
import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@RestController
public class RestApiController {

    private final ForBooking bookingManager;
    private final ForEvents eventManager;

    private final Logger log = LoggerFactory.getLogger(RestApiController.class);
    private final DtoMapper dtoMapper;

    public RestApiController(ForBooking bookingManager, ForEvents eventManager, DtoMapper dtoMapper){
        this.bookingManager = bookingManager;
        this.eventManager = eventManager;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping(value = "/booking", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PurchaseDto book(@Valid @RequestBody(required = true) List<TicketDto> dtoTickets) throws Exception {
        Set<Ticket> tickets = new HashSet<>();
        for(var dtoTicket : dtoTickets){
            tickets.add(dtoMapper.toObject(dtoTicket));
        }
        Purchase purchase = bookingManager.purchase(tickets);
        return dtoMapper.toDto(purchase);
    }

    @PostMapping(value = "/event", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Event book(@Valid @RequestBody(required = true) EventDto eventDto) throws EventAlreadyExistsException {
        return eventManager.createEvent(dtoMapper.toObject(eventDto));
    }

    @ExceptionHandler({
            BadPurchaseRequestException.class,
            SoldoutException.class,
            EventNotFoundException.class,
            EventAlreadyExistsException.class,
            DuplicateTicketException.class,
            ConcurrentPurchaseException.class })
    public ErrorResponse domainErrorRequest(Exception ex) {
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage()).build();
    }

    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        HandlerMethodValidationException.class,
        MethodArgumentNotValidException.class,
        ConstraintViolationException.class,
        EntityNotFoundException.class})
    public ErrorResponse badRequest(Exception ex) {
        log.info("Not valid request " + ex.getMessage(), ex);
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, "Request not valid").build();
     }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Something gone wrong, contact the administrator") 
    @ExceptionHandler(Exception.class)
    public void internalServerError(Exception ex) {
        log.error("This is a 500 Internal Server Error", ex);
    }
    
}
