
package edu.memphis.puzzle;

import edu.memphis.enums.OrientationMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nabin
 */
public class Puzzle {
    
    private List<Tile> tiles= new ArrayList<Tile>();
    private Map<Integer,Map<Integer,Tile>> allTileMap = new HashMap<Integer,Map<Integer,Tile>>();
    
    private Board board;
    private int uniqueTileCount;
    
    public Puzzle(){
        
    }
    

    public int getWidth(){
        return this.board.getWidth();
    }
    
    public int getHeight(){
        return this.board.getHeight();
    }
    
    /**
     * @return the tempTiles
     */
    public List<Tile> getTiles() {
        if(tiles.isEmpty()){
           for(Integer key:getAllTileMap().keySet()){
               Map<Integer,Tile> tileMap = getAllTileMap().get(key);
               for(Integer tileKey: tileMap.keySet()){
                   tiles.add(tileMap.get(tileKey));
               }
           }
        }
        return tiles;
    }

    /**
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @param board the board to set
     */
    private void setBoard(Board board) {
        this.board = board;
    }

    public void setBoard(RawTile rawBoard) {
         this.setBoard(new Board(0,rawBoard));   
    }


    public void setAllTiles(List<RawTile> rawTiles, OrientationMode o) {
       List<Tile> tempTiles = new ArrayList<Tile>();
       for(int i=0;i<rawTiles.size();i++){
            RawTile rawTile = rawTiles.get(i);
            tempTiles.add(new Tile(i,rawTile,o));
        }
       
       //get all orientations
       for(Tile t: tempTiles){
           Map<Integer,Tile> mapTile = new HashMap<Integer,Tile>();
           for(Tile to:t.getOrientations()){
               //this.tiles.add(to);
               mapTile.put(to.getOrientationId(), to);
           }
            getAllTileMap().put(t.getId(), mapTile);
       }
    }

    /**
     * @return the uniqueTileCount
     */
    public int getUniqueTileCount() {
        return uniqueTileCount;
    }

    /**
     * @param uniqueTileCount the uniqueTileCount to set
     */
    public void setUniqueTileCount(int uniqueTileCount) {
        this.uniqueTileCount = uniqueTileCount;
    }

    public void recordBoardAndTile(List<String> outLines) {
        
        outLines.add("Board: ");
        this.board.recordTileSquare(outLines);
        
        for(Integer key:getAllTileMap().keySet()){
            Tile t = getAllTileMap().get(key).get(0);
            outLines.add("Tile " + t.getId() +": ");
            t.recordTileSquare(outLines);
        }
    }

    public boolean isSolvable(){
        
        int numOfTileSquares = 0;
        for(Integer key:getAllTileMap().keySet()){
            Tile t = getAllTileMap().get(key).get(0);
            numOfTileSquares += t.getSquares().size();
        }
        
        int numOfBoardSquares = this.board.getSize();
        //TODO: handle case when num of squares in board is less than sum of number of squares in all given tile pieces
        return numOfTileSquares == numOfBoardSquares;
    }

    /**
     * @return the allTileMap
     */
    public Map<Integer,Map<Integer,Tile>> getAllTileMap() {
        return allTileMap;
    }

    public void aliasIsoMorphicTiles() {
           for(int i=0; i< allTileMap.size();i++){
               //get default tile in the puzzle
               Tile t = allTileMap.get(i).get(0);
               for(int j=i+1;j<allTileMap.size();j++){
                   Tile t1 = new Tile();
                   for(Tile temp: allTileMap.get(j).values()){
                       if(t1.getOrientations().isEmpty()){
                           t1.setId(temp.getId());
                           t1.setAliasId(temp.getAliasId());
                       }
                       t1.getOrientations().add(temp);
                   }
                   
                   if(t1.isIsoMorphic(t)){
                       if(t.getAliasId() < t1.getAliasId()){
                           for(Tile temp: allTileMap.get(j).values()){
                               temp.setAliasId(t.getAliasId());
                           }     
                       }else{
                           for(Tile temp: allTileMap.get(i).values()){
                               temp.setAliasId(t1.getAliasId());
                           }
                       }
                   }
               }
               
           }
    }

    
}
