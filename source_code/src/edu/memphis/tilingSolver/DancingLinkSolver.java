
package edu.memphis.tilingSolver;

import edu.memphis.serializable.DataElement;
import edu.memphis.serializable.SparseMatrix;
import edu.memphis.serializable.SparseRow;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author nabin
 */
public abstract class DancingLinkSolver {
    
    private final ColumnHeader rootHeader = new ColumnHeader();
    //private final ArrayList<ColumnHeader> columnHeaderList = new ArrayList<ColumnHeader>();
    private final Map<Integer,ColumnHeader> columnHeaderMap = new HashMap<Integer,ColumnHeader>();
    private final ArrayList<Node> rowHeaders = new ArrayList<Node>();
    protected final Stack<Node> solutions = new Stack<Node>();
    private final List<SparseMatrix> sparseMatrixList = new ArrayList<SparseMatrix>();
    long count=0;
    protected ColumnHeader getColumnHeader(int columnIndex){
       // return columnHeaderList.get(columnIndex);
        return columnHeaderMap.get(columnIndex);
    }
    
    protected Node getRowHeader(final int rowIndex){
        return rowHeaders.get(rowIndex);
    }
    
    protected void addRowHeader(final Node rowHeader){
        rowHeaders.add(rowHeader);
    }
    
    protected void createColumnHeader(int columnId){
        ColumnHeader header = new ColumnHeader();
        header.right = getRootHeader();
        header.left = getRootHeader().left;
        header.setId(columnId);
        rootHeader.left.right = header;
        rootHeader.left = header;

        columnHeaderMap.put(columnId, header);
    }
    
//    protected void createColumnHeaderList(int numOfColumns){
//        int baseLabel = 65;
//        for(int columnIndex=0; columnIndex < numOfColumns; columnIndex++){
//            ColumnHeader header = new ColumnHeader();            
//            int label = baseLabel + columnIndex;
//            header.setId(String.valueOf((char)label));
//            header.right = getRootHeader();
//            header.left = getRootHeader().left;
//            
//            rootHeader.left.right = header;
//            rootHeader.left = header;
//            
//            columnHeaderList.add(header);
//            
//        }
//    }
    
    public ColumnHeader chooseColumn(){
        int minimumColLength = Integer.MAX_VALUE;
        ColumnHeader returnColumn = null;
        for (ColumnHeader colHeader = (ColumnHeader) rootHeader.right; colHeader != rootHeader;colHeader = (ColumnHeader) colHeader.right) {
            if(colHeader.columnLength > 0 && minimumColLength > colHeader.columnLength){
                returnColumn = colHeader;
                minimumColLength = colHeader.columnLength;
            }
        }
       // System.out.println(String.format("Column Selected : %s at index %s with minimum column value : %d",returnColumn.getId(),returnColumn.getId(),returnColumn.getColumnLength()));
        return returnColumn;
    }

    public void coverColumn(final ColumnHeader colHeader){
        
        //remove columnheader from the headerlist
        colHeader.left.right = colHeader.right;
        colHeader.right.left = colHeader.left;
        
        for(Node rowdataNode = colHeader.down;rowdataNode != colHeader;rowdataNode = rowdataNode.down){
            for(Node colDataNode = rowdataNode.right; colDataNode != rowdataNode; colDataNode =colDataNode.right){
                //remove rows from the other columnlist
                colDataNode.up.down = colDataNode.down;
                colDataNode.down.up = colDataNode.up;
                colDataNode.columnHeader.columnLength--;
            }
        }
    }
    
    public void unCoverColumn(final ColumnHeader colHeader){
        for(Node rowdataNode = colHeader.up; rowdataNode != colHeader; rowdataNode = rowdataNode.up){
            for(Node colDataNode = rowdataNode.left;colDataNode != rowdataNode;colDataNode = colDataNode.left){
                //restore rows to the other columnlist
                colDataNode.up.down = colDataNode;
                colDataNode.down.up = colDataNode;
                colDataNode.columnHeader.columnLength++;
            }
        }
        
        //uncover columnHeader
        colHeader.left.right = colHeader;
        colHeader.right.left = colHeader;
    }
    
    public void solve(int k, int maxExploreLevel){
        
        if(maxExploreLevel !=-1 && k >= maxExploreLevel){
            //do Something and return
            SparseMatrix matrix = new SparseMatrix();
            
            Map<Integer,List<DataElement>> rowMap = new HashMap<Integer,List<DataElement>>();
            
            for (ColumnHeader colHeader = (ColumnHeader) rootHeader.right; colHeader != rootHeader;colHeader = (ColumnHeader) colHeader.right) {
                //add columnHeaders
                matrix.getHeaders().add(colHeader.getId());
                for(Node rowdataNode = colHeader.down;rowdataNode != colHeader;rowdataNode = rowdataNode.down){
                    List<DataElement> row = rowMap.get(rowdataNode.getRowIndex());
                    if(row == null){
                        row = new ArrayList<DataElement>();
                        rowMap.put(rowdataNode.getRowIndex(), row);
                    }
                    DataElement dataElem = new DataElement();
                    dataElem.setColHeader(colHeader.getId());
                    dataElem.setDataValue(rowdataNode.getValue());
                    row.add(dataElem);
                 }
            }
            
            //add rows
            List<Integer> keyList = new ArrayList<Integer>();
            for(Integer key: rowMap.keySet()){
                keyList.add(key);
            }
            
            Collections.sort(keyList);
            
            for(int i=0;i< keyList.size();i++){
                SparseRow s_row = new SparseRow();
                int key = keyList.get(i);
                s_row.setRowIndex(key);
                s_row.setDataNodes(rowMap.get(key));
                matrix.getRows().add(s_row);
            }
            
            //add partial solutions
            for (Iterator<Node> it = solutions.iterator(); it.hasNext();) {
                Node node = it.next();
               // System.out.println(node.getValue());
                DataElement dataElem = new DataElement();
                dataElem.setDataValue(node.getValue());
                dataElem.setColHeader(node.columnHeader.getId());
                matrix.getSolutionNodes().add(dataElem);
             }
            
            this.sparseMatrixList.add(matrix);
            return;
        }
        if(rootHeader.right == rootHeader){
            printSolution();
            return;
        }
        ColumnHeader colHeader = chooseColumn();
        
        if(colHeader == null){
            return;
        }
        
//        if(maxExploreLevel==-1)
//         System.out.println(String.format("Covering selected column :  %d",colHeader.getId()));
        coverColumn(colHeader);
        for(Node row = colHeader.down;row != colHeader;row = row.down){
//            if(maxExploreLevel==-1)
//                System.out.println("Solution node: " + row.getValue() + " Row count:" + colHeader.columnLength);
            solutions.push(row);
//            System.out.println("Count:"+(++count)+":solution stack size:" + solutions.size());
//            System.out.println("added node to solution with value:" + row.getValue());
            for(Node col = row.right; col!= row; col = col.right ){
//                if(maxExploreLevel==-1)
//                    System.out.println(String.format("Covering column : %d",col.columnHeader.getId()));
                coverColumn(col.columnHeader);
            }
//            if(maxExploreLevel==-1)
//                System.out.println("Exploring completed for level: " + k);
            solve(k+1,maxExploreLevel);
            solutions.pop();
            //System.out.println("solution stack size:" + solutions.size());
            //System.out.println("removed  node from solution list with value:" + row.getValue());
            colHeader = row.columnHeader;
            for(Node col = row.left;col != row;col = col.left){
                unCoverColumn(col.columnHeader);
//                if(maxExploreLevel==-1)
//                    System.out.println(String.format("Uncovering column : %d",col.columnHeader.getId()));
            }
        }
        unCoverColumn(colHeader);
//        if(maxExploreLevel==-1)
//            System.out.println(String.format("Uncovering selected column : %d",colHeader.getId()));
    }
    /**
     * @return the rootHeader
     */
    public ColumnHeader getRootHeader() {
        return rootHeader;
    }

    protected void printSolution() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the sparseMatrixList
     */
    public List<SparseMatrix> getSparseMatrixList() {
        return sparseMatrixList;
    }
    
}
