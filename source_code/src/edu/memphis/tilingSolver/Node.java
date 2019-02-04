
package edu.memphis.tilingSolver;

/**
 *
 * @author nabin
 */
public class Node {
    public Node left;
    public Node right;
    
    public Node up;
    public Node down;
    public ColumnHeader columnHeader;
    private int rowIndex;
    private String value;
    
    public Node(){
        this.left = this;
        this.right = this;
        this.up = this;
        this.down = this;
        this.rowIndex = -1;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the rowIndex
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * @param rowIndex the rowIndex to set
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
}
