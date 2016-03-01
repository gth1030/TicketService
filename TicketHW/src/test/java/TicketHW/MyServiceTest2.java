package TicketHW;

import TicketHW.RigorousEstimate.SeatBundle;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kitae on 2016-02-29.
 */
public class MyServiceTest2 {



    @Test
    public void findLevelHoldTest() {
        MyTicketService serviceOj1 = new MyTicketService("Test1");
        SeatHold seatHold = serviceOj1.findAndHoldSeats(1220, 1, 1, "aa");
        serviceOj1.reserveSeats(1, "aa");
        seatHold = serviceOj1.findAndHoldSeats(20, 1, 2, "aa");
        serviceOj1.reserveSeats(1221, "aa");
        seatHold = serviceOj1.findAndHoldSeats(1240, 2, 2, "aa");
        serviceOj1.reserveSeats(1251, "aa");
        ArrayList<Seat> seats = seatHold.get();
        ArrayList<Integer> levels = serviceOj1.findLevelForHold(20, 1, 2);
        assertEquals(1, levels.get(0).intValue());
        assertEquals(2, levels.get(1).intValue());
    }

    @Test
    public void interLevelBookingTest() throws InterruptedException {
        MyTicketService serviceOj1 = new MyTicketService("Test1");
        SeatHold seatHold = serviceOj1.findAndHoldSeats(1240, 1, 1, "aa");
        serviceOj1.reserveSeats(1, "aa");
        seatHold = serviceOj1.findAndHoldSeats(1990, 2, 2, "aa");
        serviceOj1.reserveSeats(1251, "aa");
        ArrayList<Seat> seats = seatHold.get();
        seatHold = serviceOj1.findAndHoldSeats(20, 1, 2, "aa");
        seats = seatHold.get();
        serviceOj1.reserveSeats(1241, "aa");
        assertEquals(new Seat(1241).seatID(), seats.get(0).seatID());
        assertEquals(new Seat(3241).seatID(), seats.get(10).seatID());
    }


}
