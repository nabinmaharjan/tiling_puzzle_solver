package edu.memphis.clientServer;


import edu.memphis.puzzle.SolutionBoard;
import edu.memphis.serializable.Solution;
import edu.memphis.serializable.SparseMatrix;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;


/**
 *
 * @author nabin
 */
public class ProcessSparseMatrix implements Runnable{

    private final Object mutex;
    private final TilingServer server;
    private final Socket clientConn;
    public ProcessSparseMatrix(TilingServer server,Socket socket,Object mutex){
        this.mutex = mutex;
        this.server = server;
        this.clientConn = socket;
    }
    
    @Override
    public void run() 
    {
        try 
            {
              
        ObjectOutputStream out= new ObjectOutputStream(clientConn.getOutputStream());
        ObjectInputStream in= new ObjectInputStream(clientConn.getInputStream());
        while(true)
        {

             
                SparseMatrix matrix = null;
                //ObjectOutputStream out;
                synchronized(mutex){
                    if(this.server.getSparseMatrices().isEmpty()){
                        //send empty sparse matrix to indicate no more work are remaining to client
                        //out = new ObjectOutputStream(clientConn.getOutputStream());
                        out.writeObject(new SparseMatrix());
                        this.server.decreaseNumberOfClients();
                        System.out.println("sending no work remaining status to the client...");
                        System.out.println("Remaining working clients: " + this.server.getClients());
                        return;
                    }
                    matrix =  this.server.getSparseMatrices().get(0);
                    this.server.getSparseMatrices().remove(matrix);
                    System.out.println("Distributing job: " + (this.server.getInitialTotalWork() - this.server.getSparseMatrices().size()));
                 }
                
                 //out = new ObjectOutputStream(clientConn.getOutputStream());
                out.writeObject(matrix);

               // ObjectInputStream in = new ObjectInputStream(clientConn.getInputStream());
                List<Solution> solution = (List<Solution>)in.readObject();
                while(solution.size() > 0){
                    
                    for(Solution s:solution){
                        synchronized(mutex){
                            SolutionBoard b = this.server.createSolutionBoard(s.getSolutionNodes());
                            if(!this.server.isIsoMorphicSolution(b)){
                                b.buildIsoMorphs();
                                this.server.getUniqueBoards().add(b);
                            }  
                        }
                    } 
                    //in = new ObjectInputStream(clientConn.getInputStream());
                    solution = (List<Solution>)in.readObject();  
                }
                            
        }  
        }
            catch (ClassNotFoundException ex) {
                        //Logger.getLogger(ProcessSparseMatrix.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error while deserializing List<Solution>");
                ex.printStackTrace(System.out);
                    }
            catch (IOException e) {
              System.out.println("TilingServer: Error getting I/O streams.");
              e.printStackTrace(System.out);
              //break;
            }
    }
}
    
