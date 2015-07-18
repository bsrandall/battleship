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

public class ImageAdapter extends BaseAdapter {

    private static final String TAG = SetupActivity.class.getSimpleName();

    private static Ship[] fleet;

    private static final Random r = new Random();

    private static final int FIELDS_COUNT = 100;

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


    public ImageAdapter(Context c, Ship[] ships ) {

        this.fleet = new Ship[4];
        for (int i=0; i < 4; ++i) {
            this.fleet[i] = new Ship(ships[i]);
        }


        mContext = c;
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

        for (int shipCount = 0; shipCount < fleet.length; ++shipCount) {  // for each ship

            ArrayList<Integer> shipCoordinates = new ArrayList<Integer>(fleet[shipCount].getLength());

            shipCoordinates.addAll(fleet[shipCount].getCoordinates());



         //   Log.d(TAG, "Ship" + shipCount + "Coordinates are " + shipCoordinates.toString());

            if (shipCoordinates.indexOf(position) != -1) {
                int j = shipCoordinates.indexOf(position);

                if (shipCoordinates.get(1) - shipCoordinates.get(0) == 1) {
                    if (shipCount < 1)
                        imageResource = drawableHorizontal[j];
                    else if (shipCount == 1)
                        imageResource = drawableHorizontal[(shipCount * 2) + j];
                    else if (shipCount == 2)
                        imageResource = drawableHorizontal[(shipCount * 2) + j + 1];
                    else if (shipCount == 3)
                        imageResource = drawableHorizontal[(shipCount * 3) + j];

                } else {  // Vertical
                    if (shipCount < 1)
                        imageResource = drawableVertical[j];
                    else if (shipCount == 1)
                        imageResource = drawableVertical[(shipCount * 2) + j];
                    else if (shipCount == 2)
                        imageResource = drawableVertical[(shipCount * 2) + j + 1];
                    else if (shipCount == 3)
                        imageResource = drawableVertical[(shipCount * 3) + j];

                }
                break;
            }

        }

        for (int shipCount = 0; shipCount < fleet.length; ++shipCount) {  // for each ship

            ArrayList<Integer> shipHits = new ArrayList<Integer>(fleet[shipCount].getNumberOfHits());

            shipHits.addAll(fleet[shipCount].getHits());

            if (shipHits.indexOf(position) != -1) {
                imageResource = R.drawable.fire;
            }
        }

            ArrayList<Integer> misses = new ArrayList<Integer>(Ship.getNumberOfMisses());

           misses.addAll(Ship.getMisses());

            if (misses.indexOf(position) != -1) {
                imageResource = R.drawable.fire;
            }


        return imageResource;

    }




}

