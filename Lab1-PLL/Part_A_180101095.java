/*
CS331: Programming Language Lab
Assignment I: Basic Java Concurrent Programming
Subassignment: Part A PI value by Monte Carlo Simulation
Name: Vedika Kulkarni
Roll No. : 180101095

Run: java Part_A_180101095.java x
    (x = input number of threads)

Output: Estimated value of PI is output

*/


import java.util.*;

public class Part_A_180101095 {
    
    static int numThreads; //input by user - total number of threads
    static int f=0;
    public static void main(String[] args) {
            
        if (args.length == 0) {
            f=1;
            System.out.print("ERROR - Num of threads missing in input - Terminating\n");
        } else if (args.length == 1) {
            try { numThreads = Integer.parseInt(args[0]);} 
            catch (Exception e) { 
                f=1;
                System.out.print("ERROR - Input integer for num of threads - Terminating\n");
            }
			
		} else {
            f=1;
            System.out.print("ERROR - Too many inputs - Enter num of threads only - Terminating\n");
        }

        // Array of threads to do the tasks
        Task thread_array[] = new Task[numThreads];

        //List of threads to wait for all threads to finish
        List<Thread> list_threads = new ArrayList<Thread>(numThreads);

        // start each thread
        for (int i = 0; i < numThreads; i++) {
            thread_array[i] = new Task(numThreads);
            list_threads.add(thread_array[i]);
            thread_array[i].start();
        }

        //waiting for all threads to finish execution
        for (int i = 0; i < numThreads; i++) {
            try { list_threads.get(i).join();} 
            catch (InterruptedException e) { //thread i is done
            }
        }
        
        // summed_pi holds the sum of all pi values estimated by repective threads
        // summed_pi is divided by numThreads to get the final estimate
        double val_pi = Task.summed_pi/numThreads;
        if(f!=1)
        System.out.print("Estimated val of pi is "+val_pi+"\n");
    }
}

class Task extends Thread{

    int iterations_per_thread;
    static double summed_pi=0;

    public Task(int numThreads){
        iterations_per_thread = 1000000/numThreads;
    }

    public void run(){

        Random R = new Random();
		double points_in_circle = 0, x =0,y=0,pi=0;		
        
        for(int i=0;i<iterations_per_thread;i++){
			x = R.nextDouble();
			y = R.nextDouble();
			if(x*x + y*y <= 1) {
				points_in_circle += 1;
            }      
        }
        pi = (double)points_in_circle / iterations_per_thread * 4.0;
        summed_pi+=pi;
    }
}