package com.android.battleship;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class SetupActivity extends Activity {

    protected static final int NO_FIELD_IS_AIMED = -1;

    private static final Random r = new Random();

    protected ImageAdapter imageAdapter;
    protected GridView boardGrid;

    protected Button buttonRotate;
    protected Button buttonFire;

    private Ship[] ships;

    private static final String TAG = SetupActivity.class.getSimpleName();

    private boolean lastClickWasShip;

    private int clickedShipNumber, lastClickedShipNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lastClickWasShip = false;
        clickedShipNumber = -1;
        lastClickedShipNumber = 0;


        ships = new Ship[4];
        for (int i = 0; i < 4; ++i) {
            ships[i] = new Ship(i + 2);
        }

        randomizeShips();

        displayGameScreen();
        attachActionListeners();

    }



    protected void displayGameScreen(){
        setContentView(R.layout.activity_setup);

        buttonRotate = (Button) findViewById(R.id.buttonRotate);
        buttonFire = (Button) findViewById(R.id.buttonFire);

        boardGrid = (GridView) findViewById(R.id.setup_gridview);

        imageAdapter = new ImageAdapter(this, this.ships);
        boardGrid.setAdapter(imageAdapter);

    }

    private void attachActionListeners(){


        buttonFire.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                buttonRotate.setEnabled(false);
                executeFire();
            }
        });


        //boardGame GridView listener sets the aimedField property and changes aim color
        boardGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageView _iv = (ImageView) v;

                clickedShipNumber = isShip(position);
                if (clickedShipNumber != -1) { // this is a ship and we are returned ship number 0 - 4

                    lastClickWasShip = true;
                    lastClickedShipNumber = clickedShipNumber;


                } else {  // this is not a ship.

                    if (lastClickWasShip) {  // If a Ship was previous ImageView clicked
                        // we know the ship number that was clicked. We now know board position clicked
                        boolean valid = true;
                        for (int i = 0; i < ships.length; ++i) {
                            if (i != lastClickedShipNumber) {
                                valid = shipNoConflicts(ships[lastClickedShipNumber].getDirection(), position, ships[lastClickedShipNumber].getLength(), ships[i].getCoordinates());
                                if (valid == false)
                                    break;

                            }
                        }

                        if (valid == true) {
                            if (shipWillFit(ships[lastClickedShipNumber].getDirection(), position,
                                    ships[lastClickedShipNumber].getLength())) {

                                redrawShip(position, lastClickedShipNumber);

                            }

                        }
                    }


                }

                buttonRotate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClickedShipNumber != -1)
                            rotateShip(lastClickedShipNumber);
                    }
                });


            }
        });

    }

    private void executeFire(){
        int random = r.nextInt(99);
        int hitShip =  isShip(random);
        if (hitShip == -1)
            Ship.setMiss(random);
        else
            ships[hitShip].setHit(random);

        imageAdapter.notifyDataSetChanged();
    }

    public void randomizeShips() {

        for (int shipCount = 0; shipCount < ships.length; ++shipCount)
            ships[shipCount].clearCoordinates();

        for (int shipCount = 0; shipCount < ships.length; ++shipCount) {
            int direction = r.nextInt(2);
            this.ships[shipCount].setDirection(direction);
            int randomStartPosition = r.nextInt(99);
            boolean valid = false;
            while (valid == false) {
                randomStartPosition = r.nextInt(99);
                if (shipWillFit(direction, randomStartPosition, ships[shipCount].getLength())) {
                    valid = true;
                    if (shipCount > 0) {
                        for (int i = shipCount - 1; i > 0; --i) {
                            valid = shipNoConflicts(direction, randomStartPosition, ships[i].getLength(), ships[i].getCoordinates());
                            if (valid == false)
                                break;
                        }
                    }

                }
            }  // valid is true so we have a valid start position and direction for this ship
            // we now need to update this ship's coordinates - still within for loop for each ship

            ArrayList<Integer> coordinates = new ArrayList<Integer>(ships[shipCount].getLength());

            if (direction == 0) {

                for (int i = 0; i < ships[shipCount].getLength(); ++i) {
                    coordinates.add(randomStartPosition + i);
                }

            }

            else {
                for (int i = 0; i < ships[shipCount].getLength(); ++i) {
                    coordinates.add(randomStartPosition + (i * 10));
                }

            }

            ships[shipCount].setCoordinates(coordinates);



        }  // end of for each ship

    }  // end randomizeShips()


    public boolean shipNoConflicts(int direction, int startPosition, int length, ArrayList<Integer> coordinates) {
        boolean valid = true;
// if direction is horizontal, have to check in loop for startPosition + checkedShipped length -1
        if (direction == 0) {
            for (int i = startPosition; i < startPosition + length + 1; ++i) {
                if (coordinates.contains(i)) {  // horizontal
                    valid = false;
                    break;
                }
            }
        }
        else {
            for (int i = startPosition; i < startPosition + (length * 10); i+=10) {
                if (coordinates.contains(i)) {  // horizontal
                    valid = false;
                    break;
                }
            }
        }

        return valid;
    }

    public boolean shipWillFit(int direction, int startPosition, int shipLength) {
        boolean valid = false;

        if (direction == 0) {   // horizontal
            if ((startPosition + 10) / 10*10 - startPosition >= shipLength) {
                valid = true;
            }

        }

        else {
            if (((shipLength - 1) * 10) + startPosition < 100) {
                valid = true;
            }

        }
        return valid;
    }

    public int isShip(int position) {
        int shipNumber = -1;

        for (int shipCount = 0; shipCount < ships.length; ++shipCount) {

            ArrayList<Integer> coordinates = new ArrayList<>(this.ships[shipCount].getLength());
            coordinates = this.ships[shipCount].getCoordinates();

            if (coordinates.indexOf(position) != -1) {
                shipNumber = shipCount;
                break;
            }

        }

        return shipNumber;
    }

    public void rotateShip(int shipNumber) {
        boolean valid = true;
        int oldDirection = this.ships[shipNumber].getDirection();
        int newDirection;
        if (oldDirection == 0)
            newDirection = 1;
        else
            newDirection = 0;


        for (int i = 0; i < 2; ++ i) {   // take current startPosition and try adding from 0 to 2 to see if it will rotate


            if ( shipWillFit(newDirection, this.ships[shipNumber].getStartPosition() + i,
                    this.ships[shipNumber].getLength())) {

                for (int j = 0; j < ships.length; ++j) {   // if it will fit at startPosition + i

                    if (j != shipNumber) {  // Do not check the ship you are trying to rotate
                        valid = shipNoConflicts(newDirection, this.ships[shipNumber].getStartPosition() + i,
                                this.ships[shipNumber].getLength(), this.ships[j].getCoordinates());
                        if (valid == false) {

                            break;
                        }
                    }
                }

                if (valid == true) {

                    this.ships[shipNumber].setDirection(newDirection);
                    redrawShip(this.ships[shipNumber].getStartPosition(), shipNumber);
                    break;
                }

            }
            if (valid == true)
                    break;

        }


    }



    public void redrawShip (int position, int shipNumber) {

        int imageResource = R.drawable.white;

        ArrayList<Integer> oldCoordinates = new ArrayList<>(this.ships[shipNumber].getLength());

        ArrayList<Integer> newCoordinates = new ArrayList<>(this.ships[shipNumber].getLength());

        oldCoordinates = this.ships[shipNumber].getCoordinates();

        if (ships[shipNumber].getDirection() == 0) {

            for (int i = 0; i < ships[shipNumber].getLength(); ++i) {
                newCoordinates.add(position + i);
            }

        }

        else {
            for (int i = 0; i < ships[shipNumber].getLength(); ++i) {
                newCoordinates.add(position + (i * 10));
            }

        }

        ships[shipNumber].setCoordinates(newCoordinates);


        imageAdapter.notifyDataSetChanged();

    }


}
