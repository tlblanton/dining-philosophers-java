//package co.tylerblanton;
import java.util.concurrent.locks.Lock;
import java.util.Random;
//import co.tylerblanton.DiningPhilosophers;

/**
 * Created by tlblanton on 3/3/16.
 */

//philosophers eat for less than 5 seconds, and

public class Philosopher implements Runnable
{
    boolean leftChopstick;
    boolean rightChopstick;
    int leftNum;
    int rightNum;
    long eatTime;
    long thinkTime;
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
        leftChopstick = false;
        rightChopstick = false;
        eatTime = 5000;
        leftNum = -1;
        rightNum = -1;
        timesEaten = 0;
        number = newNum;
        state = "hungry";
    }


    //each philosopher will have their own run through of this
    public void run()
    {
        try
        {
            while (timesEaten < DiningPhilosophers.numOfPhilosophers)
            {
                getLeftChopstick();     //try to get left. pick up if available and lock it.
                getRightChopstick();    //if you have left, try to get right. If you can get right, lock it. If not, unlock left and wait
                eat();  //If you have both chopsticks then increment timesEaten and wait(time spent eating), and then drop chopsticks.
                //think();       //take some time to think after eating.. Think for T milliseconds. Eat for E. T<E
            }
            state = "Sleeping";
            releaseChopsticks();
            display();
        }catch (Exception e)
        {
            System.out.println("leftnum is " + leftNum + " rightnum is  " + rightNum);
            System.out.println("Philosopher " + number + " has stopped " + e);
        }
    }

    public void think()
    {
        state = "Thinking";
        display();

        Random randomGen  = new Random();
        thinkTime = randomGen.nextInt((int)eatTime/1000);
        thinkTime *= 1000;
        long temp = thinkTime;
        System.out.println("temp is " + temp + " eat time is " + eatTime);
        try
        {
            System.out.println("about to wait");
            Thread.sleep(temp);
        }catch(Exception e)
        {
            System.out.println("think sleep didn't go well");
        }
    }

    public void getLeftChopstick()
    {
        state = "hungry";
        display();
        leftNum = grabChopstick();
        if(leftNum != -1)
        {
            leftChopstick = true;
        }
        else
        {
            releaseChopsticks();
        }
    }

    public void getRightChopstick()
    {
        if(leftChopstick)
        {
            rightNum = grabChopstick();
            if(rightNum != -1)
            {
                rightChopstick = true;
            }
            else
            {
                releaseChopsticks();
            }
        }
        else
        {
            leftChopstick = false;
            rightChopstick = false;
            rightNum = -1;
            leftNum = -1;
        }
    }

    int grabChopstick() //return -1 if no chopstick to grab, otherwise returns spot of lock that it "grabbed"
    {
        for(int i = 0; i < DiningPhilosophers.numOfPhilosophers; ++i)
        {
            if (DiningPhilosophers.chopsticks.get(i).isLocked() == false)
            {
                DiningPhilosophers.chopsticks.get(i).lock();
                System.out.println("locking chopstick " + i);
                return i;
            }
        }

        return -1;
    }


    public void eat()
    {
        Random randomGen = new Random();
        if(leftChopstick && rightChopstick)
        {
            state = "Eating";
            display();


            try
            {
                Random rand = new Random();
                eatTime = 2 + randomGen.nextInt(4);
                eatTime *= 1000;
                long temp = eatTime;

                Thread.sleep(eatTime);
            }catch(Exception e)
            {
                System.out.println("eat sleep didn't go well");
            }
            System.out.println("about to release. " + leftNum + " " + rightNum);

            for(int i = 0; i < DiningPhilosophers.chopsticks.size(); ++i)
            {
                System.out.print(DiningPhilosophers.chopsticks.get(i).isLocked());
            }

            DiningPhilosophers.chopsticks.get(leftNum).unlock();    //releasing chopsticks and setting chopstick numbers to -1
            DiningPhilosophers.chopsticks.get(rightNum).unlock();
            leftNum = -1;
            rightNum = -1;
            leftChopstick = false;
            rightChopstick = false;
            timesEaten++;
            think();

        }
        else
        {
            try
            {
                state = "hungry";
                display();
                releaseChopsticks();
                System.out.println(number + " did not eat. Waiting for 2 seconds.");
                Thread.sleep(2000);
            }catch(Exception e)
            {

            }
        }
    }

    void releaseChopsticks()
    {
        if(leftNum != -1)
        {
            DiningPhilosophers.chopsticks.get(leftNum).unlock();
        }
        leftChopstick = false;
        leftNum = -1;
        if(rightNum != -1)
        {
            DiningPhilosophers.chopsticks.get(rightNum).unlock();
        }
        rightChopstick = false;
        rightNum = -1;
    }

    public void display()
    {
        while(DiningPhilosophers.disp.isLocked())
        {
            try
            {
                Thread.sleep(50);
            }catch(Exception e)
            {
                System.out.println("Can't sleep in display");
            }
        }
        DiningPhilosophers.disp.lock();
        System.out.println("Philosopher\tState\tTimesEaten");
        System.out.println("--------------------------------");
        for(int i = 0; i < DiningPhilosophers.numOfPhilosophers; ++i)
        {
            System.out.println(DiningPhilosophers.philArr.get(i).number + "\t\t" + DiningPhilosophers.philArr.get(i).state + "\t\t" + DiningPhilosophers.philArr.get(i).timesEaten);
        }
        System.out.println("--------------------------------");
        DiningPhilosophers.disp.unlock();
    }

}
