package edu.memphis.clientServer;



import edu.memphis.serializable.SparseMatrix;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import edu.memphis.puzzle.Puzzle;
import edu.memphis.puzzle.SolutionBoard;
import edu.memphis.puzzle.Square;
import edu.memphis.puzzle.Tile;
import edu.memphis.serializable.DataElement;
import edu.memphis.util.TimeSpan;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nabin
 */
public class TilingServer {
  private int portNo = 3000; // Port to distributeTasks to for clients
  private ServerSocket clientConnect;
 // private List<Socket> clients = new ArrayList<Socket>();
 private List<SparseMatrix> sparseMatrices = new ArrayList<SparseMatrix>();
 private int totalWork;
 private int initialTotalWork;
 private Object mutex = new Object();
 private ThreadManager threadManager;
 private boolean isClosing;
 private int numOfClients;
 private Puzzle puzzle;
 private List<SolutionBoard> uniqueBoards = new ArrayList<SolutionBoard>();
 private long solvePuzzleStartTime;
 private long solveFirstPuzzleEndTime;
 private long solveAllPuzzleEndTime;
 private int totalSolutions;
 private List<String> outLines;
  public TilingServer(int port,int numOfClients) throws IllegalArgumentException {
    if (port <= 0) {
          throw new IllegalArgumentException(
                      "Bad port number given to SimpleServer constructor.");
      } 
    this.numOfClients = numOfClients;
    // Try making a ServerSocket to the given port
    System.out.println("Connecting server socket to port...");
    try { clientConnect = new ServerSocket(port); }
    catch (IOException e) {
      System.out.println("Failed to connect to port " + port);
      System.exit(1);
    }

    // Made the connection, so set the local port number
    this.portNo = port;
  }

  public void start(List<SparseMatrix> sparseMatrices,Puzzle puzzle){
      
    this.sparseMatrices = sparseMatrices; 
    this.totalWork = this.sparseMatrices.size();
    this.initialTotalWork = this.totalWork;
    this.puzzle = puzzle;
    this.threadManager = new ThreadManager(this.totalWork);
    System.out.println("Master Server running on port " + this.portNo + "...");
    
   // Timer timer = new Timer(); 
   // timer.schedule(this.threadManager, 5000);
    this.initializeClients();
    this.distributeTasks();
    this.threadManager.shutDown();
    this.shutDown();
    this.displayPuzzleSolutionStats();
   // timer.cancel();
    //timer.purge();
    System.out.println("Tasks Completed!...");
    System.out.println("exit!!!");
  }
  
  public static void main(String argv[]) throws InterruptedException, ClassNotFoundException {
    int port = 3000;
    if (argv.length > 0) {
      int tmp = port;
      try {
        tmp = Integer.parseInt(argv[0]);
      }
      catch (NumberFormatException e) {}

      port = tmp;
    }
    int numOfClients = 2;
    TilingServer server = new TilingServer(port,numOfClients);
    
    for(int i=0;i<5;i++){
            server.getSparseMatrices().add(SparseMatrix.createTestMatrix());
    }
    server.totalWork = server.getSparseMatrices().size();
    
    server.threadManager = new ThreadManager(server.totalWork);
    System.out.println("Master Server running on port " + port + "...");
    

    server.initializeClients();
    server.distributeTasks();
    server.threadManager.shutDown();
    server.shutDown();
    
    System.out.println("Tasks Completed!...");
    System.out.println("exit!!!");
    //System.exit(0);
  }

  public void distributeTasks()
  {
      System.out.println("Distributing Tasks...");
      this.solvePuzzleStartTime = Calendar.getInstance().getTimeInMillis();
      this.threadManager.executeTasks();
      
  }


  public synchronized void finalize() {
    System.out.println("Shutting down SimpleServer running on port "
                       + portNo);
  }

public SolutionBoard createSolutionBoard(List<DataElement> solutionNodes) {
        int id = this.getUniqueBoards().size();
        SolutionBoard b = new SolutionBoard(id,this.puzzle.getBoard().getWidth(),this.puzzle.getBoard().getHeight(),this.puzzle.getBoard().getSize());
        for(DataElement dataNode:solutionNodes){
            String[] values = dataNode.getDataValue().split("_");
            int tileId = Integer.parseInt(values[0]);
            int orientationId = Integer.parseInt(values[1]);
            int row = Integer.parseInt(values[2]);
            int column = Integer.parseInt(values[3]);
            Map<Integer,Tile> mapTile = this.puzzle.getAllTileMap().get(tileId);
            Tile t = mapTile.get(orientationId);
            b.placeTile(row,column,t);
            
        }
        Collections.sort(b.getSquares());
        return b;
    }

  public boolean isIsoMorphicSolution(SolutionBoard incomingBoard) {
       this.totalSolutions++;
       if(totalSolutions % 1000 == 0){
           System.out.println("Total Solutions so far: " + this.totalSolutions);
           this.outLines.add("Total Solutions so far: " + this.totalSolutions);
       }
       
        if(this.uniqueBoards.isEmpty()){
            this.solveFirstPuzzleEndTime = Calendar.getInstance().getTimeInMillis();
            this.solveAllPuzzleEndTime = this.solveFirstPuzzleEndTime;
            TimeSpan elaspedTime = new TimeSpan(this.solveFirstPuzzleEndTime - this.solvePuzzleStartTime);
            System.out.println("Time taken to return 1st solution: " + elaspedTime.toString());
            System.out.println("Unique Solution: " + (this.uniqueBoards.size() + 1));
            this.outLines.add("Unique Solution: " + (this.uniqueBoards.size() + 1));
            this.printSolutionBoard(incomingBoard);
            return false;
        }
        this.solveAllPuzzleEndTime = Calendar.getInstance().getTimeInMillis();
        for(SolutionBoard b: this.uniqueBoards ){
            if(b.isIsoMorphic(incomingBoard)){
                return true;
            }
        }
        System.out.println("Unique Solution: " + (this.uniqueBoards.size() + 1));
        this.outLines.add("Unique Solution: " + (this.uniqueBoards.size() + 1));
        
        if(this.uniqueBoards.size()<=1000 || this.uniqueBoards.size()%1000==0){
            this.printSolutionBoard(incomingBoard);
        } 
        return false;
    }

  private void displayPuzzleSolutionStats() {
        System.out.println("Puzzle Solution Summary:");
        System.out.println("Total Possible Solutions: " + this.totalSolutions);
        System.out.println("Total Unique Solutions: " + this.uniqueBoards.size());
        this.outLines.add("Puzzle Solution Summary:");
        this.outLines.add("Total Possible Solutions: " + this.totalSolutions);
        this.outLines.add("Total Unique Solutions: " + this.uniqueBoards.size());
        if(this.uniqueBoards.size() > 0){
            TimeSpan elaspedTime = new TimeSpan(this.solveFirstPuzzleEndTime - this.solvePuzzleStartTime);
            System.out.println("Time taken to return 1st solution :" + elaspedTime.toString());
            this.outLines.add("Time taken to return 1st solution :" + elaspedTime.toString());
            elaspedTime = new TimeSpan(this.solveAllPuzzleEndTime - this.solvePuzzleStartTime);
            System.out.println("Time taken to return all solutions :" + elaspedTime.toString());
            this.outLines.add("Time taken to return all solutions :" + elaspedTime.toString());
        }   
    }
  
  private void printSolutionBoard(SolutionBoard b){
        //System.out.println("Width: " + b.getWidth() + " Height: " + b.getHeight());
        //this.outLines.add("Width: " + b.getWidth() + " Height: " + b.getHeight());
        char tile[][] = new char[b.getHeight()][b.getWidth()];
        for(char[] tilerow: tile){
             Arrays.fill(tilerow,' ');
        }

        for(Square sq: b.getSquares()){
            tile[sq.getY()][sq.getX()] = sq.getValue();
        }
        for(int i=0;i<tile.length;i++){
            StringBuilder sb = new StringBuilder();
            for(int j=0;j<tile[0].length;j++){
                sb.append(tile[i][j]);
            }
            System.out.println(sb.toString());
            //this.outLines.add(sb.toString());
            sb.setLength(0);
        }
    }
    /**
     * @return the sparseMatrices
     */
    public List<SparseMatrix> getSparseMatrices() {
        return sparseMatrices;
    }


    void decreaseNumberOfClients() {
        this.numOfClients--;
    }


    
    public void shutDown(){
        if(!this.clientConnect.isClosed())
        {
            try {
                System.out.println("Closing the server socket...");
                isClosing = true;
                this.clientConnect.close();
            } catch (IOException ex) {
                Logger.getLogger(TilingServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    /**
     * @return the clients
     */
    public int getClients() {
        return numOfClients;
    }

    private void initializeClients() {
        int clients = 0;
        while(clients < this.numOfClients){
            try {
               Socket clientReq = clientConnect.accept();
               System.out.println("Got a worker process!");
               this.threadManager.enqueueTask(this, clientReq, mutex);
               clients++;
            } catch (IOException ex) {
                if(!isClosing){
                        System.out.println("IO exception while listening for clients.");
                        System.exit(1);
                } 
            }
            
            
        }
    }

    /**
     * @return the uniqueBoards
     */
    public List<SolutionBoard> getUniqueBoards() {
        return uniqueBoards;
    }


    /**
     * @param outLines the outLines to set
     */
    public void setOutLines(List<String> outLines) {
        this.outLines = outLines;
    }

    /**
     * @return the initialTotalWork
     */
    public int getInitialTotalWork() {
        return initialTotalWork;
    }
}
