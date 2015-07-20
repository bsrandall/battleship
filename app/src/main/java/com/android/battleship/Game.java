package com.android.battleship;

/**
 * Created by srandall on 7/19/15.
 */

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Game implements Serializable {

    private List<ArrayList<Integer>> boards;
    private List<Ship[]> ships;
    private List<ArrayList<Integer>> destroyedShips;

    private boolean gameOver = false;

    private final static int BOARD_SIZE = 100;
    private final static int SHIP_COUNT = 4;

    private final static Integer WATER = 99;
    private final static int FIRE_MISS = 61;
    private final static int FIRE_HIT = 62;
    private final static int FIRE_DESTROY_SHIP = 12;
    private final static int FIRE_DESTROY_FLEET = 13;





    public Game( Ship[] playerOneShips, Ship[] playerTwoShips) {

        createBoards(playerOneShips, playerTwoShips);
        ships = new ArrayList<Ship[]>(2);
        ships.add(playerOneShips);
        ships.add(playerTwoShips);

        ArrayList<Integer>  player1Destroyed = new ArrayList<Integer>(SHIP_COUNT);
        ArrayList<Integer>  player2Destroyed = new ArrayList<Integer>(SHIP_COUNT);
        this.destroyedShips = new ArrayList<ArrayList<Integer>>(2);
        destroyedShips.add(player1Destroyed);
        destroyedShips.add(player2Destroyed);


    }

    public Game (Game sentGame) {

        this.boards = new ArrayList<ArrayList<Integer>>(2);

        for (int i = 0; i < 2; ++i)
            this.boards.add(sentGame.getPlayerBoard(i));

        this.ships = new ArrayList<Ship[]>(2);

        for (int i = 0; i < 2; ++i)
            this.ships.add(sentGame.getPlayerShips(i));

        this.destroyedShips = new ArrayList<ArrayList<Integer>>(2);

        for (int i = 0; i < 2; ++i)
            this.destroyedShips.add(sentGame.getPlayerDestroyedShips(i));

    }


    public Ship[] getPlayerShips(int playerNumber) {
        return ships.get(playerNumber);
    }

    public void setPlayerShips(int playerNumber, Ship[] playerShips) {
        ships.set(playerNumber, playerShips);
    }

    public boolean iseGameOver() {
        return gameOver;
    }

    private void createBoards(Ship[] player1Ships, Ship[] player2Ships) {
        ArrayList<Integer>  board0 = new ArrayList<Integer>(BOARD_SIZE);
        ArrayList<Integer>  board1 = new ArrayList<Integer>(BOARD_SIZE);
        for (int i = 0; i < 100; ++i) {
            board0.add(WATER);
            board1.add(WATER);
        }

        for (int j = 0; j < SHIP_COUNT; ++j) {

            ArrayList<Integer>  player1ShipCoordinates = new ArrayList<Integer>(player1Ships[j].getLength());
            player1ShipCoordinates.addAll(player1Ships[j].getCoordinates());

            ArrayList<Integer>  player2ShipCoordinates = new ArrayList<Integer>(player1Ships[j].getLength());
            player2ShipCoordinates.addAll(player1Ships[j].getCoordinates());

            for (int k = 0; k < player1Ships[j].getLength(); ++k )
                board0.set(player1ShipCoordinates.get(k), j*10+k);

            for (int k = 0; k < player2Ships[j].getLength(); ++k )
                board1.set(player2ShipCoordinates.get(k), j*10+k);

        }


        this.boards = new ArrayList<ArrayList<Integer>>(2);
        this.boards.add(board0);
        this.boards.add(board0);


    }

    public ArrayList<Integer> getPlayerBoard(int playernumber) {
        ArrayList<Integer>  newBoard = new ArrayList<Integer>(BOARD_SIZE);
        newBoard.addAll(this.boards.get(playernumber));

        return newBoard;

    }

    public ArrayList<Integer> getPlayerDestroyedShips(int playernumber) {
        ArrayList<Integer>  newDestroyedShips = new ArrayList<Integer>(SHIP_COUNT);
        newDestroyedShips.addAll(this.destroyedShips.get(playernumber));

        return newDestroyedShips;
    }

    public void markBoardWithShip(int playerNumber, int shipNumber) {
        ArrayList<Integer> shipCoordinates = new ArrayList<>(ships.get(playerNumber)[shipNumber].getLength());
        shipCoordinates = ships.get(playerNumber)[shipNumber].getCoordinates();

        for (int i = 0; i < shipCoordinates.size(); ++i) {
            this.boards.get(playerNumber).get(i);
        }

    }

    public void markBoardWithHit(int playerNumber, int position) {
        this.boards.get(playerNumber).set(position, FIRE_HIT);  // 7 will mean a hit
    }

    public void markBoardWithMiss(int playerNumber, int position) {
        Log.d("TEST", "playerNumber SET TO " + playerNumber + "position SET  TO " + position);
        this.boards.get(playerNumber).set(position, FIRE_MISS);  // 6 will mean a miss
    }


    public int processMove(int attackingPlayer, int position) {
        int attackedPlayer = getOpposite(attackingPlayer);
        int positionResult = FIRE_MISS;

        // check if this is position on any of attackedPlayers ships


        ArrayList<Integer> boardGrid = new ArrayList<>(this.boards.get(attackedPlayer));

        int positionContains = boardGrid.get(position);

        if (positionContains < 50) {  // this is a hit
            markBoardWithHit(attackedPlayer, position);  // 61 will mean a hit
            positionResult = FIRE_HIT;

            // check if this hit destroyed the ship

            if (!boardGrid.contains(positionContains)) {
                destroyedShips.get(attackedPlayer).add(positionContains);
                positionResult = FIRE_DESTROY_SHIP;

                if (!fleetStillAlive(attackedPlayer))
                    positionResult = FIRE_DESTROY_FLEET;

            }

            // if it did destroy a ship, check and see if that was the end of the fleet

        }

        else { // it did not hit a ship, add board miss and return FIRE_MISS
            markBoardWithMiss(attackedPlayer, position);
            positionResult = FIRE_MISS;

        }

        return positionResult;
    }


    public int getOpposite(int player) {
        int returnPlayer = 0;
        if (player == 0)
            returnPlayer = 1;
        else
            returnPlayer = 0;

        return returnPlayer;

    }

    public boolean fleetStillAlive(int playerNumber) {
        boolean alive = true;

        if (this.destroyedShips.size() == SHIP_COUNT)
            alive = false;

        return alive;

    }

    public int getBoardCellValueByPlayer(int playerNumber, int position) {
        return this.getPlayerBoard(playerNumber).get(position);
    }





}
