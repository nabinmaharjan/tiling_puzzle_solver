
package edu.memphis.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nabin
 */
public class FileWriterUtil {
    
    public static void writeToFile(String fileName,List<String> outLines){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for(String line:outLines){
                writer.write(line+"\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(FileWriterUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
