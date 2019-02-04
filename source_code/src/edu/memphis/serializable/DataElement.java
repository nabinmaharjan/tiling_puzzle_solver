package edu.memphis.serializable;


import java.io.Serializable;

/**
 *
 * @author nabin
 */
public class DataElement implements Serializable{
    private int colHeader;
    private String dataValue;

    /**
     * @return the colHeader
     */
    public int getColHeader() {
        return colHeader;
    }

    /**
     * @param colHeader the colHeader to set
     */
    public void setColHeader(int colHeader) {
        this.colHeader = colHeader;
    }

    /**
     * @return the dataValue
     */
    public String getDataValue() {
        return dataValue;
    }

    /**
     * @param dataValue the dataValue to set
     */
    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }
}
