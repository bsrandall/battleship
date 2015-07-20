package com.android.battleship; /**
 * Created by srandall on 7/12/15.
 */
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.battleship.R;

import java.util.ArrayList;
import java.util.Random;

public class PlayerGridImageAdapter extends BaseAdapter {

    private static final String TAG = SetupActivity.class.getSimpleName();

    private static Ship[] fleet;

    private static Game game;

    private int playerNumber, enemyNumber;

    private static ArrayList<Integer> board;

    private static final Random r = new Random();

    private static final int FIELDS_COUNT = 100;

    private final static Integer WATER = 99;
    private final static int FIRE_MISS = 61;
    private final static int FIRE_HIT = 62;

    private Context mContext;
    private ImageView[] boardFields = new ImageView[FIELDS_COUNT];

    private static final int[] drawableHorizontal = new int[]{
            R.drawable.destroyer1, R.drawable.destroyer2,
            R.drawable.sub1, R.drawable.sub2, R.drawable.sub3,
            R.drawable.bs1, R.drawable.bs2, R.drawable.bs3, R.drawable.bs4,
            R.drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5
    };


    private static final int[] drawableVertical = new int[]{
            R.drawable.destroyerv1, R.drawable.destroyerv2,
            R.drawable.subv1, R.drawable.subv2, R.drawable.subv3,
            R.drawable.bsv1, R.drawable.bsv2, R.drawable.bsv3, R.drawable.bsv4,
            R.drawable.cv1, R.drawable.cv2, R.drawable.cv3, R.drawable.cv4, R.drawable.cv5

    };


    public PlayerGridImageAdapter(Context c, Game sentGame, int player ) {

        game = new Game(sentGame);
        this.playerNumber = player;

        this.enemyNumber = game.getOpposite(player);
    }


    public int getCount() {
        return FIELDS_COUNT;
    }

    public Object getItem(int position) {
        return boardFields[position];
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        SquareImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new SquareImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(1, 1, 1, 1);
            imageView.requestLayout();
        } else {
            imageView = (SquareImageView) convertView;
        }


        imageView.setImageResource(0);
        imageView.setImageDrawable(null);
        imageView.setImageResource(getImageResource(position));

        boardFields[position] = imageView;
        return imageView;
    }


    public void drawRandom(int position, int imageType, int direction) {

    }



    // references to our images
    // references to our images
    private Integer[] boardPictures = {
            R.drawable.water,
            R.drawable.destroyer1, R.drawable.destroyer2,
            R.drawable.sub1, R.drawable.sub2, R.drawable.sub3,
            R.drawable.bs1, R.drawable.bs2, R.drawable.bs3, R.drawable.bs4,
            R. drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5
    };



    public int getImageResource(int position) {

        int imageResource = R.drawable.white;
        int boardValue = game.getBoardCellValueByPlayer(this.playerNumber, position);

        if (boardValue < 50) {

            boolean isHorizontal = checkIfHorizontal(position);

            int shipNumber = boardValue / 10;
            int shipSectionNumber = boardValue%10;

            if (isHorizontal) {
                if (shipNumber < 1)
                    imageResource = drawableHorizontal[shipSectionNumber];
                else if (shipNumber == 1)
                    imageResource = drawableHorizontal[(shipNumber * 2) + shipSectionNumber];
                else if (shipNumber == 2)
                    imageResource = drawableHorizontal[(shipNumber * 2) + shipSectionNumber + 1];
                else if (shipNumber == 3)
                    imageResource = drawableHorizontal[(shipNumber * 3) + shipSectionNumber];

            } else {  // Vertical
                if (shipNumber < 1)
                    imageResource = drawableVertical[shipSectionNumber];
                else if (shipNumber == 1)
                    imageResource = drawableVertical[(shipNumber * 2) + shipSectionNumber];
                else if (shipNumber == 2)
                    imageResource = drawableVertical[(shipNumber * 2) + shipSectionNumber + 1];
                else if (shipNumber == 3)
                    imageResource = drawableVertical[(shipNumber * 3) + shipSectionNumber];

            }

            }


        if (boardValue == FIRE_MISS) { /// this is fire hit or miss
            imageResource = R.drawable.black;
        }

        else if(boardValue == FIRE_HIT)
            imageResource = R.drawable.fire;

        else if (boardValue == WATER)
            imageResource = R.drawable.white;


        return imageResource;

    }

    protected boolean checkIfHorizontal( int position) {
        boolean isHorizontal = false;
        int firstValue = game.getBoardCellValueByPlayer(this.playerNumber, position);
        int plusOneValue = game.getBoardCellValueByPlayer(this.playerNumber, position + 1);
        int minusOneValue = game.getBoardCellValueByPlayer(this.playerNumber, position - 1);

        if (plusOneValue - firstValue == 1 | firstValue - minusOneValue == 1| plusOneValue == FIRE_HIT | minusOneValue == FIRE_HIT)
            isHorizontal = true;

        return isHorizontal;

    }




}

