package TicketHW.RigorousEstimate;

import TicketHW.Seat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by kitae on 2016-02-27.
 * RowManagement class contains information about one row. The class contains seatbundle objects for any available seats
 * on the row.
 */
public class RowManagement {

    /**
     * Constructor for Rowmanagement system. Rowmanagement contains information of this low, its parent's, and its column
     * number.
     * @param seatManager Pointer to parents object that contains this row.
     * @param columnNumber ColumnNumber of this row.
     */
    public RowManagement(SeatManager seatManager, int columnNumber) {
        rowInfoArray = new ArrayList<SeatBundle>();
        Collections.synchronizedList(rowInfoArray);
        manager = seatManager;
        rowInfoArray.add(new SeatBundle(1, manager.getDimension() [0], this));
        columnN = columnNumber;
    }

    /**
     *  When held seats are returned to the rowmanagement after expiration of holding time, reaarange needs to be called
     * to combine pieces of seatbundle objects into one if they are adjecent to each other.
     */
    void rearrage() {
        ArrayList<SeatBundle> newRow = new ArrayList<>();
        for (int i = 0; i < rowInfoArray.size() - 1; i++) {
            if (rowInfoArray.get(i).getRowNumber() + rowInfoArray.get(i).getSizeOfBundle()
                    == rowInfoArray.get(i + 1).getRowNumber()) {
                SeatBundle newBundle = new SeatBundle(rowInfoArray.get(i).getRowNumber(),
                        rowInfoArray.get(i).getSizeOfBundle() + rowInfoArray.get(i + 1).getSizeOfBundle());
                rowInfoArray.add(i + 1, newBundle);
                rowInfoArray.remove(i + 2);
            } else {
                newRow.add(rowInfoArray.get(i));
            }
        }
        newRow.add(rowInfoArray.get(rowInfoArray.size() - 1));
        rowInfoArray = newRow;
    }

    /**
     * Returns Largest connected seats on this row that are available for reservation.
     * @return Seatbundle object that contains information of connceted seats.
     */
    SeatBundle getLargestBundle() {
        int max = 0;
        SeatBundle bundle = null;
        for (int i = 0; i < rowInfoArray.size(); i++) {
            if (rowInfoArray.get(i).getSizeOfBundle() > max) {
                bundle = rowInfoArray.get(i);
                max = rowInfoArray.get(i).getSizeOfBundle();
            }
        }
        return bundle;
    }


    /**
     * The function removes specific seats from availability form input rowmanagement.
     * @param rowManagement rowmanagement object that contains seats to be removed.
     * @param start row number of beginning of the setas
     * @param size number of seats connected each other.
     * @return seatbundle object that contains seats information that are removed from availability.
     */
    static SeatBundle reserveBundle(RowManagement rowManagement, int start, int size) {
        for (int i = 0; i < rowManagement.getRowInfoArray().size(); i++) {
            if (rowManagement.getRowInfoArray().get(i).getRowNumber() == start ) {
                SeatBundle newbundle = new SeatBundle(rowManagement.getRowInfoArray().get(i).getRowNumber() + size,
                        rowManagement.getRowInfoArray().get(i).getSizeOfBundle() - size, rowManagement);
                rowManagement.getRowInfoArray().remove(i);
                rowManagement.getRowInfoArray().add(i, newbundle);
                return rowManagement.getRowInfoArray().get(i);
            }
        }
        throw new IllegalArgumentException("Rowmanagement is not updated due to wrong input.");
    }

    /**
     * The function is created for sole purpose of testing. Do not use.
     */
    static void reserveBundle(RowManagement rowManagement, int start) {
        for (int i = 0; i < rowManagement.getRowInfoArray().size(); i++) {
            if (rowManagement.getRowInfoArray().get(i).getRowNumber() == start ) {
                rowManagement.getRowInfoArray().remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("Rowmanagement is not updated due to wrong input.");
    }

    /**
     * The function adds the seat back into availability once the seat is released from hold without reservation.
     * @param seatBundle Information of seats to be added back to the system for availability.
     */
    public void add(SeatBundle seatBundle) {
        for (int i = 0; i < rowInfoArray.size(); i ++) {
            if (rowInfoArray.get(i).getRowNumber() < seatBundle.getRowNumber()) {
                continue;
            }
            rowInfoArray.add(i, seatBundle);
            return;
        }
        rowInfoArray.add(rowInfoArray.size(), seatBundle);
    }

    public void add(Seat seat) {
        add(new SeatBundle(seat.seatInfo()[0], seat.seatInfo() [1], this));
    }

    /* Returns parent for this row */
    SeatManager getManager() { return manager; }

    /* Returns available seats information of this row. */
    public ArrayList<SeatBundle> getRowInfoArray() { return rowInfoArray; }

    /* Returns column number of this row. */
    int getColumnNumber() { return columnN; }

    /* Contains available seat information of this row. */
    ArrayList<SeatBundle> rowInfoArray;

    /* Contains columnNumber of this row. */
    int columnN;

    /* Contains parent of this row. */
    SeatManager manager;
}
