/**
 * Created by kitae on 2016-02-26.
 * Main frame for ticket reservation system.
 */

package TicketHW;

import TicketHW.RigorousEstimate.SeatBundle;
import TicketHW.RigorousEstimate.SeatManager;

import java.util.*;

public class MyTicketService implements TicketService{

    /**
     * Constructor of MyService
     * @param name a name of the event that can be used to identify each service.
     */
    public MyTicketService(String name) {
        eventName = name;
        threadMap = new HashMap<Integer, Thread>();
        Collections.synchronizedMap(threadMap);
        customerInfo = new HashMap<Integer, String>();
        Collections.synchronizedMap(customerInfo);
        availability = new int[] {ORCHSIZE[0] * ORCHSIZE[1], MAINSIZE[0] * MAINSIZE[1], BAL1SIZE[0] * BAL1SIZE[1], BAL2SIZE[0] * BAL2SIZE[1]};
        seatManagers = new ArrayList<>();
        Collections.synchronizedList(seatManagers);
        for (int i = 0; i < 4; i++) {
            seatManagers.add(i, new SeatManager(i + 1, getSizeLC(i + 1)));
        }
    }

    /**
     * Find and hold the best available seats for a customer
     *
     * @param numSeats the number of seats to find and hold
     * @param minLevel the minimum venue level
     * @param maxLevel the maximum venue level
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific seats and related
    information
     */
    public SeatHold findAndHoldSeats(int numSeats, int minLevel,
                                     int maxLevel, String customerEmail) {
        for (int i = minLevel; i <= maxLevel; i++) {
            if (numSeatsAvailable(i) >= numSeats) {
                ArrayList<SeatBundle> seatBundles = seatManagers.get(i - 1).findBestSeat(numSeats);
                SeatHold availSeatHold = SeatManager.convertSeatBundleToSeatHold(seatBundles);
                seatManagers.get(i - 1).updateSeatInfo(seatBundles);
                updateTicketState(availSeatHold, customerEmail);
                Thread timer = new timeThread(availSeatHold, seatBundles, this);
                threadMap.put(availSeatHold.get().get(0).seatID(), timer);
                timer.start();
                return availSeatHold;
            }
        }
        throw new IllegalArgumentException("None of the requested levels contains enough seats.");
    }

    /**
     * The number of seats in the requested level that are neither held nor reserved
     * @param venueLevel a numeric venue level identifier to limit the search
     * @return the number of tickets available on the provided level
     */
    public int numSeatsAvailable(int venueLevel) {
        return availability[venueLevel - 1];
    }


    /**
     * Update the parameters in the MyTicketservice object when the ticket is held by findAndHoldSeats.
     * @param seatHold Seats that are newly held by request
     * @param customerEmail customer e-mail.
     */
    void updateTicketState(SeatHold seatHold, String customerEmail) {
        ArrayList<Seat> seatSet = seatHold.get();
        seatHold.setSeatHoldID(seatSet.get(0).seatID());
        for (int j = 0; j < seatSet.size(); j++) {
            Seat tempSeat = seatSet.get(j);
            customerInfo.put(tempSeat.seatID(), customerEmail);
            availability[tempSeat.seatInfo()[2] - 1]--;
        }
    }

    /**
     * Commit seats held for a specific customer
     * @param seatHoldId the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold
    is assigned
     * @return a reservation confirmation code
     */
    public String reserveSeats(int seatHoldId, String customerEmail) {
        threadMap.get(seatHoldId).interrupt();
        threadMap.remove((seatHoldId));
        return Integer.toString(seatHoldId);
    }

    /**
     * When holded seats are returned to the rowmanagement after expiration of holding time, reaarange needs to be called
     * to combine pieces of seatbundle objects into one if they are adjecent to each other.
     */
    public void rearrange() {
        for (int i = 0; i < seatManagers.size(); i ++) {
            seatManagers.get(i).rearrange();
        }
    }

    /**
     * RemoveHold function removes held tickets back to availability and updates all information accordingly.
     * @param seatList
     * @param bundleList
     */
    public void removeHeldTicket(ArrayList<Seat> seatList, ArrayList<SeatBundle> bundleList) {
        for (int j = 0; j < seatList.size(); j++) {
            Seat tempSeat = seatList.get(j);
            getCustomerInfo().remove(tempSeat.seatID());
            getAvailability() [tempSeat.seatInfo()[2] - 1]++;
            getSeatManagers().get(tempSeat.seatInfo() [2]).getRowManagement(tempSeat.seatInfo()[1]).add(tempSeat);
        }
        for (int i = 0; i < bundleList.size(); i++) {
            getSeatManagers().get(bundleList.get(i).getLevel() - 1).getRowManagement(bundleList.
                    get(i).getColumnNumber()).add(bundleList.get(i));
        }
        rearrange();
        getThreadMap().remove(seatList.get(0).seatID());
    }


    /**
     * Returns size of each level in int array based on the level.
     * @param level level of the seats under consideration
     * @return returns int[# of rows, # of columns] for each level
     */
    public static int[] getSizeLC(int level) {
        switch(level) {
            case 1: { return ORCHSIZE; }
            case 2: { return MAINSIZE; }
            case 3: { return BAL1SIZE; }
            case 4: { return BAL2SIZE; }
            default: return new int[] {0, 0};
        }
    }


    /**
     * Returns ticket holding time.
     * @return time that tickets will be held
     */
    public static long getTimeWaited() {
        return TIME_WAITED;
    }


    /**
     * Returns container for customer email object.
     * @return Container for customer email information
     */
    public HashMap<Integer, String> getCustomerInfo() {
        return customerInfo;
    }

    /**
     * returns availability of seats for each level.
     * @return number of available seats on each level
     */
    public int[] getAvailability() {
        return availability;
    }

    /**
     * Returns object containing information about currently held tickets.
     * @return container for currently held tickets
     */
    public HashMap<Integer, Thread> getThreadMap() { return threadMap; }

    /**
     * Returns event name
     * @return name of the event
     */
    public String getEventName() { return eventName; }

    /**
     * Returns seatManagers which returns Arraylist containing each level's seat information.
     * @return Arraylist of SeatManager for each level.
     */
    public ArrayList<SeatManager> getSeatManagers() { return seatManagers; }

    /**
     * Hashmap contains customer email information. Key represents seatID of the seat that particular customer reserved.
     */
    HashMap<Integer, String> customerInfo;

    /**
     * Name of the event.
     */
    String eventName;

    /**
     * Hashmap that contains information of tickets that are currently held. Key equals seatID of first seat(seat with
     * the smallest seatID) and values are individual thread that deals with each holding.
     */
    HashMap<Integer, Thread> threadMap;

    /**
     * Integer array that contains information about number of available seats for each level. Formats are available
     * seats for {Orchestra, Main, Balcony1, Balcony2}. When the value = 0, there is no availability.
     */
    int[] availability;

    /**
     * Following static final values represent size of each level. The format is {size of row, size of column}.
     */
    static final int[] ORCHSIZE = {50, 25};
    static final int[] MAINSIZE = {100, 20};
    static final int[] BAL1SIZE = {100, 15};
    static final int[] BAL2SIZE = {100, 15};

    /**
     * Default time for holding tickets. If customer does not reserve items after timeWaited value, The ticket will be released.
     * The scale is in milisecond.
     */
    static final long TIME_WAITED = 100;

    /**
     * seatManagers contains seatManager object for each level. For this case, there are 4 levels, so 4 objects are contained
     * and format is {Orchestra, Main, Balcony1, Balcony2}.
     */
    ArrayList<SeatManager> seatManagers;

}

