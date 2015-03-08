package me.caesar2011.numberpuzzles.views;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


/**
 * Created by Schulä on 28.02.2015.
 */
public abstract class BidirectionalFragment extends Fragment {


    public static final String ARG_POSITION_X = "positionX";
    public static final String ARG_POSITION_Y = "positionY";

    private int positionX;
    private int positionY;

    public void setPosition(int posX, int posY) {
        positionX = posX;
        positionY = posY;
        Log.v("::BidiFragm-setPosition", "true");
        configureView(positionX, positionY);
    }

    public void setPositionX(int posX) {
        positionX = posX;
        configureView(positionX, positionY);
    }

    public void setPositionY(int posY) {
        positionY = posY;
        configureView(positionX, positionY);
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        getArgs(savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState = putArgs(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v("::ScreenFragm-onCreate", "true");
        getArgs(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setArguments(Bundle args) {
        args = putArgs(args);
        super.setArguments(args);
    }

    private Bundle putArgs(Bundle args) {
        args.putInt(ARG_POSITION_X, positionX);
        args.putInt(ARG_POSITION_Y, positionY);
        return args;
    }

    private void getArgs(Bundle args) {
        if (args != null) {
            positionX = args.getInt(ARG_POSITION_X, 0);
            positionY = args.getInt(ARG_POSITION_Y, 0);
            Log.v("::BidiFragm-getArgs", "true");
            configureView(positionX, positionY);
        }
    }

    protected void configureView(int positionX, int positionY) {
        configureView(getView(), positionX, positionY);
    }

    protected abstract View configureView(View rootView, int positionX, int positionY);
}
