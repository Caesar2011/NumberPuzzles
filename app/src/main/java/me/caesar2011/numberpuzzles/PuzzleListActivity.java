package me.caesar2011.numberpuzzles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import me.caesar2011.numberpuzzles.views.BidirectionalViewPager;
import me.caesar2011.numberpuzzles.views.GameFragment;
import me.caesar2011.numberpuzzles.views.GamePagerAdapter;


/**
 * An activity representing a list of Puzzles. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PuzzleDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link PuzzleListFragment} and the item details
 * (if present) is a {@link PuzzleDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link PuzzleListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class PuzzleListActivity extends ActionBarActivity
        implements PuzzleListFragment.Callbacks {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private BidirectionalViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private GamePagerAdapter mPagerAdapter;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_list);

        if (findViewById(R.id.pager) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((PuzzleListFragment) getFragmentManager()
                    .findFragmentById(R.id.puzzle_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link PuzzleListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            /*Bundle arguments = new Bundle();
            arguments.putString(PuzzleDetailFragment.ARG_ITEM_ID, id);
            PuzzleDetailFragment fragment = new PuzzleDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.puzzle_detail_container, fragment)
                    .commit();*/

            mPager = (BidirectionalViewPager) findViewById(R.id.pager);
            mPagerAdapter = new GamePagerAdapter(getFragmentManager());
            mPager.setAdapter(mPagerAdapter);
            mPager.setCurrentItem(1);

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, PuzzleDetailActivity.class);
            //detailIntent.putExtra(GameFragment.ARG_PUZ_TYPE, id);
            startActivity(detailIntent);
        }
    }
}
