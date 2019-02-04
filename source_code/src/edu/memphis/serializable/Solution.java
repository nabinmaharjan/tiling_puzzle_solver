
package edu.memphis.serializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nabin
 */
public class Solution implements Serializable {
    private List<DataElement> solutionNodes = new ArrayList<DataElement>();

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
