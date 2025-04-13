package it.berkhel.booking.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import it.berkhel.booking.app.entity.TicketEntry;
import it.berkhel.booking.app.exception.EventNotFoundException;

@ExtendWith(MockitoExtension.class)
public class DtoTest {

    @Test 
    void ticket_dto_to_object() throws EventNotFoundException{
        DtoMapper dto2Object = new DtoMapper();
        TicketDto ticketDto = new TicketDto(new AttendeeDto(), "0001");

        TicketEntry ticket = dto2Object.toObject(ticketDto);

        assertThat(ticket.getEventId(), equalTo("0001"));

    }
    
}
