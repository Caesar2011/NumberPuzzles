package me.caesar2011.numberpuzzles.views;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

/**
 * Created by Schulä on 28.02.2015.
 */
public abstract class BidirectionalPagerAdapter extends FragmentStatePagerAdapter {

    //private static final String[] PAGES = {"Solver", "Beginner", "Easy", "Intermediate", "Advanced", "Brutal"};
    //private String puzzleType = "";


    public BidirectionalPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /*@Override
    public Fragment getItem(int position) {
        Log.v("::ScreenSlide-getItem", String.valueOf(position));
        ScreenSlidePageFragment f = new ScreenSlidePageFragment();
        Bundle bdl = new Bundle(2);
        bdl.putString(ScreenSlidePageFragment.ARG_PUZ_TYPE, puzzleType);
        bdl.putInt(ScreenSlidePageFragment.ARG_DIFFICULTY, position);
        f.setArguments(bdl);
        return f;
    }*/

    @Override
    public final Fragment getItem(int position) {
        return getItem();
    }

    public abstract BidirectionalFragment getItem();

    @Override
    public final int getCount() {
        return getCountX();
    }

    public abstract int getCountX();

    public abstract int getCountY(int posX);

    @Override
    public final CharSequence getPageTitle(int position) {
        return getPageTitleX(position);
    }

    public abstract CharSequence getPageTitleX(int posX);

    public abstract CharSequence getPageTitleY(int posX, int posY);
}
