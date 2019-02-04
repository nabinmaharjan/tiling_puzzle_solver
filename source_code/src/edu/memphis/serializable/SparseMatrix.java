package edu.memphis.serializable;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author nabin
 */
public class SparseMatrix implements Serializable {
    private List<Integer> headers = new ArrayList<Integer>();
    private List<SparseRow> rows = new ArrayList<SparseRow>();
    private List<DataElement> solutionNodes = new ArrayList<DataElement>();
    
    public static SparseMatrix createTestMatrix(){
        SparseMatrix matrix = new SparseMatrix();
        Random rand = new Random();
        for(int i=1;i<=2;i++){
            matrix.getHeaders().add(rand.nextInt(10));
        }
        

            SparseRow row = new SparseRow();
            row.setRowIndex(rand.nextInt(10));
            
            DataElement d1 = new DataElement();
            d1.setColHeader(rand.nextInt(10));
            d1.setDataValue(String.format("%d_%d_%d_%d",rand.nextInt(10),rand.nextInt(10),rand.nextInt(10),rand.nextInt(10)));
            
           
            DataElement d2 = new DataElement();
            d2.setColHeader(rand.nextInt(10));
            d1.setDataValue(String.format("%d_%d_%d_%d",rand.nextInt(10),rand.nextInt(10),rand.nextInt(10),rand.nextInt(10)));
            
            row.getDataNodes().add(d1);
            row.getDataNodes().add(d2);
            matrix.getRows().add(row);
            
            row= new SparseRow();
            row.setRowIndex(rand.nextInt(10));
            
            d1 = new DataElement();
            d1.setColHeader(rand.nextInt(10));
            d1.setDataValue(String.format("%d_%d_%d_%d",rand.nextInt(10),rand.nextInt(10),rand.nextInt(10),rand.nextInt(10)));
               
            row.getDataNodes().add(d1);
            matrix.getRows().add(row);
            
            d1 = new DataElement();
            d1.setColHeader(rand.nextInt(10));
            d1.setDataValue(String.format("%d_%d_%d_%d",rand.nextInt(10),rand.nextInt(10),rand.nextInt(10),rand.nextInt(10)));
            
            matrix.getSolutionNodes().add(d1);
            
            return matrix;
    }
    public static void main(String args[]) throws IOException, IOException, IOException, ClassNotFoundException{
        SparseMatrix matrix = new SparseMatrix();
        
        for(int i=1;i<=2;i++){
            matrix.getHeaders().add(i);
        }
        

            SparseRow row = new SparseRow();
            row.setRowIndex(0);
            
            DataElement d1 = new DataElement();
            d1.setColHeader(1);
            d1.setDataValue("0_0_0_0");
            
           
            DataElement d2 = new DataElement();
            d2.setColHeader(2);
            d2.setDataValue("0_0_0_0");
            
            row.getDataNodes().add(d1);
            row.getDataNodes().add(d2);
            matrix.getRows().add(row);
            
            row= new SparseRow();
            row.setRowIndex(1);
            
            d1 = new DataElement();
            d1.setColHeader(1);
            d1.setDataValue("0_1_1_0");
               
            row.getDataNodes().add(d1);
            matrix.getRows().add(row);
            
            d1 = new DataElement();
            d1.setColHeader(1);
            d1.setDataValue("0_10_0_0");
            
            matrix.getSolutionNodes().add(d1);
            
            // Let's serialize an Object
		try {
			FileOutputStream fileOut = new FileOutputStream("SparseMatrix.txt");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(matrix);
			out.close();
			fileOut.close();
			System.out.println("\nSerialization Successful... Checkout your specified output file..\n");
                        
//                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                        out = new ObjectOutputStream(bos);
//                        out.writeObject(matrix);
//                        out.close();
//                        byte[] buf = bos.toByteArray();
 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
 
		// Let's deserialize an Object
		try {
			FileInputStream fileIn = new FileInputStream("SparseMatrix.txt");
			ObjectInputStream in = new ObjectInputStream(fileIn);
                        
                        SparseMatrix deSerializedMatrix = (SparseMatrix)in.readObject();
			System.out.println("Deserialized Data: \n" + deSerializedMatrix.headers.size());
			in.close();
			fileIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }
    /**
     * @return the headers
     */
    public List<Integer> getHeaders() {
        return headers;
    }

    /**
     * @param headers the headers to set
     */
    public void setHeaders(List<Integer> headers) {
        this.headers = headers;
    }

    /**
     * @return the rows
     */
    public List<SparseRow> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(List<SparseRow> rows) {
        this.rows = rows;
    }

    /**
     * @return the solutionNodes
     */
    public List<DataElement> getSolutionNodes() {
        return solutionNodes;
    }

    /**
     * @param solutionNodes the solutionNodes to set
     */
    public void setSolutionNodes(List<DataElement> solutionNodes) {
        this.solutionNodes = solutionNodes;
    }
}
