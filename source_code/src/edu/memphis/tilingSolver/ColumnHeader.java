
package edu.memphis.tilingSolver;

/**
 *
 * @author nabin
 */
public class ColumnHeader extends Node{
    public int columnLength;
    private int Id;
    
    public ColumnHeader(){
        this.columnHeader = this;
    }

    /**
     * @return the Id
     */
    public int getId() {
        return Id;
    }

    /**
     * @param Id the Id to set
     */
    public void setId(int id) {
        this.Id = id;
    }
    
    public void appendDataNode(Node n){
        
       // n.setColumnHeader(this);
        n.columnHeader = this;
        n.down = this;
        n.up = this.up;
        
        this.up.down = n;
        this.up = n;
        
        this.columnLength++;
    }
}
