package co.tylerblanton;
import java.util.concurrent.locks.Lock;
import java.util.Random;
import co.tylerblanton.DiningPhilosophers;

/**
 * Created by tlblanton on 3/3/16.
 */

//philosophers eat for less than 5 seconds, and

public class Philosopher extends Thread
{
    boolean leftChopstick;
    boolean rightChopstick;
    int timesEaten;
    String state;
    int number;
    public Philosopher(int n, boolean r, boolean l)
    {
        leftChopstick = l;
        rightChopstick = r;
        timesEaten = 0;
        number = n;
    }
    public Philosopher(int newNum)
    {
        timesEaten = 0;
        number = newNum;
    }


    //each philosopher will have their own run through of this
    public void run()
    {
        for(int i = 0; i < 10; i++)
        {
            System.out.print(i);
        }

        while(timesEaten < 5)
        {
//            getLeftChopstick();
//            getRightChopstick();
//            eat();  //wait in here
        }
        //while haven't eaten five times.
        //left
        //right
        //eat
        //wait
    }

    public void getLeftChopstick()
    {

        //somehow grab left chopstick from collective pile.
    }
    public void getRightChopstick()
    {
        //try to grab another chopstick from collective pile
    }
    public void eat()
    {

    }

    int grabChopstick() //return -1 if no chopstick to grab, 0 otherwise
    {


        return 0;
    }

    void releaseChopstick(Lock l)
    {
        l.unlock();
    }

}
