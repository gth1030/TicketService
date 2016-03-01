package TicketHW.RigorousEstimate;

import TicketHW.MyTicketService;
import TicketHW.Seat;
import TicketHW.SeatHold;
import TicketHW.TimeThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Created by kitae on 2016-02-27.
 * Seatmanager object controls all seats in one level and find best available conbination of seats for reservation. This
 * contains rowmanagement object for each row.
 */
public class SeatManager {

    /**
     * Constructor for seatmanager. Takes in level of this object and two dimensional size of the level.
     * @param level level of these seats.
     * @param size Two dimensional size of the seats in this level. Format is {row size, column size}.
     */
    public SeatManager(int level, int[] size, MyTicketService parentTicketService) {
        levelN = level;
        dimension = size;
        seatAdjacency = new HashMap<> ();
        Collections.synchronizedMap(seatAdjacency);
        for (int i = 0; i < size[1]; i ++) {
            seatAdjacency.put(i, new RowManagement(this, i + 1));
        }
        ticketService = parentTicketService;
    }

    /* The Constructor is for testing purpose, do not use */
    public SeatManager(int level, int[] size) {
        this(level, size, null);
    }



    /**
     * The function returns best available seats in this level in form of arraylist of seatbundle. The mechanism is
     * written on the ReadMe.
     * @param size number of seats required for the reservation.
     * @return best available seats for the reservation in this level.
     */
    public ArrayList<SeatBundle> findBestSeat(int size) {
        if (findOneBigSeats(size) != null) {
            ArrayList<SeatBundle> arraylist = new ArrayList<>();
            arraylist.add(findOneBigSeats(size));
            return arraylist;
        }
        return findBiggestGroups(size);
    }

    /**
     * Returns seats that are connected and as big as size. If the level doesn't have connected seats as big as size,
     * return null.
     * @param size size of seats requested for reservation.
     * @return seatbundle object that contains information of connected seats on this level. Null is there is none.
     */
    SeatBundle findOneBigSeats(int size) {
        for (int i = 0; i < seatAdjacency.size(); i++) {
            if (seatAdjacency.get(i).getLargestBundle().getSizeOfBundle() >= size) {
                return new SeatBundle(seatAdjacency.get(i).getLargestBundle().getRowNumber(), size, seatAdjacency.get(i));
            }
        }
        return null;
    }

    /**
     * Finds seats for reservation of size 'size' with smallest number of separation between seats.
     * @param size size of seats requested for reservation.
     * @return arraylist of seatbundles that contains information of seats that are least separated.
     */
    public ArrayList<SeatBundle> findBiggestGroups(int size) {
        ArrayList<SeatBundle> savedBundles = new ArrayList<>();
        PriorityQueue<SeatBundle> queue = new PriorityQueue<>();
        int maxSeat = 0;
        for (int i = 0; i < seatAdjacency.size(); i++) {
            for (int j = 0; j < seatAdjacency.get(i).getRowInfoArray().size(); j++) {
                queue.add(seatAdjacency.get(i).getRowInfoArray().get(j));
                maxSeat += seatAdjacency.get(i).getRowInfoArray().get(j).getSizeOfBundle();
            }
        }
        if (maxSeat >= size) {
            int addedSeats = 0;
            SeatBundle seatBundle = null;
            while(addedSeats < size) {
                seatBundle = queue.poll();
                if (seatBundle.getSizeOfBundle() > size - addedSeats) {
                    savedBundles.add( new SeatBundle(seatBundle.getRowNumber(), size - addedSeats, seatBundle.getRowManage()));
                    addedSeats += size - addedSeats;
                } else {
                    addedSeats += seatBundle.getSizeOfBundle();
                    savedBundles.add(seatBundle);
                }
            }
        }
        return savedBundles;
    }

    /**
     * The function converts seatbundle object to seathold object for communication among classes.
     * @param seatBundles Seatbundle object to be converted to seathold object.
     * @return Converted seathold object.
     */
    public static SeatHold convertSeatBundleToSeatHold(ArrayList<SeatBundle> seatBundles) {
        SeatHold seatHold = new SeatHold();
        SeatBundle seatBundle = null;
        for (int i = 0; i < seatBundles.size(); i ++) {
            seatBundle = seatBundles.get(i);
            for (int j = 0; j < seatBundle.getSizeOfBundle(); j++) {
                seatHold.add(new Seat(seatBundle.getRowNumber() + j, seatBundle.getColumnNumber(), seatBundle.getLevel()));
            }
        }
        return seatHold;
    }

    /**
     * The function removed requested seats from availability to stop any duplicate reservation.
     * @param seatBundles Seats to be removed from availability.
     */
    public void updateSeatInfo(ArrayList<SeatBundle> seatBundles) {
        for (SeatBundle seatBundle : seatBundles) {
            RowManagement.reserveBundle(seatAdjacency.get(seatBundle.getColumnNumber() - 1),
                    seatBundle.getRowNumber(), seatBundle.getSizeOfBundle());
        }
    }

    /**
     * When held seats are returned to the rowmanagement after expiration of holding time, reaarange needs to be called
     * to combine pieces of seatbundle objects into one if they are adjecent to each other
     */
    public void rearrange() {
        for (int i = 0; i <seatAdjacency.size(); i++) {
            seatAdjacency.get(i).rearrage();
        }
    }

    /**
     * RemoveHold function removes held tickets back to availability and updates all information accordingly.
     * @param seatList Contains information of seats to be removed as an individual collection.
     * @param bundleList Contains information of seats to be removed in bundle form
     * @param  myTicketService  myTicketserve under operation.
     */
    public static synchronized void removeHeldTicket(ArrayList<Seat> seatList, ArrayList<SeatBundle> bundleList,
                                              MyTicketService myTicketService) {
        for (int j = 0; j < seatList.size(); j++) {
            Seat tempSeat = seatList.get(j);
            myTicketService.getCustomerInfo().remove(tempSeat.seatID());
            myTicketService.getAvailability() [tempSeat.seatInfo()[2] - 1]++;
            myTicketService.getSeatManagers().get(tempSeat.seatInfo() [2]).getRowManagement(tempSeat.seatInfo()[1]).add(tempSeat);
        }
        for (int i = 0; i < bundleList.size(); i++) {
            myTicketService.getSeatManagers().get(bundleList.get(i).getLevel() - 1).getRowManagement(bundleList.
                    get(i).getColumnNumber()).add(bundleList.get(i));
        }
        myTicketService.rearrange();
        myTicketService.getThreadMap().remove(seatList.get(0).seatID());
    }

    /**
     * Find and hold the best available seats for a customer. Throw illegal argument exception if none of the level has
     * enough spaces in a single level.
     *
     * @param numSeats the number of seats to find and hold
     * @param minLevel the minimum venue level
     * @param maxLevel the maximum venue level
     * @param customerEmail unique identifier for the customer
     * @param myTicketService Myticketservice to hold seats.
     * @return a SeatHold object identifying the specific seats and related
    information
     */
    public static SeatHold findAndHoldSeats(int numSeats, int minLevel,
                                     int maxLevel, String customerEmail, MyTicketService myTicketService) {
        ArrayList<Integer> levelToIterate = myTicketService.findLevelForHold(numSeats, minLevel, maxLevel);
        ArrayList<SeatBundle> returnedSeatBundles = new ArrayList<>();
        int seatsReservedSoFar = 0;
        for (int i : levelToIterate) {
            ArrayList<SeatBundle> seatBundles = null;
            if (i == levelToIterate.get(levelToIterate.size() - 1)) {
                seatBundles = myTicketService.getSeatManagers().get(i - 1).findBestSeat(numSeats - seatsReservedSoFar);
            } else {
                seatBundles = myTicketService.getSeatManagers().get(i - 1).findBestSeat(myTicketService.getAvailability() [i]);
                seatsReservedSoFar += myTicketService.getAvailability() [i];
            }
            returnedSeatBundles.addAll(seatBundles);
            SeatHold availSeatHold = SeatManager.convertSeatBundleToSeatHold(seatBundles);
            myTicketService.getSeatManagers().get(i - 1).updateSeatInfo(seatBundles);
            updateTicketState(availSeatHold, customerEmail, myTicketService);
        }
        SeatHold availSeatHold = SeatManager.convertSeatBundleToSeatHold(returnedSeatBundles);
        TimeThread timer = new TimeThread(availSeatHold, returnedSeatBundles, myTicketService);
        myTicketService.getThreadMap().put(availSeatHold.get().get(0).seatID(), timer);
        timer.start();
        return availSeatHold;
    }

    /**
     * Update the parameters in the MyTicketservice object when the ticket is held by findAndHoldSeats.
     * @param seatHold Seats that are newly held by request
     * @param customerEmail customer e-mail.
     * @param  myTicketService myTicketService under operation
     */
    public static void updateTicketState(SeatHold seatHold, String customerEmail, MyTicketService myTicketService) {
        ArrayList<Seat> seatSet = seatHold.get();
        seatHold.setSeatHoldID(seatSet.get(0).seatID());
        for (int j = 0; j < seatSet.size(); j++) {
            Seat tempSeat = seatSet.get(j);
            myTicketService.getCustomerInfo().put(tempSeat.seatID(), customerEmail);
            myTicketService.getAvailability() [tempSeat.seatInfo()[2] - 1]--;
        }
    }

    /**
     * Returns rowmanagement object for each row.
     * @param columnNumber column number of requested row.
     * @return Rowmanagement for requested column number.
     */
    public RowManagement getRowManagement(int columnNumber) { return seatAdjacency.get(columnNumber - 1); }

    /* Returns dimensional information of this level. */
    int[] getDimension() { return dimension; }

    /* Returns level of these seats. */
    int getLevelNumber() { return levelN; }

    /* Contains Rowmanagement objects for each row. */
    HashMap<Integer, RowManagement> seatAdjacency;

    /* level of these seats. */
    int levelN;

    /* Dimension of this level. Format is {number of rows, number of columns}. */
    int[] dimension;

    /* Pointer to parent */
    MyTicketService ticketService;
}
