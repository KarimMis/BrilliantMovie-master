package app.karim.com.moveapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;


public class MainActivity extends ActionBarActivity implements MainActivityFragment.Callback {

    public static boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    JSONArray resultArrayofMovieTrailer = null;
    MovieItem  item = new MovieItem() ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_list, new MainActivityFragment(),DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.movie);


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(intent);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(String MovieID, String original_title, String overview, String vote_average, String release_date, String image) {

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putString(DetailFragment.DETAIL_DATA_MOVIE_ID,MovieID );
            args.putString(DetailFragment.DETAIL_DATA_MOVIE_TITLE,original_title );
            args.putString(DetailFragment.DETAIL_DATA_MOVIE_OVERVIEW,overview );
            args.putString(DetailFragment.DETAIL_DATA_MOVIE_VOTE_AVERAGE,vote_average );
            args.putString(DetailFragment.DETAIL_DATA_MOVIE_RELEASE_DATE,release_date );
            args.putString(DetailFragment.DETAIL_DATA_MOVIE_IMAGE,image );

//
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailFragment.DETAIL_DATA_MOVIE_ID,MovieID);
            intent.putExtra(DetailFragment.DETAIL_DATA_MOVIE_TITLE,original_title);
            intent.putExtra(DetailFragment.DETAIL_DATA_MOVIE_OVERVIEW,overview);
            intent.putExtra(DetailFragment.DETAIL_DATA_MOVIE_VOTE_AVERAGE,vote_average);
            intent.putExtra(DetailFragment.DETAIL_DATA_MOVIE_RELEASE_DATE,release_date);
            intent.putExtra(DetailFragment.DETAIL_DATA_MOVIE_IMAGE,image);

            startActivity(intent);
        }

    }


}
