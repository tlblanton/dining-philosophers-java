//package co.tylerblanton;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

//use java util lock and unlock to avoid deadlocking

public class DiningPhilosophers
{
    public static int numOfPhilosophers;
    public static ReentrantLock disp = new ReentrantLock();
    public static ArrayList<Philosopher> philArr = new ArrayList<Philosopher>();
    public static ArrayList<ReentrantLock> chopsticks = new ArrayList<>();
    public static void main(String[] args)
    {
        if(args.length != 0)
        {
            numOfPhilosophers = Integer.parseInt(args[0]);
            for(int i = 0; i < numOfPhilosophers; ++i)
            {
                chopsticks.add(new ReentrantLock());        //there should now be 5 locks for the 5 chopsticks (given that args[0] is 5)
            }
        }
        else
        {
            System.out.println("No command line arguments. Program terminated.");
            System.exit(1);
        }


        for(int i = 0; i < numOfPhilosophers; ++i)
        {
            philArr.add(new Philosopher(i));
            Thread t = new Thread(philArr.get(i));
            t.start();
        }

    }
}
