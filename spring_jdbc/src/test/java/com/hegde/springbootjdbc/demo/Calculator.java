package com.hegde.springbootjdbc.demo;

public class Calculator {

    //find sum of 3 integers
    public long sum(int x,int y,int z){
        return (long) x+y+z;
    }

    //find product of two integers
    public long mult(int x,int y){

        return (long) x * (long) y;
    }

    public boolean compare2Nums(int x,int y){

        return x == y;
    }
}
