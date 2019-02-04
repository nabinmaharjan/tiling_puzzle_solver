
package edu.memphis.puzzle;

import edu.memphis.enums.Orientation;
import edu.memphis.enums.OrientationMode;
import edu.memphis.util.TransformationUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author nabin
 */
public class Tile extends BaseTile{
     
    private List<Tile> orientations = new ArrayList<Tile>();
    private int orientationId;
    protected OrientationMode orientationMode;
    private int aliasId;
    public Tile(){
        
    }
    
    public Tile(int id,RawTile rawTile,OrientationMode o){
        this.id = id;
        this.aliasId = id;
        this.rawTile = rawTile;
        this.width = rawTile.getWidth();
        this.height = rawTile.getHeight();
        this.size = rawTile.getSize();
        this.orientationMode = o;
        processRawTile();
    }
    
    private void processRawTile() {    
        this.buildOrientations();
        this.setTileAndOrientationIds();
    }

    /**
     * build orientations
     */
    public void buildOrientations() {
        for(Orientation o: Orientation.getOrientations(this.orientationMode)){
            
            Tile tile = new Tile();
            //System.out.println("start transformation...Orientation:" + o + " for tile Id:" + this.id);
            for(Square sq:rawTile.getSquares()){
                //transform rawTile to origin
                int x1 = sq.getX() - rawTile.getLeft();
                int y1 = sq.getY() - rawTile.getTop();
                
                int bottom = rawTile.getBottom()- rawTile.getTop();
                int right = rawTile.getRight() - rawTile.getLeft();
                char value = sq.getValue();
               // System.out.println("Orientation:"+ o + " oldX:" + x1+ " ,oldY:" + y1);
                //transform    
                int x = TransformationUtil.GetX(x1, y1, bottom, right, o);
                int y = TransformationUtil.GetY(x1, y1, bottom, right, o);
               // System.out.println("Orientation:"+ o + " newX:" + x+ " ,newY:" + y);
                tile.getSquares().add(new Square(x,y,value));
            }
            //System.out.println("end transformation...Orientation:" + o+ " for tile Id:" + this.id);
            //adjust width and height of the tile
            tile.adjustDimensions(this.width,this.height,o);
            tile.setSize(tile.getSquares().size());
            
            //sort the squares in the tile
            Collections.sort(tile.getSquares());
            
            //add only distinct transformations
            if(!isDuplicate(tile)){
                this.orientations.add(tile);
            }  
        }
    }

    /**
     * @return the orientations
     */
    public List<Tile> getOrientations() {
        return orientations;
    }

    private void adjustDimensions(int width, int height, Orientation o) {
       switch(o){
           case ROTATION_90:
           case ROTATION_270:
           case REFLECTION_X_ROTATION_90:
           case REFLECTION_X_ROTATION_270:
               this.width = height;
               this.height = width;
           default:
               this.width = width;
               this.height = height;
       }
    }

    private boolean isDuplicate(Tile incomingTile) {
        
       for(Tile t: this.orientations){
           boolean isDuplicate = true;
           if(incomingTile.getSize()== t.getSize()){
               for(int i=0;i<t.getSize();i++){
                    if(!incomingTile.getSquares().get(i).equals(t.squares.get(i))){
                        isDuplicate = false;
                        break;
                    }
                }
                if(isDuplicate){
                    return true;
                }
           }   
       }
        return false;
    }

    /**
     * @return the orientationId
     */
    public int getOrientationId() {
        return orientationId;
    }

    /**
     * @param orientationId the orientationId to set
     */
    public void setOrientationId(int orientationId) {
        this.orientationId = orientationId;
    }

    private void setTileAndOrientationIds() {
        for(int i=0;i<this.orientations.size();i++){
            Tile t = this.orientations.get(i);
            t.setId(this.getId());
            t.setAliasId(this.getAliasId());
            t.setOrientationId(i);
        }
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the aliasId
     */
    public int getAliasId() {
        return aliasId;
    }

    /**
     * @param aliasId the aliasId to set
     */
    public void setAliasId(int aliasId) {
        this.aliasId = aliasId;
    }

    public boolean isIsoMorphic(Tile incomingTile){
        return this.isDuplicate(incomingTile);
    }
  
}
