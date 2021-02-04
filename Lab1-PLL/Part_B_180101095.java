/*
CS331: Programming Language Lab
Assignment I: Basic Java Concurrent Programming
Subassignment: Part B Simpson Integral
Name: Vedika Kulkarni
Roll No. : 180101095

Run: java Part_B_180101095.java x
    (x = input number of threads)

Output: Estimated value of the integral is output

*/

import java.util.*;

public class Part_B_180101095 {

    static int numThreads; //input by user - total number of threads
    static int f=0;
    static int num_points_in_interval;


    public static void main( String args[]){

        double a =-1, b=1;
        int numThreads=10;

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
if(f!=1){

    double[] x_arr = new double[numThreads+2];

    double diff = (b-a)/numThreads;


    x_arr[1]=a;
    x_arr[numThreads+1] = b;
    for(int i =2;i<numThreads+1;i++){
        x_arr[i] = x_arr[i-1]+diff;
    }

    num_points_in_interval = 1000000/numThreads;
    if(num_points_in_interval%2==0) num_points_in_interval+=1;

    // Array of threads to do the tasks
    Worker thread_array[] = new Worker[numThreads+1];

    //List of threads to wait for all threads to finish
    List<Thread> list_threads = new ArrayList<Thread>(numThreads);

    // start each thread
    for (int i = 1; i < numThreads+1; i++) {
        thread_array[i] = new Worker(x_arr[i], x_arr[i+1],num_points_in_interval);
        list_threads.add(thread_array[i]);
        thread_array[i].start();
    }

    //waiting for all threads to finish execution
    for (int i = 0; i < numThreads; i++) {
        try { list_threads.get(i).join();} 
        catch (InterruptedException e) { //thread i is done
        }
    }

    double ans=Worker.ans/Math.sqrt(2*3.14);
    System.out.print("Estimated val of integral is "+ans+"\n");



}
    }

}

class Worker extends Thread{

    double start,end, delta;
    int points_in_interval;
    double individual_sum;
    static double ans=0;

    static double func(double x) 
    { 
        double y = -1*x*x;
        return Math.exp(y/2); 
    } 
  
    public Worker(double start_x,double end_x, int num_pts){
        start = start_x;
        end = end_x;
        points_in_interval = num_pts;
        delta = (end - start)/(points_in_interval+1);
    }

    public void run(){

        individual_sum = 0;

        double x = start;
//        System.out.print("x = "+x+" | f(x) = "+func(x)+"\n");
 
        individual_sum+=func(x);
        x+=delta;
        for(int i=0;i<points_in_interval;i++){
            if(i%2==0){
                individual_sum+=4*func(x);
            }
            else{
                individual_sum+=2*func(x);
            }
            x+=delta;
        }
//        System.out.print("x = "+x+" | f(x) = "+func(x)+"\n");
 
        individual_sum+=func(x);

        individual_sum = delta*individual_sum/3;

//        System.out.print("Start = "+start+" End = "+end+" Val = "+individual_sum+"\n");

        ans+=individual_sum;

        }

}
