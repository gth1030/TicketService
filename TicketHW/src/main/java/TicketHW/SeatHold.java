package TicketHW;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by kitae on 2016-02-27.
 * SeatHold object contains multiple Seat objects to handle them together properly. SeatHold object also contains a
 * unique holdID number that indentifies existing holding of seats from others.
 */
public class SeatHold {

    /**
     * Constructor of SeatHold object. It initiates data structure for the seatHolding object.
     */
    public SeatHold () {
        seatHoldArray = new ArrayList<Seat>();
        Collections.synchronizedList(seatHoldArray);
    }

    /**
     * Returns arraylist that contains Seat objects. The seatHolding contains all of seats that are being held on this
     * holding.
     * @return arraylist of Seat objects that contains information of seats being held.
     */
    public ArrayList<Seat> get() {
        return seatHoldArray;
    }

    /**
     * Add another seat on this holding.
     *
     * @param seat Seat object that contains information of seat to be held.
     */
    public void add(Seat seat) {
        seatHoldArray.add(seat);
    }

    /**
     * Add other seats on this holding.
     * @param seatHold seatHold object that contains Seat objects to be added on this holding.
     */
    void add(SeatHold seatHold) {
        seatHoldArray.addAll(seatHold.get());
    }

    /**
     * Remove particular seat from the holding. The seats removed from holding before expiration time of holding will
     * be reserved.
     * @param seat seat to be removed from the holding.
     */
    void remove(Seat seat) {
        seatHoldArray.remove(seat);
    }

    /**
     * Returns number of seats being held on this holding object.
     *
     * @return number of seats being held on this holding object.
     */
    public int size() {
        return seatHoldArray.size();
    }

    /**
     * Sets up unique SeatHoldID for the holding object. SeatHoldID is used as unique identifier for each holding.
     *
     * @param ID A unique SeatHoldID for this holding.
     */
    public void setSeatHoldID(int ID) {
        seatHoldID = ID;
    }

    /**
     * Returns SeatHoldID of this object. The function provides unique identifier for each holding.
     *
     * @return unique SeatHoldID for this object.
     */
    public int getSeatHoldID() {
        return seatHoldID;
    }

    /**
     * ArrayList that contains Seat objects that are being held on this holding.
     */
    ArrayList<Seat> seatHoldArray;

    /* A unique SeatHoldID for this holding. The number is used to identify individual holdings from others. */
    int seatHoldID;

}
