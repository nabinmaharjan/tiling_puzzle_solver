
package edu.memphis.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author nabin
 */
public enum Orientation {
    
    DEFAULT_0,
    
    //ROTATION
    ROTATION_90,
    ROTATION_180,
    ROTATION_270,
    
    //REFLECTION
    REFLECTION_X,
    REFLECTION_Y,
    
    //MIXED
    REFLECTION_X_ROTATION_90,
    REFLECTION_X_ROTATION_270;
    
    public static List<Orientation> getOrientations(OrientationMode o){
        List<Orientation> orientationList = new ArrayList<Orientation>();
        switch(o){
            case DEFAULT:
                orientationList.add(DEFAULT_0);
                break;
            case ROTATION:
                orientationList.add(DEFAULT_0);
                orientationList.add(ROTATION_90);
                orientationList.add(ROTATION_180);
                orientationList.add(ROTATION_270);
                break;
            case REFLECTION:
                orientationList.add(DEFAULT_0);
                orientationList.add(REFLECTION_X);
                orientationList.add(REFLECTION_Y);
                break;
            case ALL:
                orientationList = Arrays.asList(Orientation.values());
        }
        return orientationList;
    }
}
