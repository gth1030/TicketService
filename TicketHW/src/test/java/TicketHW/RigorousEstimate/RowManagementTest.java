package TicketHW.RigorousEstimate;


import TicketHW.Seat;
import com.sun.rowset.internal.Row;
import org.junit.Test;

import java.util.PriorityQueue;
import static org.junit.Assert.*;
/**
 * Created by kitae on 2016-02-28.
 */
public class RowManagementTest {

    @Test
    public void reserveBundleTest() {
        SeatManager seatManager = new SeatManager(1,new int[]{50, 25});
        RowManagement rowManagement = seatManager.getRowManagement(1);
        RowManagement.reserveBundle(rowManagement, 1, 25);
        SeatBundle seatBundle = new SeatBundle(1, 25, rowManagement);
        assertEquals(seatBundle.getSizeOfBundle(), rowManagement.getRowInfoArray().get(0).getSizeOfBundle());
        assertEquals(26, rowManagement.getRowInfoArray().get(0).getRowNumber());
        RowManagement.reserveBundle(rowManagement, 26, 24);
        assertEquals(1, rowManagement.getRowInfoArray().get(0).getSizeOfBundle());
        assertEquals(seatBundle.getLevel(), rowManagement.getRowInfoArray().get(0).getLevel());
        assertEquals(50, rowManagement.getRowInfoArray().get(0).getRowNumber());

    }


    @Test
    public void findSIngleRowAvailabilityTest() {
        SeatManager seatManager = new SeatManager(1,new int[]{50, 25});
        RowManagement rowManagement = new RowManagement(seatManager, 1);
        SeatBundle seatBundle = rowManagement.getLargestBundle();
        assertEquals(50, seatBundle.getSizeOfBundle());

    }

    @Test
    public void getLargestBundleTest() {
        SeatManager seatManager = new SeatManager(1,new int[]{50, 25});
        RowManagement rowManagement = seatManager.getRowManagement(1);
        assertEquals(50, rowManagement.getLargestBundle().getSizeOfBundle());
        RowManagement.reserveBundle(rowManagement, 1, 25);
        assertEquals(25, rowManagement.getLargestBundle().getSizeOfBundle());
        RowManagement.reserveBundle(rowManagement, 26, 10);
        assertEquals(15, rowManagement.getLargestBundle().getSizeOfBundle());
    }

    @Test
    public void rearrangeTest() {
        SeatManager seatManager = new SeatManager(1,new int[]{50, 25});
        RowManagement rowManagement = seatManager.getRowManagement(1);
        RowManagement.reserveBundle(rowManagement, 1, 2);
        rowManagement.add(new Seat(1, 1, 1));
        rowManagement.add(new Seat(2, 1, 1));
        rowManagement.rearrage();
        assertEquals(1, rowManagement.getRowInfoArray().size());
        RowManagement.reserveBundle(rowManagement, 1, 2);
        rowManagement.add(new Seat(1, 1, 1));
        rowManagement.rearrage();
        assertEquals(2, rowManagement.getRowInfoArray().size());
        rowManagement.add(new Seat(2, 1, 1));
        rowManagement.rearrage();
        RowManagement.reserveBundle(rowManagement, 1, 49);
        rowManagement.add(new Seat(49, 1, 1));
        rowManagement.rearrage();
        assertEquals(1, rowManagement.getRowInfoArray().size());

    }






}
