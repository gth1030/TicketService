package TicketHW.RigorousEstimate;

import com.sun.rowset.internal.Row;

/**
 * Created by kitae on 2016-02-27.
 * Seatbundle class contains information of one connected seats. One object contains number of seats that are connected
 * to each other. Unconnected seats cannot be contained in one object.
 */
public class SeatBundle implements Comparable<SeatBundle> {

    /**
     * Constructor for seatbundle object. It takes in its parent row, its row number of the first seat, and its column
     * number. First seat always has smallest seatID.
     * @param rowNumber row number of the first seat.
     * @param sizeOfSeats number of seats in this object.
     * @param rowM parent object of this bundle.
     */
    public SeatBundle(int rowNumber, int sizeOfSeats, RowManagement rowM) {
        rowN = rowNumber;
        rowManage = rowM;
        sizeOfBundle = sizeOfSeats;
    }

    /* This is for testing purpose. Do not use this function. */
    public SeatBundle(int rowNumber, int sizeOfSeats) { this(rowNumber, sizeOfSeats, null); }

    /* Returns row number of first seat in this bundle. */
    public int getRowNumber() { return rowN; }

    /* Returns column number of this bundle. */
    public int getColumnNumber() { return rowManage.getColumnNumber(); }

    /* Returns number of seats in this bundle */
    int getSizeOfBundle() { return sizeOfBundle; }

    /* Returns parent of this bundled seats. */
    RowManagement getRowManage() { return rowManage; }

    /* Returns level of this bundle. */
    public int getLevel() { return rowManage.getManager().getLevelNumber(); }

    /* Contains row number of the first seat in this bundle. */
    int rowN;

    /* Contains parent of this bundle. */
    RowManagement rowManage;

    /* Contains number of seats in the bundle. */
    int sizeOfBundle;

    /* comparable method for priority queue. */
    @Override
    public int compareTo(SeatBundle bundle) {
        if (sizeOfBundle > bundle.getSizeOfBundle()) {
            return -1;
        } else if (sizeOfBundle == bundle.getSizeOfBundle()) {
            if (getColumnNumber() > bundle.getColumnNumber()) {
                return 1;
            } else if (getColumnNumber() < bundle.getColumnNumber()) {
                return -1;
            }
            return 0;
        }
        return 1;
    }
}
