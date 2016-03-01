package TicketHW;

import TicketHW.RigorousEstimate.SeatBundle;
import java.util.ArrayList;


/**
 * Created by kitae on 2016-02-27.
 * The class is used to keep track of time of each holding. The Class observes each holding once it is created and removes
 * seats from holding state once holding time is expired. Externally interrupting this class will put currently held
 * seats to reserve state.
 */
public class TimeThread extends Thread{

    /**
     * Constructor for timerThread. It takes in information about seatHold and the current service being used.
     * @param seatHold SeatHold object that contains information about seats being held on this time
     *                 trigger.
     * @param myService The service where timerThread is operating under.
     */
    public TimeThread(SeatHold seatHold, ArrayList<SeatBundle> seatBundle, MyTicketService myService) {
        super();
        holdings = seatHold.get();
        bundleList = seatBundle;
        service = myService;
    }

    /**
     * Returns seats held on this reservation.
     * @return return arraylist of held seats.
     */
    ArrayList<Seat> getHeldSeats() {return holdings; }


    /**
     * Run function of timerThread that will start count down for holding expiration. If the function is not interrupted
     * until expiration time, it will remove held tickets from holding state and release them.
     */
    @Override
    public void run() {
        try {
            Thread.sleep(service.getTimeWaited());
        } catch (InterruptedException e) {
            return;
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                service.removeHeldTicket(holdings, bundleList);
            }
        });
        thread.start();

    }

    /* The pointer to Arraylist object that contains information about seats being held on this holding. */
    ArrayList<Seat> holdings;

    /* BundleList contains information of seats for this specific holding in seatbundle form. Seatbundle is used for
     * rigorousEstimate package */
    ArrayList<SeatBundle> bundleList;

    /* The service object that timerThread is operation on. */
    MyTicketService service;
}
