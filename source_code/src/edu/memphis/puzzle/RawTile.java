
package edu.memphis.puzzle;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sujit
 */
public class RawTile {
    private List<Square> squares = new ArrayList<Square>();
    
    private int left;
    private int top;
    private int right;
    private int bottom;
    
    public RawTile(){
        
    }
    
    public RawTile(int l,int t,int r,int b){
        this.left = l;
        this.top = t;
        this.right = r;
        this.bottom = b;
    }
    public void addSquare(Square sq){
        updateTileGeometry(sq);
        getSquares().add(sq);
    }

    public int getSize(){
        return getSquares().size();
    }
    
    private void updateTileGeometry(Square sq) {
        if(getSquares().isEmpty()){
            this.left = this.right = sq.getX();
            this.top = this.bottom = sq.getY();
        }else{
            if(this.getLeft() > sq.getX()){
                this.left = sq.getX();
            }
            
            if(this.getRight() < sq.getX()){
                this.right = sq.getX();
            }
            
            if(this.getTop() > sq.getY()){
                this.top = sq.getY();
            }
            
            if(this.getBottom() < sq.getY()){
                this.bottom = sq.getY();
            }
        }
    }

    /**
     * @return the left
     */
    public int getLeft() {
        return left;
    }


    /**
     * @return the top
     */
    public int getTop() {
        return top;
    }

    /**
     * @return the right
     */
    public int getRight() {
        return right;
    }

    /**
     * @return the bottom
     */
    public int getBottom() {
        return bottom;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return this.right - this.left + 1;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return this.bottom - this.top + 1;
    }

    /**
     * @return the squares
     */
    public List<Square> getSquares() {
        return squares;
    }
    
}
