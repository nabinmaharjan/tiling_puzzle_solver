
package edu.memphis.tilingSolver;

import edu.memphis.clientServer.TilingServer;
import edu.memphis.enums.OrientationMode;
import edu.memphis.parser.TilingParser;
import edu.memphis.puzzle.Board;
import edu.memphis.puzzle.Puzzle;
import edu.memphis.puzzle.SolutionBoard;
import edu.memphis.puzzle.Square;
import edu.memphis.puzzle.Tile;
import edu.memphis.serializable.DataElement;
import edu.memphis.serializable.SparseMatrix;
import edu.memphis.serializable.SparseRow;
import edu.memphis.util.FileWriterUtil;
import edu.memphis.util.TimeSpan;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nabin
 */
public class TilingPuzzleSolver extends DancingLinkSolver{

    private int width;
    private Puzzle puzzle;
    private int totalSolutions;
    private long solvePuzzleStartTime;
    private long solveFirstPuzzleEndTime;
    private long solveAllPuzzleEndTime;
    private TilingServer server;
    private List<String> outLines = new ArrayList<String>();
    private List<SolutionBoard> uniqueBoards = new ArrayList<SolutionBoard>();
    public TilingPuzzleSolver(int width, int height,Puzzle puzzle){
        this.width = width;
        this.puzzle = puzzle;
    }
    
    public TilingPuzzleSolver(Puzzle puzzle){
        this.puzzle = puzzle;
        this.width = puzzle.getUniqueTileCount() + puzzle.getBoard().getSize();
    }
    
    public void recordOutLines(String s){
        getOutLines().add(s);
    }
    
    public void recordOutLines(List<String> s){
        getOutLines().addAll(s);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
       
        System.out.println("Number of arguments provided: " + args.length);
        if(args.length != 3 && args.length != 6){
            String message = "Invalid number of arguments specified!";
            displayErrorMessage(message);
            return;
        }
        
        String puzzleFile = args[0];
        boolean distributedMode = Boolean.parseBoolean(args[1]);
        OrientationMode om = (OrientationMode)Enum.valueOf(OrientationMode.class, args[2].toUpperCase());
        int exploreLevel = 1;
        int numOfClientInstances = 2;
        int port = 3000;
        if(args.length == 3 && distributedMode){
            String message = String.format("Using default values: exploreLevel: %d,numOfClientInstance: %d,portNo: %d",exploreLevel,numOfClientInstances,port);
            System.out.println(message);
        }
        
        if(args.length ==6 && !distributedMode){
            System.out.println("The program is running in single instance mode. Ignoring last three arguments...");
        }

        if(args.length == 6 && distributedMode){
            exploreLevel = Integer.parseInt(args[3]);
            numOfClientInstances = Integer.parseInt(args[4]);
            port = Integer.parseInt(args[5]);
            if(port < 3000){
                port = 3000;
                System.out.println("Smaller value specified for server port. Changing the port to " + port);
            }
        }
        
        List<String> outLines = new ArrayList<String>();
        Puzzle puzzle = new Puzzle();
       // OrientationMode om = OrientationMode.ALL;
        //OrientationMode om = OrientationMode.ROTATION;
        //OrientationMode om = OrientationMode.REFLECTION;
        //OrientationMode om = OrientationMode.DEFAULT;
        
        System.out.println("Solution Mode: " + om.toString());
        outLines.add("Solution Mode: "+ om.toString());
        outLines.add("Puzzle Input File: " + puzzleFile);
        TilingParser parser = new TilingParser();
        parser.parse(puzzleFile);
        //set the Board
        puzzle.setBoard(parser.getRawBoard());  
        outLines.add("Board Size: " + puzzle.getBoard().getSize());
        outLines.add(String.format("Board Dimenstion: %d x %d",puzzle.getBoard().getWidth(),puzzle.getBoard().getHeight()));
        //set tempTiles with all possible orientations
        puzzle.setAllTiles(parser.getRawTiles(),om);
        
        puzzle.aliasIsoMorphicTiles();
        
        //set unique tiles count
        puzzle.setUniqueTileCount(parser.getRawTiles().size());
        outLines.add("Total unique tiles: " + puzzle.getUniqueTileCount());
        outLines.add("Total number of tiles with different orientations: "+puzzle.getTiles().size());
        puzzle.recordBoardAndTile(outLines);
        //create instance of TilingPuzzleSolver
        String pFileName = puzzleFile.substring(puzzleFile.lastIndexOf("/")+1);
        String outFile = "puzzleOutput_" + pFileName +"_" + om.toString()+"_"+numOfClientInstances+".txt";
        TilingPuzzleSolver solver = new TilingPuzzleSolver(puzzle);
        solver.recordOutLines(outLines);
        if(puzzle.isSolvable()){
            

           //for testing uncomment this and comment the next two lines
           //solver.createSparseMatrix();
           solver.createColumnHeaders();
           solver.createSparseMatrix2();
           
            if(distributedMode){ 
                //build partial solution trees with number of solution node = exploreLevel
                solver.solve(0,exploreLevel); 
                
                //solver.printPartialTree();

                //distribute the partial solution trees to client nodes
                solver.startTilingServer(port,numOfClientInstances);
                FileWriterUtil.writeToFile(outFile, solver.outLines);
                
            }
            else{
                solver.initializeStartTime();
                solver.solve(0,-1);
                solver.displayPuzzleSolutionStats();
                FileWriterUtil.writeToFile(outFile, solver.outLines);
            }  
        }
        else{
            System.out.println("Given puzzle is not solvable. Num of squares in board should be equal to sum of number of squares in all given tile pieces!");
            solver.recordOutLines("Given puzzle is not solvable. Num of squares in board should be equal to sum of number of squares in all given tile pieces!");
            FileWriterUtil.writeToFile(outFile, solver.outLines);
        } 
    }

    private void createSparseMatrix() {
        int[][] rows = { 
            {0,0,1,0,1,1,0},
            {1,0,0,1,0,0,1},
            {0,1,1,0,0,1,0},
            {1,0,0,1,0,0,0},
            {0,1,0,0,0,0,1},
            {0,0,0,1,1,0,1}
        };
        
        this.width = 7;
        for(int i=0;i<7;i++){
            this.createColumnHeader(i);
        }
        
        int rowIndex=0;
        for(int i=0;i<rows.length;i++){
//            Node rowHeader= new Node();
//            rowHeader.setValue(-1);
//            this.addNodeToRowHeader(rowHeader);
            Node startingRowNode = null;
            boolean firstDataNode = true;
            for(int j=0;j<rows[0].length;j++){
                if(rows[i][j] == 1){
                    Node dataNode = new Node();
                    //store location row,column  as the data value
                    int location = i * this.width + j;
                    dataNode.setValue(String.valueOf(location));
                    dataNode.setRowIndex(rowIndex);
                    ColumnHeader colHeader = this.getColumnHeader(j);
                    colHeader.appendDataNode(dataNode);
                    if(firstDataNode){
                        startingRowNode = dataNode;
                        firstDataNode = false;
                    }
                    else{
                        dataNode.right = startingRowNode;
                        dataNode.left = startingRowNode.left;
                        
                        startingRowNode.left.right = dataNode;
                        startingRowNode.left = dataNode;
                    }
                    
                }
            }
            rowIndex++;
        }

    }

    @Override
    public void printSolution() {
        this.totalSolutions++;
        if(totalSolutions==1){
            this.solveFirstPuzzleEndTime = Calendar.getInstance().getTimeInMillis();
            this.solveAllPuzzleEndTime = this.solveFirstPuzzleEndTime;
            TimeSpan elaspedTime = new TimeSpan(this.solveFirstPuzzleEndTime - this.solvePuzzleStartTime);
            System.out.println("Time taken to return 1st solution: " + elaspedTime.toString());
            this.recordOutLines("Time taken to return 1st solution: " + elaspedTime.toString());
            //this.printSolutionTiles();
            SolutionBoard b = this.createSolutionBoard();
            this.printSolutionBoard(b);
            b.buildIsoMorphs();
            this.uniqueBoards.add(b);
            
            return;
        }
        this.solveAllPuzzleEndTime = Calendar.getInstance().getTimeInMillis();
        SolutionBoard b = this.createSolutionBoard();
       // System.out.println("--------------------------");
        //this.printSolutionBoard(b);
        if(!this.isIsoMorphicSolution(b)){
            System.out.println("Unique Solution: " + (this.uniqueBoards.size() + 1));
            this.recordOutLines("Unique Solution: " + (this.uniqueBoards.size() + 1));
            this.printSolutionBoard(b);
            this.uniqueBoards.add(b);
            b.buildIsoMorphs();
        }
       // this.printSolutionTiles();
    }

    private void createSparseMatrix2() {
        Board board = this.puzzle.getBoard();
        int boardHeight = board.getHeight();
        int boardWidth = board.getWidth();
        int totalRow =0;
        int rowIndex = 0;
        for(Tile t:this.puzzle.getTiles()){
            for(int row=0;row<boardHeight;row++){
                for(int column=0;column<boardWidth;column++){
                    if(board.canTileBePlaced(row,column,t)){
                        createMatrixRow(rowIndex++,row,column,t);
                        totalRow ++;
                    }
                }
            }
        }
        System.out.println("TotalRowsCreated:"+totalRow);
        this.recordOutLines("TotalRowsCreated:"+totalRow);
    }


    private void createMatrixRow(int rowIndex,int row, int column, Tile t) {
        Node rowHeader = new Node();
        String data = String.format("%d_%d_%d_%d",t.getId(),t.getOrientationId(),row,column);
        rowHeader.setValue(data);
        rowHeader.setRowIndex(rowIndex);
       // System.out.println(data);
        ColumnHeader colHeader = this.getColumnHeader(t.getId());
        colHeader.appendDataNode(rowHeader);
        int boardWidth = this.puzzle.getBoard().getWidth();
        int uniqueTileCount = puzzle.getUniqueTileCount();
        List<Square> squares = t.getSquares();
        //System.out.println("Squares: "+ t.getSquares().size());
        for(int i=0;i<squares.size();i++){
            Node dataNode = new Node();
            dataNode.setValue(data);
            dataNode.setRowIndex(rowIndex);
            Square sq = squares.get(i);
            int sqPosX = column + sq.getX();
            int sqPosY = row + sq.getY();
            int location = sqPosY * boardWidth + sqPosX;
            //System.out.println("row: " + row + " sqY: " + sq.getY() + " column: " + column + "sqX: " + sq.getX());
            //System.out.println("sqPosX: " + sqPosX);
            int columnId = uniqueTileCount + location;
            //System.out.println("Data ColumnId:"+ columnId);
            //System.out.println("ColumnId: " + columnId);
            ColumnHeader header = this.getColumnHeader(columnId);
            
            header.appendDataNode(dataNode);
            dataNode.right = rowHeader;
            dataNode.left = rowHeader.left;

            rowHeader.left.right = dataNode;
            rowHeader.left = dataNode;
        }   
        //System.out.println("fadsssssss");
    }

    private void createColumnHeaders() {
        
         //create column headers for each tile type
        int uniqueTileCount = puzzle.getUniqueTileCount();
        for(int i=0;i<uniqueTileCount;i++){
            this.createColumnHeader(i);
        }
        
        Board board = puzzle.getBoard();
        //create column headers for each data of the board
        int boardWidth = board.getWidth();
        int boardHeight = board.getHeight();
        int boardSize = board.getSize();
        
        if(boardSize == (boardWidth * boardHeight)){
            for(int i=this.puzzle.getUniqueTileCount();i< this.width;i++){
                this.createColumnHeader(i);
            }
        }
        else{
            for(int row=0;row<boardHeight;row++){
                for(int column=0;column<boardWidth;column++){
                    if(board.containsSquareAt(row,column)){
                        int location = row * boardWidth + column;
                        int columnId = this.puzzle.getUniqueTileCount() + location;
                        this.createColumnHeader(columnId);
                    }
                }
             }
        }
    }

    private void displayPuzzleSolutionStats() {
        System.out.println("Puzzle Solution Summary:");
        System.out.println("Total Possible Solutions: " + this.totalSolutions);
        System.out.println("Total Unique Solutions: " + this.uniqueBoards.size());
        this.recordOutLines("Puzzle Solution Summary:");
        this.recordOutLines("Total Possible Solutions: " + this.totalSolutions);
        this.recordOutLines("Total Unique Solutions: " + this.uniqueBoards.size());
        
        if(this.uniqueBoards.size() > 0){
            TimeSpan elaspedTime = new TimeSpan(this.solveFirstPuzzleEndTime - this.solvePuzzleStartTime);
            System.out.println("Time taken to return 1st solution :" + elaspedTime.toString());
            this.recordOutLines("Time taken to return 1st solution :" + elaspedTime.toString());
            elaspedTime = new TimeSpan(this.solveAllPuzzleEndTime - this.solvePuzzleStartTime);
            System.out.println("Time taken to return all solutions :" + elaspedTime.toString());
            this.recordOutLines("Time taken to return all solutions :" + elaspedTime.toString());
        }
        
    }

    /**
     * @return the outLines
     */
    public List<String> getOutLines() {
        return outLines;
    }

    private void initializeStartTime() {
        this.solvePuzzleStartTime = Calendar.getInstance().getTimeInMillis();
    }


    private void printPartialTree() {
        for(SparseMatrix sm: this.getSparseMatrixList()){
            System.out.println("Header Columns...");
            for(Integer columnId:sm.getHeaders()){
                System.out.println(columnId);
            }
            System.out.println("Header Columns...");
            System.out.println("Sparse Rows...");
            for(SparseRow row:sm.getRows()){
                StringBuilder sb = new StringBuilder();
                sb.append(row.getRowIndex());
                for(DataElement elem: row.getDataNodes()){
                    sb.append(" ").append(elem.getColHeader()).append(":").append(elem.getDataValue());
                }
                System.out.println(sb.toString());
            }
            System.out.println("Sparse Rows...");
            System.out.println("Partial Solutions...");
            StringBuilder sb = new StringBuilder();
            for(DataElement elem:sm.getSolutionNodes()){
                sb.append(" ").append(elem.getColHeader()).append(":").append(elem.getDataValue());
            }
            System.out.println(sb.toString());
            System.out.println("Partial Solutions...");
        }
    }

    private void startTilingServer(int port,int numOfClients) {
        System.out.println("Number of sub-problems: " + this.getSparseMatrixList().size());
        this.recordOutLines("Number of sub-problems: " + this.getSparseMatrixList().size());
        server = new TilingServer(port,numOfClients);
        server.setOutLines(this.outLines);
        server.start(this.getSparseMatrixList(),this.puzzle);
    }

    private void printSolutionBoard(SolutionBoard b){
        System.out.println("Width: " + b.getWidth() + " Height: " + b.getHeight());
        System.out.println("Width: " + b.getWidth() + " Height: " + b.getHeight());
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
            this.recordOutLines(sb.toString());
            sb.setLength(0);
        }
    }
    private SolutionBoard createSolutionBoard() {
        int id = this.uniqueBoards.size();
        SolutionBoard b = new SolutionBoard(id,this.puzzle.getBoard().getWidth(),this.puzzle.getBoard().getHeight(),this.puzzle.getBoard().getSize());
        
        for (Iterator<Node> it = solutions.iterator(); it.hasNext();) {
            Node node = it.next();
            String[] values = node.getValue().split("_");
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

    private void printSolutionTiles() {
        System.out.println("Solution No:" + this.totalSolutions);
        System.out.println("Start");
        this.recordOutLines("Solution No:" + this.totalSolutions);
        this.recordOutLines("Start");
        for (Iterator<Node> it = solutions.iterator(); it.hasNext();) {
            Node node = it.next();
            System.out.println(node.getValue());
            this.recordOutLines(node.getValue());  
        }
        System.out.println("end");
        this.recordOutLines("end");
    }

    private boolean isIsoMorphicSolution(SolutionBoard incomingBoard) {
        for(SolutionBoard b: this.uniqueBoards ){
            if(b.isIsoMorphic(incomingBoard)){
                return true;
            }
        }
        return false;
    }

    private static void displayErrorMessage(String message) {
        System.out.println(message);
        System.out.println("Usage:");
        System.out.println("java -cp TilingSolver.jar edu.memphis.tilingSolver.TilingPuzzleSolver <puzzleFileName> <distributedMode> <orientationMode> [exploreLevel] [numOfClientInstance] [portNo]");
        System.out.println("<puzzleFileName>: required. value is path of puzzle file");
        System.out.println("<distributedMode>: required. value is any of {true, false}");
        System.out.println("<orientationMode>: required. value is any of {default, all,reflection,rotation}");
        System.out.println("[exploreLevel]: required only if distributedMode is true. value is any positive integer value usually 1,2,3");
        System.out.println("[numOfClientInstance]: required only if distributedMode is true. value is any positive integer value");
        System.out.println("[portNo]: required only if distributedMode is true. value is any higher integer value higher or equal to 3000 for server port");

    }

  
}
