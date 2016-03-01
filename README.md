Author : Kitae Kim

# TicketService
TicketService program is built to support smooth ticketing service under automation. The program can hold, reserve, and release tickets during process of ticket reservation.

# Underlying logic for ticketing.
- The program does not support manual selection of tickets but provide automatic selection of best available tickets in each particular level.
- Definition of best available ticket in terms of this program
    If possible, customer will get seats that are connected to each other for all of customer in one order.
    If there is no seats big enough to contain all of customers in one order. Minimize splitting among customers. 
    For example, if the size of an order is 8 and there is connceted empty row that is bigger than 8, the customer will get it.
		otherwise, it will look for 2 splits then 3 splits and so on...
	Eventually, if customers cannot fit into one level, look for different levels and split customers into different levels.
Assumption
    - customer always wants biggest clump. The program will always provide single biggest clump regardless of the split. It means if the order size is 9 and we have choice of 8, 1 split and 5, 4 split. The program will always choose 8, 1 split giving biggest clump on one clump.
    - customers prefer not to be splited into different level and they will do it only when they have to.
    - Pricing of the tickets do not affect customer's seating arrangment as long as all customers are in the same level.
    - E mail address for the reservation is shared by

# System layout
The testing can be simply done by creating MyService object. The class can be constructed with call new MyService("event name"). Once the object is created, every seats are empty by default. To hold seats, call findAndHoldSeats(int numSeats, int minLevel, int maxLevel, String customerEmail). The hold will last for time equivalent to timeWaited value which can be easily changed in the class and 100miliseconds by default. (Customer needs to be very fast or holding is gone.) To reserve seats, call reserveSeats(int seatHoldId, String customerEmail) However, I assumed all the seats will be held before reservation, so calling reserveSeats without holding tickets will fail in operation.


# Logic behind ticketing
Ticketing process in the program is separated into two part. On the first part, it looks for one connected seats, that can provide seats for all customers in one seat from the lowest number of column. This part is not absolutely necessary for the program but needed for optimization which will be talked about on second part.
    On second part, the program converts all connected seats as a one token. For example, if first row is empty and the row size is 20, then program takes 20 seats as one token. If one person is seating on second seat of the first row,

    X O X X X X X X X X X X X X X X X X X X \n
    O = customer, X = empty seats

then the program takes one token of size 1, and another token of size 18. The program takes in all tuples from a single level and put them into the queue. From the queue, the program always takes out biggest tuple first to give largest possible adjacent seat for the customer. Although the part 2 can solely provide best solution the reason for implementing part 1 is that it is often better to leave largest tuple for the future if possible.Example case is this. For empty level 1 with size of 50 seats on row and 25 seats on column, request to reserve 10 seats will provide first 10 seats on the first row. Once these seats are filled let¡¯s say another request of 10 seats comes in. My part2 implementation will still provide consecutive 10 seats since it is available in the level but it will provide seats from 2nd column instead of first column despite of having spaces on the first column. It is often better to fill row by row because it gives better option for future customers to have bigger consecutive seats. With collaboration of both parts, the program will fill out first row first if possible. 

# Possible improvement
There are few possible improvements for the current algorithm. One possible improvement can be done by distance calculation among seats. Current implementation does not consider the distance between seats when the customer has to be split. 
For example, when customer request seat of 9, and there is no availability for all 9 people to seat together, then program seeks for next best availability. If there is only one place where the program can fit 7 people and there are 2 places where the program can fit 2 people. Then program will randomly choose one of two places for 2 people regardless of the distance between 2 people and 7 people. However, with correct geometry information, it is possible to incorporate distance information very easily in the program. Once the geometric information is filled in for each seat in 3D spaces, by adding cases for compareTo method when two objects have same seat size, the factor can easily be incorporated. 
