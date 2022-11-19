package com.hegde.springbootjdbc.demo.backup;

import java.util.SplittableRandom;
import java.util.stream.DoubleStream;

//File to create random latitude longitudes for testing purposes
class RandomNumbers {

    private static final Long HOW_MANY_NUMBERS_YOU_WANT = 9999999999L;

    public static void main(String[] args) {

        double rangeMinLat = 19.71000;
        double rangeMaxLat = 19.80000;

        double rangeMinLon = 73.171000;
        double rangeMaxLon = 73.99000;


        SplittableRandom splittableRandom = new SplittableRandom();

        DoubleStream limitedIntStreamWithinARangeWithSplittableRandom = splittableRandom.doubles(HOW_MANY_NUMBERS_YOU_WANT, rangeMinLat, rangeMaxLat);
       limitedIntStreamWithinARangeWithSplittableRandom.forEach(System.out::println);

    }
}
