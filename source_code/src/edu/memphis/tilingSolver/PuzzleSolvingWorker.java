
package edu.memphis.tilingSolver;

import edu.memphis.serializable.DataElement;
import edu.memphis.serializable.Solution;
import edu.memphis.serializable.SparseMatrix;
import edu.memphis.serializable.SparseRow;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author nabin
 */
public class PuzzleSolvingWorker extends DancingLinkSolver{
    private final SparseMatrix sparseMatrix;
    //private final Socket serverConn;
    private final ObjectOutputStream out;
    public PuzzleSolvingWorker(SparseMatrix sparseMatrix,ObjectOutputStream out){
        this.sparseMatrix = sparseMatrix;
        //this.serverConn = serverConn;
        this.out = out;
    }

    public void work() {
        this.createColumnHeaders();
        this.createSparseMatrix();
        this.solve(0, -1);
    }

    private void createSparseMatrix() {
        for(SparseRow srow: this.sparseMatrix.getRows()){

            Node startingRowNode = null;
            boolean firstDataNode = true;
            for(DataElement dataElem: srow.getDataNodes()){
                    Node dataNode = new Node();
                    dataNode.setValue(dataElem.getDataValue());
                    dataNode.setRowIndex(srow.getRowIndex());
                    ColumnHeader colHeader = this.getColumnHeader(dataElem.getColHeader());
                    colHeader.appendDataNode(dataNode);
                    if(firstDataNode){
                        startingRowNode = dataNode;
                        firstDataNode = false;
                    }
                    else{
                        dataNode.right = startingRowNode;
                        dataNode.left = startingRowNode.left;
                        
                        startingRowNode.left.right = dataNode;
                        startingRowNode.left = dataNode;
                    }
            }
        }
    }

    private void createColumnHeaders() {
        for(int columnId: sparseMatrix.getHeaders()){
            this.createColumnHeader(columnId);
        }
    }
    
     @Override
    public void printSolution()  {
        //ObjectOutputStream out = null;
        try {
            System.out.println("Got Solution!!!");
            List<DataElement> solutionNodes = new ArrayList<DataElement>();
            solutionNodes.addAll(this.sparseMatrix.getSolutionNodes());
            for (Iterator<Node> it = solutions.iterator(); it.hasNext();) {
                Node node = it.next();
                DataElement dataElem = new DataElement();
                dataElem.setDataValue(node.getValue());
                dataElem.setColHeader(node.columnHeader.getId());
                solutionNodes.add(dataElem);
            }   Solution sol = new Solution();
            sol.setSolutionNodes(solutionNodes);
            List<Solution> returnSolutions = new ArrayList<Solution>();
            returnSolutions.add(sol);
            //out = new ObjectOutputStream(serverConn.getOutputStream());
            out.writeObject(returnSolutions);
            //returnSolutions.add(sol);
        } catch (IOException ex) {
            System.out.println("Error writing to output stream...");
            ex.printStackTrace(System.out);
        } 
     }

}
