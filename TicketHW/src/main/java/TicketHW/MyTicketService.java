/**
 * Created by kitae on 2016-02-26.
 * Main frame for ticket reservation system. A single myTicketService class can handle all of required functions for
 * ticketing. Once myticketservice is created, ticket reservation request can be easily be held and the class takes care
 * of selecting best seats, hold seats, reserve seats, and release seats once time is expired. The class is multi thread
 * safe for multiple booking process.
 */

package TicketHW;

import TicketHW.RigorousEstimate.SeatManager;

import java.util.*;

public class MyTicketService implements TicketService{

    /**
     * Constructor of MyService
     * @param name a name of the event that can be used to identify each service.
     */
    public MyTicketService(String name) {
        eventName = name;
        threadMap = new HashMap<Integer, TimeThread>();
        Collections.synchronizedMap(threadMap);
        customerInfo = new HashMap<Integer, String>();
        Collections.synchronizedMap(customerInfo);
        availability = new int[] {ORCHSIZE[0] * ORCHSIZE[1], MAINSIZE[0] * MAINSIZE[1], BAL1SIZE[0] * BAL1SIZE[1], BAL2SIZE[0] * BAL2SIZE[1]};
        seatManagers = new HashMap<>();
        Collections.synchronizedMap(seatManagers);
        for (int i = 0; i < 4; i++) {
            seatManagers.put(i, new SeatManager(i + 1, getSizeLC(i + 1)));
        }
        reservations = new HashMap<>();
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
        reservations.put(seatHoldId, threadMap.get(seatHoldId).getHeldSeats());
        threadMap.remove((seatHoldId));
        return Integer.toString(seatHoldId);
    }

    /**
     * Find and hold the best available seats for a customer. Throw illegal argument exception if none of the level has
     * enough spaces in a single level.
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
        return SeatManager.findAndHoldSeats(numSeats, minLevel, maxLevel, customerEmail, this);
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
     * Provides method for which level to look for when searching available seats. The function decides what level to look
     * for and how many levels to look for to provide best seating availability.
     * @param numSeats number seats to be reserved.
     * @param minLevel minimum level that customer requires.
     * @param maxLevel maximum level that customer requires.
     * @return arraylist of level integer in sorted form.
     */
    public ArrayList<Integer> findLevelForHold(int numSeats, int minLevel, int maxLevel) {
        ArrayList<Integer> levelArray = new ArrayList<>();
        PriorityQueue<Integer> queue = new PriorityQueue<>(4, Collections.reverseOrder());
        for (int i = minLevel; i <= maxLevel; i++) {
            if (numSeatsAvailable(i) >= numSeats) {
                levelArray.add(i);
                return levelArray;
            }
            queue.add(numSeatsAvailable(i));
        }
        int seatsAccumulated = 0;
        while (!queue.isEmpty()) {
            int temp = queue.poll();
            for (int i = minLevel; i <= maxLevel; i++) {
                if (numSeatsAvailable(i) == temp) {
                    levelArray.add(i);
                    minLevel = i + 1;
                    break;
                }
            }
            seatsAccumulated += temp;
            if (seatsAccumulated >= numSeats) {
                Collections.sort(levelArray);
                return levelArray;
            }
        }
        throw new IllegalArgumentException("None of the requested levels contains enough seats.");
    }


    /**
     * Whenever held seats are returned to the rowmanagement due to expiration of holding time, reaarange needs to be called
     * to combine pieces of seatbundle objects into one if they are adjecent to each other. Calling rearrange on myticket
     * service level will rearrange every row in the entire system.
     */
    public void rearrange() {
        for (int i = 0; i < seatManagers.size(); i ++) {
            seatManagers.get(i).rearrange();
        }
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
    public HashMap<Integer, TimeThread> getThreadMap() { return threadMap; }

    /**
     * Returns event name
     * @return name of the event
     */
    public String getEventName() { return eventName; }

    /**
     * Returns seatManagers which returns Arraylist containing each level's seat information.
     * @return Arraylist of SeatManager for each level.
     */
    public HashMap<Integer, SeatManager> getSeatManagers() { return seatManagers; }

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
    HashMap<Integer, TimeThread> threadMap;

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
    static final long TIME_WAITED = 1000;

    /**
     * seatManagers contains seatManager object for each level. For this case, there are 4 levels, so 4 objects are contained
     * and format is {Orchestra, Main, Balcony1, Balcony2}.
     */
    HashMap<Integer, SeatManager> seatManagers;

    /* Saved Reservations. */
    HashMap<Integer, ArrayList<Seat>> reservations;
}

