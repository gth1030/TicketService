package TicketHW.RigorousEstimate;


import TicketHW.SeatHold;
import org.junit.Test;

import java.util.ArrayList;
import java.util.PriorityQueue;
import static org.junit.Assert.*;
/**
 * Created by kitae on 2016-02-28.
 */
public class SeatManagerTest {

    @Test
    public void findBiggestGroupsTest() {
        SeatManager seatManager = new SeatManager(1,new int[]{50, 25});
        RowManagement rowManagement = seatManager.getRowManagement(1);
        rowManagement.getRowInfoArray();
        ArrayList<SeatBundle> seatbundles = seatManager.findBiggestGroups(50);
        assertEquals(1, seatbundles.size());
        for (int i = 1; i <= 25; i++) {
            rowManagement = seatManager.getRowManagement(i);
            RowManagement.reserveBundle(rowManagement, 1, 10);
        }
        seatbundles = seatManager.findBiggestGroups(50);
        assertEquals(1, seatbundles.get(0).getColumnNumber());
        assertEquals(40, seatbundles.get(0).getSizeOfBundle());
        assertEquals(2, seatbundles.size());
        for (int i = 1; i <= 25; i++) {
            rowManagement = seatManager.getRowManagement(i);
            RowManagement.reserveBundle(rowManagement, 11, 30);
        }
        seatbundles = seatManager.findBiggestGroups(50);
        assertEquals(5, seatbundles.size());
    }

    @Test
    public void convertSeatBundleToSeatHoldTest() {
        SeatManager seatManager = new SeatManager(1,new int[]{50, 25});
        RowManagement rowManagement = seatManager.getRowManagement(1);
        rowManagement.getRowInfoArray();
        ArrayList<SeatBundle> seatbundles = seatManager.findBiggestGroups(5);
        SeatHold seatHold = SeatManager.convertSeatBundleToSeatHold(seatbundles);
        assertEquals(5, seatHold.size());
        assertEquals(1, seatHold.get().get(0).seatInfo()[0]);
        assertEquals(1, seatHold.get().get(0).seatInfo()[1]);
        assertEquals(1, seatHold.get().get(0).seatInfo()[2]);
        assertEquals(2, seatHold.get().get(1).seatInfo()[0]);
    }

    @Test
    public void updateSeatInfoTest() {
        SeatManager seatManager = new SeatManager(1,new int[]{50, 25});
        RowManagement rowManagement = seatManager.getRowManagement(1);
        ArrayList<SeatBundle> seatbundles = seatManager.findBiggestGroups(10);
        seatManager.updateSeatInfo(seatbundles);
        assertEquals(11, seatManager.getRowManagement(1).getRowInfoArray().get(0).getRowNumber());
        seatbundles = seatManager.findBestSeat(10);
        seatManager.updateSeatInfo(seatbundles);
        assertEquals(21, seatManager.getRowManagement(1).getRowInfoArray().get(0).getRowNumber());

    }




}
