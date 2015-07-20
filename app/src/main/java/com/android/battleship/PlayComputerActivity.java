package com.android.battleship;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class PlayComputerActivity extends Activity {

    Ship[] player1Ships, computerShips;
    int player1, computer, playerTurn;

    Game game;

    GridView playerBoardGrid;
    GridView computerBoardGrid;
    EnemyGridImageAdapter enemyImageAdapter;
    PlayerGridImageAdapter playerImageAdapter;

    private Handler myHandler = new Handler();

    private static final Random r = new Random();

    TextView directionsTextView, resultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();

        player1Ships = (Ship[])bundle.getSerializable("player1Ships");
        computerShips = (Ship[])bundle.getSerializable("player2Ships");

        game = new Game(player1Ships, computerShips);
        player1 = 0;
        computer = 1;

        playerTurn = 0;

        Log.d("TEST", "MADE IT PAST INTENT");

        playGame();

    }

    protected void displayArrangeGameScreen(int playerTurn){
        setContentView(R.layout.activity_play_computer);

        directionsTextView = (TextView) findViewById(R.id.play_tv1);
        resultsTextView = (TextView) findViewById(R.id.play_tv2);
        playerBoardGrid = (GridView) findViewById(R.id.setup_gridview);
        computerBoardGrid = (GridView) findViewById(R.id.setup_gridview);


        if (playerTurn == 0) {
            enemyImageAdapter = new EnemyGridImageAdapter(this, this.game, this.playerTurn);  // display grid without ships, just misses and hits
            computerBoardGrid.setAdapter(enemyImageAdapter);
        }
        else {  // it is the computer turn, use PlayerImageAdapter to display own grid with ships

            playerImageAdapter = new PlayerGridImageAdapter(this, this.game, this.playerTurn);
            playerBoardGrid.setAdapter(playerImageAdapter);
        }
    }


    private void attachActionListeners(){



        //boardGame GridView listener sets the aimedField property and changes aim color
        computerBoardGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageView iv = (ImageView) v;

                fire(position);




            }
        });

    }



    public void playGame() {


            if (playerTurn == 0) {
                displayArrangeGameScreen(playerTurn);
                directionsTextView.setText("Player 1, pick a cell to fire upon");
                attachActionListeners();
                Log.d("TEST", "ADDED ACTION LISTENERS");
            }

            else {
                displayArrangeGameScreen(playerTurn);
                int random = 0;
                while (game.getPlayerBoard(0).get(random) > 9 )
                    random = r.nextInt(99);
                myHandler.postDelayed(displayTurnResults, 1000);
                Log.d("TEST", "IS COMPUTER FIRING?");
                fire(random);


            }



    }


    public void fire(int position) {

        Log.d("TEST", "A FIRE OCCURRED");

        int result = this.game.processMove(playerTurn, position);

        Log.d("TEST_BATTLESHIP", "RESULT IS" + result);

        enemyImageAdapter.notifyDataSetChanged();

        ArrayList<Integer> boardContents = new ArrayList<>();
        boardContents = game.getPlayerBoard(game.getOpposite(playerTurn));

        Log.d("TEST_BATTLESHIP", "Board Grid is" + boardContents.toString());


        myHandler.postDelayed(displayTurnResults, 100);

        if (result > 4)  // it was a miss, switch turns
            playerTurn = game.getOpposite(playerTurn);
        else {
            if (!game.fleetStillAlive(playerTurn))
                processWinner(playerTurn);
        }

        Log.d("TEST_BATTLESHIP", "After Switching Turns, next Turn is" + playerTurn);

        playGame();


    }


    public void processWinner(int playerNumber) {

    }


    private Runnable displayTurnResults = new Runnable() {
        public void run() {

            if (playerTurn == 0)
                resultsTextView.setText("Player1 Results");
            else
                resultsTextView.setText("Computer Results");
            myHandler.postDelayed(this, 100);
            }

    };


}
