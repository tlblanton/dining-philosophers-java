//package co.tylerblanton;
import java.util.concurrent.locks.Lock;
import java.util.Random;

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
                think();       //take some time to think after eating (or not eating). Think for T milliseconds. Eat for E. T<E
                getLeftChopstick();     //try to get left. pick up if available and lock it.
                getRightChopstick();    //if you have left, try to get right. If you can get right, lock it. If not, unlock left and wait
                eat();  //If you have both chopsticks then increment timesEaten and wait(time spent eating), and then drop chopsticks.
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
        try
        {
            Thread.sleep(temp);
        }catch(Exception e)
        {
            System.out.println("Can't spend time thinking -> " + e);
        }
    }

    public void getLeftChopstick()
    {
        state = "Hungry";   //when you start grabbing for chopsticks it means you're hungry
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
            state = "Eating";   //if you have both chopsticks, you are then put in eating state
            display();          //dispay change of state
            try
            {
                eatTime = 2 + randomGen.nextInt(4);     //getting random number between 2 and 5. Because time thinking must be less than time spend eating and we go in whole second increments, this minimum for eating cannot be 1 second.
                eatTime *= 1000;        //time 1000 to convert to whole seconds instead of milliseconds
                Thread.sleep(eatTime);  //thread sleep for 'eatTime' milliseconds (2000 to 5000) or 2 to 5 seconds
            }catch(Exception e)
            {
                System.out.println("Couldn't get to sleep after eating -> " + e);
            }

            DiningPhilosophers.chopsticks.get(leftNum).unlock();    //releasing chopsticks and setting chopstick numbers to -1
            DiningPhilosophers.chopsticks.get(rightNum).unlock();
            leftNum = -1;
            rightNum = -1;
            leftChopstick = false;
            rightChopstick = false;
            timesEaten++;
        }
        else
        {
            state = "Hungry";
            releaseChopsticks();
            display();
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

    //in order to make sure that output doesn't get jumbled and awful, I treat the display function as a shared resource(similar to the chopstick locks)
    //and so I have one lock to say whether the display is currently being used by a philosopher or not. If it is locked, then the philosopher
    //waits for his turn to display. If not, the phiolosopher locks the display lock, displays new information, then unlocks the display lock.
    //This ensures that each time there is a change in state, it is displayed
    public void display()
    {
        while(DiningPhilosophers.disp.isLocked())
        {
            try
            {
                Thread.sleep(50);
            }catch(Exception e)
            {
                System.out.println("Can't sleep in display -> " + e);
            }
        }
        DiningPhilosophers.disp.lock();
        System.out.format("%-15s%-15s%s\n", "Philosopher", "State", "Times Eaten");
        System.out.println("------------------------------------------");
        for(int i = 0; i < DiningPhilosophers.numOfPhilosophers; ++i)
        {
            System.out.format("%-15d%-15s%d\n", DiningPhilosophers.philArr.get(i).number, DiningPhilosophers.philArr.get(i).state, DiningPhilosophers.philArr.get(i).timesEaten);
        }
        System.out.println("------------------------------------------\n");
        DiningPhilosophers.disp.unlock();
    }
}
