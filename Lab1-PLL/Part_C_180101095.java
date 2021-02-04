/*
CS331: Programming Language Lab
Assignment I: Basic Java Concurrent Programming
Subassignment: Part C Matrix Multiplication
Name: Vedika Kulkarni
Roll No. : 180101095

Run: 
1. java Part_C_180101095.java x
    (x = input number of threads)
2. Program will ask the user if he wants to check if value of a cell is correct after multiplication,
    if yes, input row number and col number for the same.

Note: Matrices A and B have been populated with integer values
This can be changed by uncommenting the respective code in run method of Worker_to_initialise class.
However, taking double (float) values might cause cell check to fail due to decimal multiplication
value comparison.
*/



import java.util.*;

public class Part_C_180101095 {

    static int numThreads;

    public static void print_matrix( double A[][], String name){
        int rows = A.length;
        int cols = rows;
        for(int i = 0;i<rows;i++){
	        for(int j=0;j<cols;j++){
              System.out.print(A[i][j]+"\t|\t");
            }
            System.out.print("\n");

        }
        System.out.print("\n");
    }

    static boolean check_cell(double[][] A, double[][] B,double C[][], int r, int c){
        int N=C[0].length;
        if(r>N || c>N){
            System.out.print("Error in val of r and/or c\n");
            return false;
        }
        double cell=0; 
        for(int k=0;k<N;k++){
            cell+= A[r][k]*B[k][c];
        }
        if(cell == C[r][c]) return true;
        else return false;
    }
    /*
    Following function is used to initialise matrices using threads
    */ 

    public static void initialise_by_threads(double[][] A,int N, int[] starting_row_for_thread){
        int rows_per_thread;
        if(N%numThreads==0){
            rows_per_thread = N/numThreads;
        }
        else{
            rows_per_thread = (N-N%numThreads)/numThreads;
        }

        Worker_to_initialise th_array[] = new Worker_to_initialise[numThreads+1];

        //List of threads to wait for all threads to finish
        List<Thread> list_th = new ArrayList<Thread>(numThreads);
    
        // start each thread
        for (int i = 1; i < numThreads+1; i++) {
            if(i!=numThreads){
                th_array[i] = new Worker_to_initialise(A,starting_row_for_thread[i],rows_per_thread);
                list_th.add(th_array[i]);
                th_array[i].start();
        
            }
            else{
                th_array[i] = new Worker_to_initialise(A,starting_row_for_thread[i],N-starting_row_for_thread[i]);
                list_th.add(th_array[i]);
                th_array[i].start();
            }
        }
    
        //waiting for all threads to finish execution
        for (int i = 0; i < numThreads; i++) {
            try { list_th.get(i).join();} 
            catch (InterruptedException e) { //thread i is done
            }
        }

    }

    public static void main( String args[]){

        numThreads = 4;
        int N = 1000, f=0;
        int rows_per_thread;
        double A[][] = new double[N][N];
        double B[][] = new double[N][N];
        double C[][] = new double[N][N];

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

if(f==0)  {
        int[] starting_row_for_thread = new int[numThreads+2];

        if(N%numThreads==0){
            rows_per_thread = N/numThreads;
        }
        else{
            rows_per_thread = (N-N%numThreads)/numThreads;
        }

        initialise_by_threads(A, N, starting_row_for_thread);
        initialise_by_threads(B, N, starting_row_for_thread);
        
//        print_matrix(A, "A");
//        print_matrix(B, "B");    

            // Array of threads to do the tasks
    Worker thread_array[] = new Worker[numThreads+1];

    //List of threads to wait for all threads to finish
    List<Thread> list_threads = new ArrayList<Thread>(numThreads);

    // start each thread
    for (int i = 1; i < numThreads+1; i++) {
        if(i!=numThreads){
            thread_array[i] = new Worker(A,B,C,starting_row_for_thread[i],rows_per_thread);
            list_threads.add(thread_array[i]);
            thread_array[i].start();
    
        }
        else{
            thread_array[i] = new Worker(A,B,C,starting_row_for_thread[i],N-starting_row_for_thread[i]);
            list_threads.add(thread_array[i]);
            thread_array[i].start();
        }
    }

    //waiting for all threads to finish execution
    for (int i = 0; i < numThreads; i++) {
        try { list_threads.get(i).join();} 
        catch (InterruptedException e) { //thread i is done
        }
    }

//    print_matrix(C, "Threaded Multiplication");

    int check_flag=1;

    while (check_flag==1){
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Do you want to check a cell? Type 1, Else Type 0 : ");
    
        check_flag = myObj.nextInt();  // Read user input
    
        if (check_flag==1){
    
            System.out.println("Enter row number and col number (space separated) : ");
    
            int r = myObj.nextInt();  // Read user input
            int c = myObj.nextInt();
            
            boolean t = check_cell(A, B, C, r,c);
    
            if(t) System.out.print("Cell check - Okay\n");
            else System.out.print("Cell check - Failed\n");
        
    
        }else if(check_flag==0){
            System.out.println("Thank you, terminating \n");
    
        }else{
            System.out.println("Error, terminating \n");
        }
    
    }


    }
    }

}

class Worker extends Thread{

    double[][] A,B,C;  
    int start, N; 

    public Worker(double mat1[][],double mat2[][], double mat3[][],int start_row, int no_rows ){
        A = mat1; 
        B = mat2;
        C = mat3;
        start = start_row;
        N = no_rows;
    }

    public void run()
    {
	int c = A[0].length;
	for(int i = start;i<N+start;i++){
        if(i>c) break;
        for(int j=0;j<c;j++)
		{
		    C[i][j] = 0;
		    for(int k=0;k<c;k++)
    			C[i][j] += A[i][k] * B[k][j];
		}	
    }}

}

class Worker_to_initialise extends Thread{

    double[][] A;  
    int start, N; 

    public Worker_to_initialise(double mat1[][],int start_row, int no_rows ){
        A = mat1; 
        start = start_row;
        N = no_rows;
    }

    public void run()
    {
        int min = 1, max = 9, cols =A[0].length;  
	    for(int i = start;i<(N+start);i++){
	        for(int j=0;j<cols;j++)
                { 
                // For integer values, use following
                A[i][j]=Math.round((Math.random() * (max - min + 1) + min)) ;

                // For double values, use following
//              A[i][j]=Math.round((Math.random() * (max - min + 1) + min)*10) ;
//              A[i][j]/=10.0;
            }
        }

    }

}

