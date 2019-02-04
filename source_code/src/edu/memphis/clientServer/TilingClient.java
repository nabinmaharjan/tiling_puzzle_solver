package edu.memphis.clientServer;



import edu.memphis.serializable.SparseMatrix;
import edu.memphis.serializable.Solution;
import edu.memphis.tilingSolver.PuzzleSolvingWorker;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nabin
 */
public class TilingClient {
    // Our socket connection to the server
  protected Socket serverConn;

  public TilingClient(String host, int port)
      throws IllegalArgumentException {
    try {
      System.out.println("Trying to connect to " + host + " " + port);
      serverConn = new Socket(host, port);
    }
    catch (UnknownHostException e) {
      throw new IllegalArgumentException("Bad host name given.");
    }
    catch (IOException e) {
      System.out.println("Tiling Client: " + e);
      System.exit(1);
    }

    System.out.println("Made server connection.");
  }

  public static void main(String argv[]) throws ClassNotFoundException {
    if (argv.length < 2) {
      System.out.println("Usage: java TilingClient [host] [port]");
      System.exit(1);
    }
    
    String host = argv[0];
    int port = 3000;
    try {
      port = Integer.parseInt(argv[1]);
    }
    catch (NumberFormatException e) {}
    
    TilingClient client = new TilingClient(host, port);
    client.getWork();
  }
  
    @Override
  public synchronized void finalize() {
    System.out.println("Closing down TilingClient...");
    try { serverConn.close(); }
    catch (IOException e) {
      System.out.println("TilingClient: " + e);
      System.exit(1);
    }
  }

    private void getWork() throws ClassNotFoundException {
        try {

                ObjectInputStream in = new ObjectInputStream(serverConn.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(serverConn.getOutputStream());
                while(true){
                    System.out.println("Obtaining sub-problem...");
                    SparseMatrix deSerializedMatrix = (SparseMatrix)in.readObject();
                    //System.out.println("Deserialized Data: \n" + deSerializedMatrix.getHeaders().size());
                    if(deSerializedMatrix.getSolutionNodes().isEmpty()){
                        return;
                    }
                    PuzzleSolvingWorker worker = new PuzzleSolvingWorker(deSerializedMatrix,out);
                    worker.work();

                    //send empty solution list to indicate end of work
                    List<Solution> dataNodes = new ArrayList<Solution>(); 
                    out.writeObject(dataNodes);
               }
               
            // this.getWork();
       }
    catch (IOException e) {
      System.out.println("TilingClient: " + e); 
      System.exit(1);
    }
    }
}
