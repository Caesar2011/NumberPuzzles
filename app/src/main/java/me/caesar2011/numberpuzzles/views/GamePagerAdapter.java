package me.caesar2011.numberpuzzles.views;

import android.app.FragmentManager;

/**
 * Created by Schulä on 28.02.2015.
 */
public class GamePagerAdapter extends BidirectionalPagerAdapter {

    private static final String[] PAGES = {"Solver", "Beginner", "Easy", "Intermediate", "Advanced", "Brutal"};

    public GamePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public BidirectionalFragment getItem(){
            return new GameFragment();
    }

    @Override
    public int getCountX() {
        return PAGES.length;
    }

    @Override
    public int getCountY(int posX) {
        /*return -1;*/
        return 100000;
    }

    @Override
    public CharSequence getPageTitleX(int posX) {
        return PAGES[posX];
    }

    @Override
    public CharSequence getPageTitleY(int posX, int posY) {
        return "Spiel "+posY;
    }
}
