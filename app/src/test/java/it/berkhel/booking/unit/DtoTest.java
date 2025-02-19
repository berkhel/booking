package it.berkhel.booking.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.dto.AttendeeDto;
import it.berkhel.booking.dto.DtoMapper;
import it.berkhel.booking.dto.TicketDto;
import it.berkhel.booking.entity.Attendee;
import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Ticket;
import it.berkhel.booking.repository.EventRepository;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DtoTest {

    @Test 
    void ticket_dto_to_object(@Mock EventRepository fakeEventRepo) throws EventNotFoundException{
        when(fakeEventRepo.findById(anyString()))
            .thenAnswer(method ->  Optional.of(new Event(method.getArgument(0), 0, 0)));
        DtoMapper dto2Object = new DtoMapper(fakeEventRepo);
        TicketDto ticketDto = new TicketDto(new AttendeeDto(), "0001");

        Ticket ticket = dto2Object.toObject(ticketDto);

        assertThat(ticket.getEvent().getId(), equalTo("0001"));

    }
    
}
