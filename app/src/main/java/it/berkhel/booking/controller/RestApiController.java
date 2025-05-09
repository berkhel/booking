package it.berkhel.booking.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.annotation.Transactional;
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
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.entity.Purchase;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.ConcurrentPurchaseException;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventAlreadyExistsException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;
import it.berkhel.booking.dto.DtoMapper;
import it.berkhel.booking.dto.EventDto;
import it.berkhel.booking.dto.PurchaseDto;
import it.berkhel.booking.dto.PurchaseRequest;
import it.berkhel.booking.service.TransactionalBookingService;
import it.berkhel.booking.service.TransactionalEventService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@RestController
public class RestApiController {

    private final TransactionalBookingService bookingService;
    private final TransactionalEventService eventService;

    private final Logger log = LoggerFactory.getLogger(RestApiController.class);
    private final DtoMapper dtoMapper;

    public RestApiController(TransactionalBookingService bookingService, TransactionalEventService eventService, DtoMapper dtoMapper){
        this.bookingService = bookingService;
        this.eventService = eventService;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping(value = "/booking", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PurchaseDto booking(@Valid @RequestBody(required = true) PurchaseRequest purchaseRequest) throws BadPurchaseRequestException, EventNotFoundException, DuplicateTicketException, SoldoutException, ConcurrentPurchaseException {
        Purchase purchase = dtoMapper.toObject(purchaseRequest);
        bookingService.process(purchase);
        return dtoMapper.toDto(purchase);
    }



    @PostMapping(value = "/event", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Event event(@Valid @RequestBody(required = true) EventDto eventDto) throws EventAlreadyExistsException {
        return eventService.create(dtoMapper.toObject(eventDto));
    }

    @ExceptionHandler({
            BadPurchaseRequestException.class,
            SoldoutException.class,
            EventNotFoundException.class,
            EventAlreadyExistsException.class,
            DuplicateTicketException.class,
            ConcurrentPurchaseException.class })
    public ErrorResponse domainErrorRequest(Exception ex) {
        log.error("This is a 400 Domain Error " + ex.getMessage(), ex);
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage()).build();
    }

    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        HandlerMethodValidationException.class,
        MethodArgumentNotValidException.class,
        ConstraintViolationException.class,
        EntityNotFoundException.class})
    public ErrorResponse badRequest(Exception ex) {
        log.error("Not valid request " + ex.getMessage(), ex);
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, "Request not valid").build();
     }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Something gone wrong, contact the administrator") 
    @ExceptionHandler(Exception.class)
    public void internalServerError(Exception ex) {
        log.error("This is a 500 Internal Server Error", ex);
    }
    
}
