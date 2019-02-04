
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
public class SolutionBoard extends BaseTile{
    
    private List<SolutionBoard> isoMorphs = new ArrayList<SolutionBoard>();
    private OrientationMode orientationMode;
    public SolutionBoard(int id, int width, int height,int size){
        this.id = id;
        this.width = width;
        this.height = height;
        this.size = size;  
        this.orientationMode = OrientationMode.ALL;
    }
    
    public SolutionBoard(){
        
    }
    public void placeTile(int row, int column, Tile t) {
        for(Square sq:t.getSquares()){
            int sqX = column + sq.getX();
            int sqY = row + sq.getY();
            Square sqr = new Square(sqX,sqY, (char) (t.getAliasId()+(int)sq.getValue()));
            this.squares.add(sqr);
        }
    }

    public List<SolutionBoard> getIsoMorphs() {
        return isoMorphs;
    }

    public void buildIsoMorphs() {
        for(Orientation o: Orientation.getOrientations(this.orientationMode)){
           
            SolutionBoard board = new SolutionBoard();
            //System.out.println("start transformation...Orientation:" + o + " for solution board Id:" + this.id);
            for(Square sq:this.squares){
                //transform rawTile to origin
                int x1 = sq.getX();
                int y1 = sq.getY();
                
                int bottom = this.height-1;
                int right = this.width-1;
                char value = sq.getValue();
                //System.out.println("Orientation:"+ o + " oldX:" + x1+ " ,oldY:" + y1);
                //transform    
                int x = TransformationUtil.GetX(x1, y1, bottom, right, o);
                int y = TransformationUtil.GetY(x1, y1, bottom, right, o);
                //System.out.println("Orientation:"+ o + " newX:" + x+ " ,newY:" + y);
                board.getSquares().add(new Square(x,y,value));
            }
            //System.out.println("end transformation...Orientation:" + o+ " for board Id:" + this.id);
            //adjust width and height of the board
            board.adjustDimensions(this.width,this.height,o);
            board.setSize(board.getSquares().size());
            
            //sort the squares in the board
            Collections.sort(board.getSquares());
            
            //add only distinct transformations
            if(!isDuplicate(board)){
                this.isoMorphs.add(board);
            }  
        }
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

    private boolean isDuplicate(SolutionBoard incomingBoard) {
        for(SolutionBoard b: this.isoMorphs){
           boolean isDuplicate = true;
           if(incomingBoard.getSize()== b.getSize()){
               for(int i=0;i<b.getSize();i++){
                    if(!incomingBoard.getSquares().get(i).equals(b.squares.get(i))){
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
    
    public boolean isIsoMorphic(SolutionBoard incomingBoard){
        return this.isDuplicate(incomingBoard);
    }
}
