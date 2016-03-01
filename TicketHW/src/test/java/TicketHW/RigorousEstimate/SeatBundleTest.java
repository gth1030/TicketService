package TicketHW.RigorousEstimate;


import org.junit.Test;

import java.util.PriorityQueue;
import static org.junit.Assert.*;

/**
 * Created by kitae on 2016-02-27.
 */
public class SeatBundleTest {

    @Test
    public void priorityQueueTest() {
        PriorityQueue<SeatBundle> queue = new PriorityQueue<>();
        queue.add(new SeatBundle(1, 1));
        queue.add(new SeatBundle(1, 2));
        queue.add(new SeatBundle(1, 3));
        queue.add(new SeatBundle(2, 4));
        queue.add(new SeatBundle(2, 2));
        SeatBundle obj1 = new SeatBundle(2, 4);
        SeatBundle obj2 = new SeatBundle(1, 3);
        assertEquals(obj1.getSizeOfBundle(), queue.poll().getSizeOfBundle());
        assertEquals(obj2.getRowNumber(), queue.peek().getRowNumber());
        assertEquals(obj2.getSizeOfBundle(), queue.peek().getSizeOfBundle());
    }


}
