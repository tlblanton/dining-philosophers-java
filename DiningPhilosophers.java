package co.tylerblanton;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

//use java util lock and unlock to avoid deadlocking

public class DiningPhilosophers implements Runnable
{
    public static int numOfPhilosophers;
    static final Philosopher[] philArr = new Philosopher[numOfPhilosophers];
    public static void main(String[] args)
    {
        if(args.length != 0)
        {
            numOfPhilosophers = Integer.parseInt(args[0]);
        }
        else
        {
            System.out.println("No command line arguments. Program terminated.");
            System.exit(1);
        }
        //this makes it so there are 5 chopsticks for 5 philosophers
        //each chopstick is a lock so that it can be locked or unlocked based on whether the philosopher has it or not
        Lock[] chopsticks = new ReentrantLock[numOfPhilosophers];

        //array of 'numOfPhilosophers' Philosophers


        for(int i = 0; i < numOfPhilosophers; ++i)
        {
            philArr[i] = new Philosopher(i);
            Thread t = new Thread(philArr[i]);
            t.start();
        }


            //(new Thread(new philArr[i]())).start();
//        Philosopher p = new Philosopher();
//        Philosopher p1 = new Philosopher();
//        Philosopher p2 = new Philosopher();
//        Thread t1 = new Thread(p1);
//        Thread t2 = new Thread(p2);
//        Thread t = new Thread(p);
//        t.start();
//        t1.start();
//        t2.start();
            //philArr[i] = new Philosopher(i, ).start()//not sure how to properly pass locks to each philosopher I'll be creating.
            //create philosopher and call new phil.start()

    }

    @Override
    public void run()
    {
        System.out.println("In here");
    }

    public static void display()
    {
        System.out.println("Philosopher\tState\tTimesEaten");
        for(int i = 0; i < numOfPhilosophers; ++i)
        {
            System.out.println(philArr[i].number + "\t" + philArr[i].state + "\t" + philArr[i].timesEaten);
        }
    }
}
