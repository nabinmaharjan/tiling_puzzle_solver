
package edu.memphis.util;

import edu.memphis.enums.Orientation;

/**
 *
 * @author nabin
 */
public class TransformationUtil {
    
    public static int GetX(int x,int y,int height,int width, Orientation o){
        switch(o){
            case ROTATION_90:
                return y;
            case ROTATION_180:
                return width-x;
            case ROTATION_270:
                return height-y;
            case REFLECTION_X:
                return x;
            case REFLECTION_Y:
                return width-x;
            case REFLECTION_X_ROTATION_90:
                return height-y;
            case REFLECTION_X_ROTATION_270:
                return y;
        }
        //case DEFAULT_0:
        return x;
    }
    
    public static int GetY(int x,int y,int height,int width,Orientation o){
        switch(o){
            case ROTATION_90:
                return width-x;
            case ROTATION_180:
                return height-y;
            case ROTATION_270:
                return x;
            case REFLECTION_X:
                return height-y;
            case REFLECTION_Y:
                return y;
            case REFLECTION_X_ROTATION_90:
                return width-x;
            case REFLECTION_X_ROTATION_270:
                return x;
        }
        return y;
    }
    
    
}
