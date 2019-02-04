/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.memphis.tilingSolver;

import edu.memphis.util.TimeSpan;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author nabin
 */
public class test {
    public static void main(String args[]){
       // ColumnHeader column = new ColumnHeader();
        
//        Node n = new Node();
//        Node n2 = new Node();
//        
//        n2.right = n;
//        n2.left = n.left;
//        n.left.right = n2;
//        n.left = n2;
//        Stack<Integer> solutions = new Stack<Integer>();
//        
//        solutions.push(1);
//        solutions.push(2);
//        solutions.push(3);
//        solutions.push(4);
//        
//        for (Iterator<Integer> it = solutions.iterator(); it.hasNext();) {
//            System.out.println(it.next());
//        }
//        System.out.println("Stack size:"+ solutions.size());
//        int size = solutions.size();
//        for (int i=0;i < size; i++) {
//            System.out.println(solutions.pop());
//        }
//        System.out.println("Stack size:"+ solutions.size());
//        System.out.println((char)(0+65));
//        
//        boolean t = Boolean.parseBoolean("false");
//        boolean t1 = Boolean.parseBoolean("true");
//        boolean t2 = Boolean.parseBoolean("True");
//        boolean t3 = Boolean.parseBoolean("TrUe");
//        
//        System.out.println(t);
//        System.out.println(t1);
//        System.out.println(t2);
//        System.out.println(t3);
//        
//        System.out.println((char)999);
//        System.out.println((int)'ϧ');
        
        List<Integer> testList = new ArrayList<Integer>();
        testList.add(1);
        testList.add(2);
        testList.add(3);
        testList.add(4);
        
        int count = testList.size();
        for(int k=0;k< count;k++){
            Integer a =  testList.get(0);
            testList.remove(a);
            System.out.println("Item: " + a);
        }
        
        
//        Date d1 = new Date();
//        System.out.println(d1.getTime());
//        
//        TimeSpan ts = new TimeSpan(1512*60);
//        System.out.println(ts.toString());
    }
}
