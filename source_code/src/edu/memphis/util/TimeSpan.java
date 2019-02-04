
package edu.memphis.util;

/**
 *
 * @author nabin
 */
public class TimeSpan {
    
    private long timeInMilliSec;
    private double minutes;
    private double hours;
    private double seconds;
    public TimeSpan(long timeInMilliSec){
        this.timeInMilliSec = timeInMilliSec;
        this.initalize();
    }
    
    public double getElaspedMinutes(){
        return minutes;
    }
    
    public double getElaspedSeconds(){
        return seconds;
    }
    
    public double getElaspedHours(){
        return hours;
    }
    
    public double getTotalElaspedMilliSeconds(){
        return timeInMilliSec;
    }
    
    @Override
    public String toString(){
        return String.format("%f Hours %f Minutes %f Seconds", this.hours,this.minutes,this.seconds);
    }

    private void initalize() {
        double hr = (double)this.timeInMilliSec/(1000*60*60);
        this.hours = Math.floor(hr);
        
        double min = (hr - this.hours) * 60;
        this.minutes = Math.floor(min);
        
        this.seconds = (min-this.minutes)*60;
    }
}
