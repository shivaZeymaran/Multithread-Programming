import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * This program is the first phase of Operating system course project.
 * It will handle a street with cars that want to pass that without any problems.
 * The main class:
 * It contains 4 Queues for 4 sides of the street. they made of cars that are in that side of street
 * these queues are important to know which car in each side should move at first.
 *
 * It also contains 8 semaphores: 4 for the places in the middle part and 4 for each side of street.
 *
 * the second 4 semaphores used to block all cars in one side's list,
 * until the first car of that list completes its movement and removed from that list.
 *
 * We also have an Array list to store all the threads that created for each car.
 *
 * @author Shiva Zeymaran
 */
public class Street {

    static Queue<Car> west_list = new LinkedList<>();
    static Queue<Car> east_list = new LinkedList<>();
    static Queue<Car> north_list = new LinkedList<>();
    static Queue<Car> south_list = new LinkedList<>();
    static Semaphore se = new Semaphore(1);
    static Semaphore sw = new Semaphore(1);
    static Semaphore ne = new Semaphore(1);
    static Semaphore nw = new Semaphore(1);
    static Semaphore west_street = new Semaphore(1);
    static Semaphore south_street = new Semaphore(1);
    static Semaphore east_street = new Semaphore(1);
    static Semaphore north_street = new Semaphore(1);

    private static ArrayList<CarThread> threadList = new ArrayList<>();

    public static void main(String[] args) {

        // inputs
        Scanner scanner = new Scanner(System.in);
        System.out.println("type the source and destination with one space between them:");
        // first we get the number of cars that we want to process
        int numberOfCars = scanner.nextInt();
        for (int i = 0; i < numberOfCars; i++) {
            int source = scanner.nextInt();
            int destination = scanner.nextInt();
            Car car = new Car(source, destination);
            car.add_to_list();
            CarThread carThread = new CarThread(car);
            threadList.add(carThread);
        }

        // start processing
        // iterating the thread list and make all of its threads to be started
        for (CarThread carThread : threadList) {
            carThread.thread.start();
        }

        // main thread should wait for other threads to be completed
        // and then prints that our process is finished successfully
        // so we use join method
        for (CarThread carThread : threadList) {
            try {
                carThread.thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("All workers completed");
    }
}