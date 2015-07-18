package com.android.battleship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by srandall on 7/12/15.
 */
public class Ship implements Serializable {
    private int length;
    private ArrayList<Integer> coordinates;
    private ArrayList<Integer> hits;

    private static ArrayList<Integer> misses = new ArrayList<Integer>();

    private int direction;  // we will use 0 = horizontal and 1 = vertical



    public Ship (int length) {
        this.length = length;
        this.coordinates = new ArrayList<Integer>(length);
        this.hits = new ArrayList<Integer>(length);
    }

    public Ship (Ship modelShip) {
        this.length = modelShip.getLength();
        this.coordinates = modelShip.getCoordinates();
        this.hits = modelShip.getHits();
        this.direction = modelShip.getDirection();
    }

    public int getLength() {
        return this.length;
    }

    public boolean isShipDestroyed() {
        return this.length == hits.size();
    }

    public ArrayList<Integer> getCoordinates() {
        return this.coordinates;
    }
    public ArrayList<Integer> getHits() {
        return this.hits;
    }

    public void clearCoordinates() {
        this.coordinates.clear();
    }

    public void setCoordinates( ArrayList<Integer> coordinates) {
        this.coordinates.clear();
        this.coordinates.addAll(coordinates);
    }

    public void setDirection( int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return this.direction;
    }

    public int getStartPosition() {
        return this.coordinates.get(0);
    }

    public void setHit(int position) {
        this.hits.add(position);
    }

    public int getNumberOfHits() {
        if (this.hits.isEmpty())
            return 0;
        else
            return this.hits.size();
    }


    public static void setMiss(int position) {
        misses.add(position);
    }

    public static int getNumberOfMisses() {
        if (misses.isEmpty())
            return 0;
        else
            return misses.size();
    }

    public static ArrayList<Integer> getMisses() {
        return misses;
    }



}
