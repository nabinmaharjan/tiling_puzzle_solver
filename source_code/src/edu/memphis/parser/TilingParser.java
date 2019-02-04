
package edu.memphis.parser;

import edu.memphis.puzzle.RawTile;
import edu.memphis.puzzle.Square;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author sujit
 */
public class TilingParser {
    
    private List<RawTile> rawTiles = new ArrayList<RawTile>();
    private RawTile rawBoard;
    public TilingParser(){
        
    }
    
    public List<RawTile> getRawTiles(){
        return rawTiles;
    }
    
    public RawTile getRawBoard(){
        return rawBoard;
    }
    
    public void parse(String fileName){
        
        ArrayList<char[]> charInputMatrix = new ArrayList<char[]>();

        BufferedReader reader =null;
        try {
                reader = new BufferedReader(new FileReader(new File(fileName)));
                String line = null;
                while ((line = reader.readLine()) != null) {
                        charInputMatrix.add(line.toCharArray());
            } 
        }
        catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        finally {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
        
        int i, j;
        for (j = 0; j < charInputMatrix.size(); j++) {
                for (i = 0; i < charInputMatrix.get(j).length; i++) {
                        if (charInputMatrix.get(j)[i] != ' ') {
                                // detect & explore raw rawTile and clear the points using ' '
                                RawTile rawTile = new RawTile();
                                explore(charInputMatrix, i, j, rawTile);
                                this.rawTiles.add(rawTile); 
                        }
                }
        }
        
        //for testing
       // this.createTestBoardAndTiles();
        separateBoardAndTiles();
    }
    
    private void createTestBoardAndTiles()
    {
        RawTile rawTile = new RawTile(0,0,3,1);
        
        List<Square> squares = new ArrayList<Square>();
        squares.add(new Square(0,0,'*'));
        squares.add(new Square(1,0,'*'));
        squares.add(new Square(2,0,'*'));
        squares.add(new Square(3,0,'*'));
        squares.add(new Square(1,1,'*'));
        
        rawTile.getSquares().addAll(squares);
        
        RawTile rawTile2 = new RawTile(0,1,1,2);
        
        List<Square> squares2 = new ArrayList<Square>();
        squares2.add(new Square(0,1,'*'));
        squares2.add(new Square(0,2,'*'));
        squares2.add(new Square(1,2,'*'));
        
        rawTile2.getSquares().addAll(squares2);
        
        RawTile rawTile3 = new RawTile(2,1,3,2);
        
        List<Square> squares3 = new ArrayList<Square>();
        squares3.add(new Square(2,1,'*'));
        squares3.add(new Square(3,1,'*'));
        squares3.add(new Square(2,2,'*'));
        squares3.add(new Square(3,2,'*'));
        
        rawTile3.getSquares().addAll(squares3);
        
        RawTile rawTile4 = new RawTile(0,3,3,3);
        
        List<Square> squares4 = new ArrayList<Square>();
        squares4.add(new Square(0,3,'*'));
        squares4.add(new Square(1,3,'*'));
        squares4.add(new Square(2,3,'*'));
        squares4.add(new Square(3,3,'*'));
        
        rawTile4.getSquares().addAll(squares4);
        
        RawTile rawTile5 = new RawTile(0,0,3,3);
        
        List<Square> squares5 = new ArrayList<Square>();
        squares5.add(new Square(0,0,'*'));
        squares5.add(new Square(0,1,'*'));
        squares5.add(new Square(0,2,'*'));
        squares5.add(new Square(0,3,'*'));
        squares5.add(new Square(1,0,'*'));
        squares5.add(new Square(1,1,'*'));
        squares5.add(new Square(1,2,'*'));
        squares5.add(new Square(1,3,'*'));
        squares5.add(new Square(2,0,'*'));
        squares5.add(new Square(2,1,'*'));
        squares5.add(new Square(2,2,'*'));
        squares5.add(new Square(2,3,'*'));
        squares5.add(new Square(3,0,'*'));
        squares5.add(new Square(3,1,'*'));
        squares5.add(new Square(3,2,'*'));
        squares5.add(new Square(3,3,'*'));
        
        rawTile5.getSquares().addAll(squares5);
        
        this.rawTiles.add(rawTile);
        this.rawTiles.add(rawTile2);
        this.rawTiles.add(rawTile3);
        this.rawTiles.add(rawTile4);
        this.rawTiles.add(rawTile5);
    }
    
    public void separateBoardAndTiles(){
        
        //find the tile with the largest size
        RawTile rawTile = null;
        int size = 0;
        for(RawTile tile : rawTiles){
            if(size < tile.getSize()){
                size = tile.getSize();
                rawTile = tile;
            }
        }
        
        //set it to the rawBoard
        this.rawBoard = rawTile;
        
        //remove it from the raw tile list
        
        this.rawTiles.remove(rawTile);
        
    }

    private void explore(ArrayList<char[]> charMatrix, int x, int y, RawTile rawTile) {
        rawTile.addSquare(new Square(x,y,charMatrix.get(y)[x])); 
        charMatrix.get(y)[x] = ' ';

        if (withinBound(charMatrix, x - 1, y) && charMatrix.get(y)[x - 1] != ' ') {
                explore(charMatrix, x - 1, y, rawTile);
        }

        if (withinBound(charMatrix, x, y - 1) && charMatrix.get(y-1)[x] != ' ') {
                explore(charMatrix, x, y - 1, rawTile);
        }
        if (withinBound(charMatrix, x, y + 1) && charMatrix.get(y+1)[x] != ' ') {
                explore(charMatrix, x, y + 1, rawTile);
        }


        if (withinBound(charMatrix, x + 1, y) && charMatrix.get(y)[x + 1] != ' ') {
                explore(charMatrix, x + 1, y, rawTile);
        }

    }

    private boolean withinBound(ArrayList<char[]> charMatrix, int x, int y) {
        return y >= 0 && y < charMatrix.size() && x >= 0 && x < charMatrix.get(y).length;
    }
    
    public void printTilesAndBoard(){
        System.out.println("Print Board...!") ;
        this.print(this.getRawBoard());
        
        System.out.println("Printing Tiles...!");
        
        for(int i=0;i< this.rawTiles.size();i++){
            System.out.println("Tile: " + (i+1)) ;
            this.print(rawTiles.get(i));
        }
            
    }
    
    private void print(RawTile rawTile){
        System.out.println("Width: " + rawTile.getWidth() + " Height: " + rawTile.getHeight());
        char tile[][] = new char[rawTile.getHeight()][rawTile.getWidth()];
        for(char[] tilerow: tile){
             Arrays.fill(tilerow,' ');
        }

        for(Square sq: rawTile.getSquares()){
            tile[sq.getY()-rawTile.getTop()][sq.getX()-rawTile.getLeft()] = sq.getValue();
        }
        for(int i=0;i<tile.length;i++){
            StringBuilder sb = new StringBuilder();
            for(int j=0;j<tile[0].length;j++){
                sb.append(tile[i][j]);
            }
            System.out.println(sb.toString());
            sb.setLength(0);
        }
    }
    
    public static void main(String args[]){
        String fileName = "D:\\Nabin\\UOM\\TilingSolver\\puzzles\\test\\testPuzzle5";
        //String fileName = "C:\\Users\\nabin\\Downloads\\puzzles\\puzzles\\testBoard.txt";
        TilingParser parser = new TilingParser();
        parser.parse(fileName);
        parser.printTilesAndBoard();
    }
}
