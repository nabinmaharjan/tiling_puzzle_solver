package edu.memphis.serializable;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author nabin
 */
public class SparseRow implements Serializable{
    private int rowIndex;
    private List<DataElement> dataNodes = new ArrayList<DataElement>();

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

    /**
     * @return the dataNodes
     */
    public List<DataElement> getDataNodes() {
        return dataNodes;
    }

    /**
     * @param dataNodes the dataNodes to set
     */
    public void setDataNodes(List<DataElement> dataNodes) {
        this.dataNodes = dataNodes;
    }
}
