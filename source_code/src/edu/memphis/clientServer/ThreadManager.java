package edu.memphis.clientServer;


import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author nabin
 * Class for managing thread pool
 * nmharjan@memphis.edu
 */
public class ThreadManager {//extends TimerTask {

    /**
    Service object for running threads
    */
    private final ExecutorService executor;
   // private boolean enqueueTask = true;
    private List<Runnable> workerThreads = new ArrayList<Runnable>();
    public ThreadManager(int threadSize){
        executor = Executors.newFixedThreadPool(threadSize);
    }


/**
 * 
 * @param server
 * @param socket
 * @param mutex 
 */
    public void enqueueTask(TilingServer server,Socket socket, Object mutex) {
        Runnable workerThread =  new ProcessSparseMatrix(server, socket,mutex);
        //if(isEnqueueTask()){
            workerThreads.add(workerThread); 
           // return;
       // }
        //executor.execute(workerThread);
    }
    
    public void shutDown(){
        executor.shutdown();
       // int i=0;
        while(!executor.isTerminated()){
             //System.out.println(i++);
        };
    }   


    public void executeTasks() {
       // enqueueTask = false;
        for(Runnable task: workerThreads){
            executor.execute(task);
        }
    }
//
//    /**
//     * @return the enqueueTask
//     */
//    public boolean isEnqueueTask() {
//        return enqueueTask;
//    }
}
