package me.caesar2011.numberpuzzles.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.caesar2011.numberpuzzles.R;

/**
 * Created by Schulä on 01.03.2015.
 */
public class GameFragment extends BidirectionalFragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("::GameFragm-onCreateV", "Erstelle Liste");
        View rootView = (View) inflater.inflate(
                R.layout.game_page, container, false);
        rootView = configureView(rootView, getPositionX(), getPositionY());
        return rootView;
    }

    @Override
    protected View configureView(View rootView, int positionX, int positionY) {
        Log.v("::GameFragm-configureVx", String.valueOf(positionX));
        Log.v("::GameFragm-configureVy", String.valueOf(positionY));
        if (rootView instanceof TextView) {
            Log.v("::GameFragm-configureV_", "isTextView");
            TextView textView = ((TextView) rootView);
            textView.setText(String.valueOf(getPositionX()) + " " + String.valueOf(getPositionY()));
            return textView;
        } else {
            return rootView;
        }
    }
}
