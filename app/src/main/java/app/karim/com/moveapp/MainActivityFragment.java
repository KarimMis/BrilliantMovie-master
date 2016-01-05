package app.karim.com.moveapp;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.karim.com.moveapp.models.FavouriteMovie;

public class MainActivityFragment extends Fragment {


    private ArrayList<MovieItem> mGridData;
    public MovieGridAdapter movieGridAdapter;
    View rootView;
    static GridView gridViewMovies;
    static public FavouriteMovieAdapter FavouriteMovieAdapter;


    public MainActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        mGridData = new ArrayList<>();

        setHasOptionsMenu(true);


    }


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(String MovieID, String original_title, String overview, String vote_average, String release_date, String image);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_refresh) {

            updateMovies();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridViewMovies = (GridView) rootView.findViewById(R.id.flavors_grid);
        // Get a reference to the ListView, and attach this adapter to it.


//        updateMovies();


        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateMovies();
    }

    @Override
    public void onStart() {
        super.onStart();


        updateMovies();


    }








    @Override
    public void onResume() {
        super.onResume();

        updateMovies();
    }

    private void updateMovies() {



        FetchMovieDataTask movieTask = new FetchMovieDataTask();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String MovieSortedBy = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sortedby_popular));


        if (MovieSortedBy.equals("favourite"))

        {

                final List<FavouriteMovie> movies = new Select().from(FavouriteMovie.class).execute();

                FavouriteMovieAdapter = new FavouriteMovieAdapter(getActivity());
                FavouriteMovieAdapter.setData(movies);
                gridViewMovies.setAdapter(FavouriteMovieAdapter);
try {

    if (MainActivity.mTwoPane && MovieSortedBy.equals("favourite")) {

        ((Callback) getActivity()).onItemSelected(movies.get(0).movieId, movies.get(0).originalTitle,
                movies.get(0).overview, movies.get(0).voteAverage, movies.get(0).releaseDate, movies.get(0).imageUrl);

    }

} catch (Exception e){
    Toast.makeText(getActivity(),"Please select movies  and add it to yours favourite list",Toast.LENGTH_LONG).show();
}

            gridViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                 final List<FavouriteMovie> movies = new Select().from(FavouriteMovie.class).execute();

                    FavouriteMovieAdapter = new FavouriteMovieAdapter(getActivity());
                    FavouriteMovieAdapter.setData(movies);
                    gridViewMovies.setAdapter(FavouriteMovieAdapter);

                    FavouriteMovie data = movies.get(position);
                    String movieId = data.movieId;
                    String originalTitle = data.originalTitle;
                    String overView = data.overview;
                    String voteAverage = data.voteAverage;
                    String releaseDate = data.releaseDate;
                    String imageUrl = data.imageUrl;


                    ((Callback) getActivity())
                            .onItemSelected(movieId, originalTitle, overView, voteAverage, releaseDate, imageUrl);


                }
            });


        } else {

            movieTask.execute(MovieSortedBy);
            movieGridAdapter = new MovieGridAdapter(getActivity(), mGridData);
            gridViewMovies.setAdapter(movieGridAdapter);

            gridViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    MovieItem item = (MovieItem) parent.getItemAtPosition(position);

                    ((Callback) getActivity())
                            .onItemSelected(item.getMovie_id(), item.getOriginal_title(), item.getOverview(), item.getVote_average(), item.getRelease_date(), item.getImage());

                }


            });
        }

    }


    public class FetchMovieDataTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchMovieDataTask.class.getSimpleName();


        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            String API = "696a2388a673c61ce09d5781cfc11cc9";


            try {

                final String MOVIE_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, params[0])
                        .appendQueryParameter(APPID_PARAM, API)
                        .build();


                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
                Log.v(LOG_TAG, "=" + movieJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return movieJsonStr;

        }

        @Override
        protected void onPostExecute(String movieJsonStr) {


            // These are the names of the JSON objects that need to be extracted.
            final String OWM_RESULTS = "results";
            final String OWM_ORIGINAL_TITLE = "original_title";
            final String OWM_POSTER_PATH = "poster_path";
            final String OWM_OVERVIEW = "overview";
            final String OWM_VOTE_AVERAGE = "vote_average";
            final String OWM_RELEASE_DATE = "release_date";
            final String OWM_MOVIE_ID = "id";


            JSONObject movieJson = null;
            JSONArray resultArray = null;
            try {
                movieJson = new JSONObject(movieJsonStr);
                resultArray = movieJson.getJSONArray(OWM_RESULTS);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String ImageURL;

            MovieItem item;
            String original_title = null;
            String image_thumbnail = null;
            String overview = null;
            String vote_average = null;
            String release_date = null;
            String movie_id = null;

            movieGridAdapter.clear();

            for (int i = 0; i < resultArray.length(); i++) {

                // Get the JSON object representing the FavouriteMovie
                JSONObject MovieData = null;
                try {
                    MovieData = resultArray.getJSONObject(i);
                    original_title = MovieData.getString(OWM_ORIGINAL_TITLE);
                    overview = MovieData.getString(OWM_OVERVIEW);
                    vote_average = MovieData.getString(OWM_VOTE_AVERAGE);
                    release_date = MovieData.getString(OWM_RELEASE_DATE);

                    image_thumbnail = MovieData.getString(OWM_POSTER_PATH);
                    movie_id = MovieData.getString(OWM_MOVIE_ID);

                } catch (Exception e) {
                }
                if(!image_thumbnail.isEmpty() && !image_thumbnail.equals("null")) {
                    item = new MovieItem();
                    item.setOriginal_title(original_title);
                    item.setOverview(overview);
                    item.setVote_average(vote_average);
                    item.setRelease_date(release_date);
                    ImageURL = "http://image.tmdb.org/t/p/w185//" + image_thumbnail;
                    item.setImage(ImageURL);
                    item.setMovie_id(movie_id);
                    mGridData.add(item);
                    movieGridAdapter.setGridData(mGridData);
                }
            }

            if (MainActivity.mTwoPane) {
                ((Callback) getActivity()).onItemSelected(mGridData.get(0).getMovie_id(), mGridData.get(0).getOriginal_title(),
                        mGridData.get(0).getOverview(), mGridData.get(0).getVote_average(), mGridData.get(0).getRelease_date(),
                        mGridData.get(0).getImage());
            }


        }
    }
    }

