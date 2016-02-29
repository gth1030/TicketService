/**
 * Created by kitae on 2016-02-26.
 * Seat object contains information of individual seat reserved or held. The class also does some sanity check for seat reserve request.
 */

package TicketHW;


public class Seat {

    /**
     * Constructor for Seat object. It takes in row, column and level information of one seat and stores the information.
     * The constructor also does sanity check for the input values.
     *
     * @param row row number of the seat
     * @param column column number of the seat
     * @param level level of the seat
     */
    public Seat(int row, int column, int level) {
        if (level > 5 || level <= 0 || row <= 0 || column <= 0) {
            throw new IllegalArgumentException("level should be between 1 to 4 and all input should be positive");
        }
        switch(level) {
            case 1: {
                if (row > 50 || column > 25) {
                    throw new IllegalArgumentException("row cannot exceed 50 and column cannot exceed 25 for Orchestra");
                }
            }
            case 2: {
                if (row > 100 || column > 20) {
                    throw new IllegalArgumentException("row cannot exceed 100 and column cannot exceed 20 for Main");
                }
            }
            case 3: {
                if (row > 100 || column > 15) {
                    throw new IllegalArgumentException("row cannot exceed 100 and column cannot exceed 15 for Balcony 1");
                }
            }
            case 4: {
                if (row > 100 || column > 15) {
                    throw new IllegalArgumentException("row cannot exceed 100 and column cannot exceed 15 for Balcony 2");
                }
            }
        }
        rowN = row;
        columnN = column;
        levelN = level;
    }

    /**
     * Second constructor for Seat object. The constructor takes in unique seatID and calculate detailed seat information
     * based on seatID.
     *
     * @param seatID Unique seatID for each seat. Please read readMe for details about seatID.
     */
    public Seat(int seatID) {
        this(seatIDToInfo(seatID)[0], seatIDToInfo(seatID)[1], seatIDToInfo(seatID)[2]);
    }

    /**
     * The function that returns details for the seat object.
     *
     * @return int array that contains seat number of {row, column, level}
     */
    public int[] seatInfo() {return new int[] {rowN, columnN, levelN};
    }


    /**
     * The function provides unique seatID of the seat object.
     *
     * @return returns unique seatID of the object.
     */
    public int seatID () {
        return seatID(rowN, columnN, levelN);
    }

    /**
     * The function that will calculate unique seatID for each seat. SeatID is defined by setting the seat has row = 1,
     * column = 1, level = 1 to have seatID = 1. SeatID increases as row number increases. Once it reaches the end of
     * row, it goes to next column, row = 1, and keeps incrementing seatID by 1. Once it reaches the end of the level,
     * it goes to next level row = 1, column = 1, then continue to increment ID number. BY doing so, individual seat has
     * unique ID number.
     *
     * @param row row number of the seat
     * @param column column number of the seat
     * @param level level of the seat
     * @return unique seatID that correspond to input row, column, level
     */
    public static int seatID(int row, int column, int level) {
        int seatID = 0;
        for (int i = 1; i < level; i++) {
            int[] seatSize = MyTicketService.getSizeLC(i);
            seatID = seatID + seatSize[0] * seatSize[1];
        }
        int[] seatSize = MyTicketService.getSizeLC(level);
        return seatID + seatSize[0] * (column - 1) + row;
    }

    /**
     * Reverse function of seatID function. It takes in unique seatID number and convert it back to general information
     * containing row number, column number, and level.
     * @param seatID unique seatID for the seat of interest.
     * @return int array that contains detailed information of the seat. The format is {row number, column number, level}
     */
    public static int[] seatIDToInfo(int seatID) {

        int dummy = 0;
        int estimateSize = 0;
        int level = 0;
        while (seatID > dummy) {
            level ++;
            estimateSize = dummy;
            int[] seatSize = MyTicketService.getSizeLC(level);
            dummy = dummy + seatSize[0] * seatSize[1];
        }
        seatID = seatID - estimateSize;
        int[] seatSize = MyTicketService.getSizeLC(level);

        if (seatID % seatSize[0]== 0) {
            return new int[] {seatSize[0], seatID / seatSize[0], level};
        }
        return new int[] {seatID % seatSize[0], seatID / seatSize[0] + 1, level};
    }

    /* row number of the seat object */
    int rowN;

    /* column number of the seat object */
    int columnN;

    /* level of the seat object */
    int levelN;

}


