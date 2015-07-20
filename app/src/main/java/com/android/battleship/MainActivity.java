package com.android.battleship;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    private Button buttonTwoPlayer;
    private Button buttonComputer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }



    public void addButtonListener() {

        buttonTwoPlayer = (Button) findViewById(R.id.button_1p);
        buttonTwoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SetupActivity.class);
                i.putExtra("playComputer", false);
                finish();
                startActivity(i);
            }
        });

        buttonComputer = (Button) findViewById(R.id.button_2p);
        buttonComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SetupActivity.class);
                i.putExtra("playComputer", true);
                finish();
                startActivity(i);

            }
        });

    }


}
