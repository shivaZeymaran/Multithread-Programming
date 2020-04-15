import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * This class represents a thread for each car that has 3 attributes:
 * one is a car that we want to make a thread for that;
 * one is a thread that we want to create it;
 * and one that is a list to save all semaphores that we need for this movement.
 * @see java.lang.Runnable
 */
class CarThread implements Runnable {
    private Car car;
    Thread thread;
    private ArrayList<Semaphore> requiredSemaphores = new ArrayList<>();

    /**
     * The only constructor of the class
     * @param car the current car that we create a thread for that
     */
    CarThread(Car car) {
        this.car = car;
        thread = new Thread(this);
    }


    /**
     * The function of each thread is defined here in run method that start its work when we start a thread.
     * The logic of this part is as follows:
     * 1 when a thread becomes started, it will sleep for one second at first
     * 2 then we find the side list of current thread
     *    2.1 if it is the first element of this list, it can acquire the lock of this side list
     *      and then try to access the street and move
     *      if it can do all of these, then it should removed from that side list and release the side list's lock
     *    2.2 but if it's not the first element of list, it should sleep(e.g. 2seconds) and checks again
     *      until it becomes the first element of its list
     */
    @Override
    public void run() {
        // each Car stop 1 second at first
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // choose between 4 lists
        while (true) {
            if (car.start == 1) {
                if (Street.west_list.peek().equals(car)) {
                    try {
                        Street.west_street.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    canAccessStreet();
                    Street.west_list.remove();
                    Street.west_street.release();
                    break;
                } else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (car.start == 2) {
                if (Street.south_list.peek().equals(car)) {
                    try {
                        Street.south_street.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    canAccessStreet();
                    Street.south_list.remove();
                    Street.south_street.release();
                    break;
                }
                else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (car.start == 3) {
                if (Street.east_list.peek().equals(car)) {
                    try {
                        Street.east_street.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    canAccessStreet();
                    Street.east_list.remove();
                    Street.east_street.release();
                    break;
                }
                else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            // start = 4
            else {
                if (Street.north_list.peek().equals(car)) {
                    try {
                        Street.north_street.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    canAccessStreet();
                    Street.north_list.remove();
                    Street.north_street.release();
                    break;
                }
                else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * A "synchronized" method that each thread will use to check if it can access the street.
     *
     * it is synchronized so it can be run in one thread at the same time and all other threads
     * that want to call this, will be blocked.
     *
     * at first finds which semaphores this car is needed;
     * then it acquires all these semaphores
     * and moves along this places(it implemented by sleeping the thread for number of places seconds)
     * we make this sleep as a comment to run our program much faster(it doesn't have essential effect on the result);
     * then releases all of them.
     */
    private synchronized void canAccessStreet() {
        checkRequiredSemaphores();
        for (Semaphore semaphore : requiredSemaphores) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Car with [" + car.start + " to " + car.end + "] wanna pass the intersection.");
//        try {
//            Thread.sleep(1000*requiredSemaphores.size());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("Car with [" + car.start + " to " + car.end + "] passes safely");
        for (Semaphore semaphore : requiredSemaphores) {
            semaphore.release();
        }
    }


    /**
     * This method just check which semaphores we need for current car's movement
     * and we determine that with two attributes of the car.
     */
    private void checkRequiredSemaphores() {
        if (car.start == 1){
            if (car.end == 2)
                requiredSemaphores.add(Street.sw);
            else if (car.end == 3){
                requiredSemaphores.add(Street.sw);
                requiredSemaphores.add(Street.se);
            }
            // end = 4
            else {
                requiredSemaphores.add(Street.sw);
                requiredSemaphores.add(Street.se);
                requiredSemaphores.add(Street.ne);
            }
        }
        else if (car.start == 2){
            if (car.end == 3)
                requiredSemaphores.add(Street.se);
            else if (car.end == 4){
                requiredSemaphores.add(Street.se);
                requiredSemaphores.add(Street.ne);
            }
            // end = 1
            else {
                requiredSemaphores.add(Street.se);
                requiredSemaphores.add(Street.ne);
                requiredSemaphores.add(Street.nw);
            }
        }
        else if (car.start == 3){
            if (car.end == 4)
                requiredSemaphores.add(Street.ne);
            else if (car.end == 1){
                requiredSemaphores.add(Street.ne);
                requiredSemaphores.add(Street.nw);
            }
            // end = 2
            else{
                requiredSemaphores.add(Street.ne);
                requiredSemaphores.add(Street.nw);
                requiredSemaphores.add(Street.sw);
            }
        }
        // start = 4
        else{
            if (car.end == 1)
                requiredSemaphores.add(Street.nw);
            else if (car.end == 2){
                requiredSemaphores.add(Street.nw);
                requiredSemaphores.add(Street.sw);
            }
            // end = 3
            else {
                requiredSemaphores.add(Street.nw);
                requiredSemaphores.add(Street.sw);
                requiredSemaphores.add(Street.se);
            }
        }
    }
}

