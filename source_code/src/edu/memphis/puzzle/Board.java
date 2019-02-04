
package edu.memphis.puzzle;

import edu.memphis.enums.Orientation;
import edu.memphis.util.TransformationUtil;
import java.util.Collections;

/**
 *
 * @author nabin
 */
public class Board extends BaseTile{
    
    public Board(int id,RawTile rawTile){
        this.id = id;
        this.rawTile = rawTile;
        this.width = rawTile.getWidth();
        this.height = rawTile.getHeight();
        this.size = rawTile.getSize();
        processRawBoard();
    }

    
    private void processRawBoard() {
        Orientation o = Orientation.DEFAULT_0;

        for(Square sq:rawTile.getSquares()){
            //transform rawTile to origin
            int x = sq.getX() - rawTile.getLeft();
            int y = sq.getY() - rawTile.getTop();
            char value = sq.getValue();

            //transform    
            x = TransformationUtil.GetX(x, y, height, width, o);
            y = TransformationUtil.GetY(x, y, height, width, o);

            this.getSquares().add(new Square(x,y,value));
        }

        this.setSize(this.getSquares().size());
            
        //sort the squares in the tile
        Collections.sort(this.getSquares());
    }
    
    public boolean containsSquareAt(int row, int column) {
        for(Square sq:this.squares){
            if(sq.getX() == column && sq.getY() == row){
                return true;
            }
        }
        return false;
    }
    
    public boolean containsSameSquareAt(int row, int column, char ch){
        for(Square sq:this.squares){
            if(sq.getX() == column && sq.getY() == row && sq.getValue() == ch){
                return true;
            }
        }
        return false;
    }
    
    public boolean canTileBePlaced(int row, int column, Tile t) {
        
        for(Square sq:t.getSquares()){
            int sqX = column + sq.getX();
            int sqY = row + sq.getY();
            if(!this.containsSameSquareAt(sqY, sqX,sq.getValue())){
            //if(!this.containsSquareAt(sqY, sqX)){
                return false;
            }
        }
        
        return true;
    }  
}
