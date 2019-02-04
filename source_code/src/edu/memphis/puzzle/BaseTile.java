
package edu.memphis.puzzle;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author nabin
 */
public abstract class BaseTile {
    protected List<Square> squares = new ArrayList<Square>(); 
    protected RawTile rawTile;
    protected int width;
    protected int height;
    protected int size;
    protected int id;
    
    
     /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    
    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }


    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the squares
     */
    public List<Square> getSquares() {
        return squares;
    }
 
     /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    public void recordTileSquare(List<String> outLines){
        char tile[][] = new char[this.height][this.width];
        for(char[] tilerow: tile){
             Arrays.fill(tilerow,' ');
        }

        for(Square sq: this.getSquares()){
            tile[sq.getY()][sq.getX()] = sq.getValue();
        }
        for(int i=0;i<tile.length;i++){
            StringBuilder sb = new StringBuilder();
            for(int j=0;j<tile[0].length;j++){
                sb.append(tile[i][j]);
            }
            outLines.add(sb.toString());
            sb.setLength(0);
        }
    }
}
