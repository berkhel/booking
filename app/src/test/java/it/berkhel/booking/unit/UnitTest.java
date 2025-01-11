package it.berkhel.booking.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.berkhel.booking.app.App;
import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.dto.DtoMapper;
import it.berkhel.booking.dto.TicketDto;
import it.berkhel.booking.entity.Attendee;
import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;
import it.berkhel.booking.repository.EventRepository;
import it.berkhel.booking.unit.fixture.InMemoryStorage;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class)
class UnitTest {

    @Test void booking_targets_the_storage(@Mock ForStorage theStorage, @Mock Ticket aTicket) throws Exception {
        ForBooking app = App.init(theStorage);

        Purchase aPurchase = app.purchase(List.of(aTicket));

        verify(theStorage).save(aPurchase);
    }

    @Test void booking_always_return_a_response(@Mock ForStorage aStorage, @Mock Ticket aTicket) throws Exception {
        ForBooking app = App.init(aStorage);

        Purchase thePurchase = app.purchase(List.of(aTicket));

        assertThat(thePurchase, is(notNullValue()));
        assertThat(thePurchase.getId(), hasLength(greaterThan(0)) );
    }

    @Test
    void not_more_than_three_tickets_for_purchase(@Mock ForStorage aStorage){
        List<Ticket> fourTickets = Stream.generate(Ticket::new).limit(4).toList();
        ForBooking app = App.init(aStorage);

        assertThrows(Exception.class, () -> {
            app.purchase(fourTickets);
        });
        

    }

    @Test
    void three_tickets_for_purchase_are_allowed(@Mock ForStorage aStorage){
        List<Ticket> threeTickets = Stream.generate(Ticket::new).limit(3).toList();
        ForBooking app = App.init(aStorage);

        assertDoesNotThrow(() -> {
            app.purchase(threeTickets);
        });

    }

    @Test
    void zero_tickets_for_purchase_are_not_allowed(@Mock ForStorage aStorage){
        List<Ticket> noTickets = List.of();
        ForBooking app = App.init(aStorage);

        assertThrows(Exception.class, () -> {
            app.purchase(noTickets);
        });

    }

    @Test
    void a_null_list_of_tickets_for_purchase_is_not_allowed(@Mock ForStorage aStorage){
        List<Ticket> noTickets = null;
        ForBooking app = App.init(aStorage);

        assertThrows(Exception.class, () -> {
            app.purchase(noTickets);
        });

    }

    @Test 
    void ticket_dto_to_object(@Mock EventRepository fakeEventRepo){
        when(fakeEventRepo.getReferenceById(anyString()))
            .thenAnswer(method ->  new Event(method.getArgument(0), 0, 0));
        DtoMapper dto2Object = new DtoMapper(fakeEventRepo);
        TicketDto ticketDto = new TicketDto(new Attendee(), "0001");

        Ticket ticket = dto2Object.toObject(ticketDto);

        assertThat(ticket.getEvent().getId(), equalTo("0001"));

    }

}
