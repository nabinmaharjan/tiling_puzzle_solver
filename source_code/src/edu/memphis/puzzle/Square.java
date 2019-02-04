
package edu.memphis.puzzle;

/**
 *
 * @author sujit
 */
public class Square implements Comparable<Square>{
    private int x;
    private int y;
    private char value;

    public Square(){
        
    }
    
    public Square(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public Square(int x, int y, char ch){
        this.x = x;
        this.y = y;
        this.value = ch;
    }
    
    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the value
     */
    public char getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(char value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o){
        return this.equals((Square)o);
    }
    public boolean equals(Square o){
        return this.x == o.x && this.y == o.y && this.value == o.value;
    }
    
    @Override
    public int compareTo(Square o) {
        return this.y!=o.y?(this.y-o.y):(this.x-o.x);
    }
}
