package TicketHW;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by kitae on 2016-02-27.
 */
public class TimeThreadTest {

    @Test
    public void runTest() {
        MyTicketService serviceOj1 = new MyTicketService("Test1");
        SeatHold seatHold = serviceOj1.findAndHoldSeats(3, 1, 2, "aa");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        seatHold = new SeatHold();
        seatHold.add(new Seat(1, 1, 1));
        seatHold.add(new Seat(1, 3, 1));
        seatHold.add(new Seat(50, 2, 4));
        serviceOj1.updateTicketState(seatHold, "aa");
        assertEquals("aa", serviceOj1.getCustomerInfo().get(1));
        assertEquals(1248, serviceOj1.getAvailability()[0]);
    }

    @Test
    public void multiThreadTest() {
        MyTicketService serviceOj1 = new MyTicketService("Test1");
        serviceOj1.findAndHoldSeats(3, 1, 2, "aa");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        serviceOj1.findAndHoldSeats(3, 1, 2, "aa");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        assertEquals(1250, serviceOj1.getAvailability()[0]);


    }

}
