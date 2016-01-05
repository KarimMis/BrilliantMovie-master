/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.karim.com.moveapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;




public class DetailActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {


            Bundle bundle= getIntent().getExtras();

            String MovieId =  bundle.getString(DetailFragment.DETAIL_DATA_MOVIE_ID);
            String MovieTitle = bundle.getString(DetailFragment.DETAIL_DATA_MOVIE_TITLE);
            String MovieOverview =bundle.getString(DetailFragment.DETAIL_DATA_MOVIE_OVERVIEW);
            String MovieVoteAverage = bundle.getString(DetailFragment.DETAIL_DATA_MOVIE_VOTE_AVERAGE);
            String MovieDate =bundle.getString(DetailFragment.DETAIL_DATA_MOVIE_RELEASE_DATE);
            String MovieImage = bundle.getString(DetailFragment.DETAIL_DATA_MOVIE_IMAGE);


            Bundle arguments = new Bundle();

            arguments.putString(DetailFragment.DETAIL_DATA_MOVIE_ID, MovieId );
            arguments.putString(DetailFragment.DETAIL_DATA_MOVIE_TITLE, MovieTitle);
            arguments.putString(DetailFragment.DETAIL_DATA_MOVIE_OVERVIEW, MovieOverview );
            arguments.putString(DetailFragment.DETAIL_DATA_MOVIE_VOTE_AVERAGE,  MovieVoteAverage );
            arguments.putString(DetailFragment.DETAIL_DATA_MOVIE_RELEASE_DATE, MovieDate );
            arguments.putString(DetailFragment.DETAIL_DATA_MOVIE_IMAGE, MovieImage);


            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
