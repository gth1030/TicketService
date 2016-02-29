package TicketHW;

import TicketHW.RigorousEstimate.SeatBundle;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kitae on 2016-02-26.
 */

public class MyServiceTest {


    @Test
    public void SeatIDInfoTest()  {
        int[] answer = new int[] {50, 2, 2};
        int[] testV1 = Seat.seatIDToInfo(1400);
        assertEquals(answer[0], testV1[0]);
        assertEquals(answer[1], testV1[1]);
        assertEquals(answer[2], testV1[2]);
    }

    @Test
    public void SeatTest () {
        Seat obj1 = new Seat(1400);
        Seat obj2 = new Seat(50, 2, 2);
        assertEquals(obj1.seatID(), obj2.seatID());
    }

    @Test
    public void NumSeatAvailableTest() {
        MyTicketService serviceOj1 = new MyTicketService("Test1");
        assertEquals(serviceOj1.numSeatsAvailable(1), 1250);
        assertEquals(serviceOj1.numSeatsAvailable(4), 1500);
    }



    @Test
    public void reserveSeatsTest() {
        MyTicketService serviceOj1 = new MyTicketService("Test1");
        SeatHold seatHold = serviceOj1.findAndHoldSeats(1, 1, 1, "aa");
        serviceOj1.reserveSeats(1, "aa");
        ArrayList<Seat> seats = seatHold.get();
        assertEquals(new Seat(1).seatID(), seats.get(0).seatID());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        assertEquals(new Seat(1).seatID(), seats.get(0).seatID());
    }

    @Test
    public void updateTicketStateTest() {
        MyTicketService serviceOj1 = new MyTicketService("Test1");
        SeatHold seatHold = new SeatHold();
        seatHold.add(new Seat(1, 1, 1));
        seatHold.add(new Seat(1, 3, 1));
        seatHold.add(new Seat(50, 2, 4));
        serviceOj1.updateTicketState(seatHold, "aa");
        assertEquals("aa", serviceOj1.getCustomerInfo().get(1));
        assertEquals(1248, serviceOj1.getAvailability()[0]);
    }

    @Test
    public void findAndHoldSeatsTest1() {
        MyTicketService serviceOj1 = new MyTicketService("Test1");
        SeatHold seatHold = serviceOj1.findAndHoldSeats(1, 1, 1, "aa");
        ArrayList<Seat> seats2 = seatHold.get();
        assertEquals(new Seat(1).seatID(), seats2.get(0).seatID());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }

        seatHold = serviceOj1.findAndHoldSeats(1, 1, 1, "aa");
        seats2 = seatHold.get();
        assertEquals(new Seat(1).seatID(), seats2.get(0).seatID());

    }

    @Test
    public void findAndHoldSeatsTest2() {
        MyTicketService serviceOj1 = new MyTicketService("Test1");
        SeatHold seatHold = serviceOj1.findAndHoldSeats(1, 1, 1, "aa");
        ArrayList<Seat> seats = seatHold.get();
        assertEquals(new Seat(1).seatID(), seats.get(0).seatID());
        assertEquals(1249, serviceOj1.getAvailability()[0]);
        seatHold = serviceOj1.findAndHoldSeats(1, 1, 1, "aa");
        seats = seatHold.get();
        assertEquals(new Seat(2).seatID(), seats.get(0).seatID());
        seatHold = serviceOj1.findAndHoldSeats(1, 2, 3, "aa");
        seats = seatHold.get();
        assertEquals(new Seat(1251).seatID(), seats.get(0).seatID());

        seatHold = serviceOj1.findAndHoldSeats(5, 3, 3, "aa");
        assertEquals(1495, serviceOj1.getAvailability()[2]);
        serviceOj1.reserveSeats(3251, "aa");
        seats = seatHold.get();
        assertEquals(new Seat(3251).seatID(), seats.get(0).seatID());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        assertEquals(new Seat(3252).seatID(), seats.get(1).seatID());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        assertEquals(new Seat(3253).seatID(), seats.get(2).seatID());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        assertEquals(new Seat(3254).seatID(), seats.get(3).seatID());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        assertEquals(new Seat(3255).seatID(), seats.get(4).seatID());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        assertEquals(1250, serviceOj1.getAvailability()[0]);
        seatHold = serviceOj1.findAndHoldSeats(1, 1, 1, "aa");
        ArrayList<Seat> seats2 = seatHold.get();
        assertEquals(1249, serviceOj1.getAvailability()[0]);
        assertEquals(new Seat(1).seatID(), seats2.get(0).seatID());
        assertEquals(new Seat(3251).seatID(), seats.get(0).seatID());
    }



    @Test
    public void findAndHoldSeatsTest3() {
        MyTicketService serviceOj1 = new MyTicketService("Test1");
        SeatHold seatHold = serviceOj1.findAndHoldSeats(1, 1, 1, "aa");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        seatHold = serviceOj1.findAndHoldSeats(1, 1, 1, "aa");
        ArrayList<Seat> seats = seatHold.get();
        assertEquals(1249, serviceOj1.getAvailability()[0]);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        seatHold = serviceOj1.findAndHoldSeats(1, 1, 1, "aa");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }


        assertEquals(1250, serviceOj1.getAvailability()[0]);
        seatHold = serviceOj1.findAndHoldSeats(1, 1, 1, "aa");
        assertEquals(1249, serviceOj1.getAvailability()[0]);
        assertEquals(1, seatHold.get().get(0).seatInfo()[0]);
        assertEquals(1, seatHold.get().get(0).seatID());
    }

    @Test
    public void removeHoldTest() {
        MyTicketService serviceOj1 = new MyTicketService("Test1");
        SeatHold seatHold = serviceOj1.findAndHoldSeats(1, 1, 1, "aa");
        serviceOj1.reserveSeats(seatHold.getSeatHoldID(), "aa");
        ArrayList<Seat> holdings = new ArrayList<>();
        holdings.add(new Seat(1, 1, 1));
        ArrayList<SeatBundle> bundleList = new ArrayList<>();
        bundleList.add(new SeatBundle(1, 1, serviceOj1.getSeatManagers().get(0).getRowManagement(1)));
        serviceOj1.removeHeldTicket(holdings, bundleList);
        assertEquals(1, serviceOj1.getSeatManagers().get(0).getRowManagement(1).getRowInfoArray().get(0).getRowNumber());
    }


}
