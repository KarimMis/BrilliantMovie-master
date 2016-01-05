package app.karim.com.moveapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;
import com.google.android.youtube.player.YouTubeIntents;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import app.karim.com.moveapp.models.FavouriteMovie;



public class DetailFragment extends Fragment {


    static Intent shareIntent;

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();


    static  String DETAIL_DATA_MOVIE_ID = "ID";
    static  String DETAIL_DATA_MOVIE_TITLE = "TITLE";
    static  String DETAIL_DATA_MOVIE_OVERVIEW = "OVERVIEW";
    static  String DETAIL_DATA_MOVIE_VOTE_AVERAGE = "VOTE_AVERAGE";
    static  String DETAIL_DATA_MOVIE_RELEASE_DATE = "DATE";
    static  String DETAIL_DATA_MOVIE_IMAGE = "IMAGE";


    ProgressDialog dialog;
    ToggleButton tg;
    ImageView myImageplay;
    JSONArray resultArrayofReviews = null;
    JSONArray resultArrayofMovieTrailer = null;
    MovieItem  item = new MovieItem() ;

    String MovieId;
    String Title, ImageUrl, OverView, ReleaseData;
    String VoteAverage;

    private ImageView imageView;
    private TextView titleTextView, overViewTextView, averageTextView, ReleaseDateTextView;
    private View rootView;
    private Context context;


    public DetailFragment() {
        setHasOptionsMenu(true);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the menu; this adds items to the action bar if it is present.






        Bundle arguments =getArguments();
        if (arguments != null) {
            MovieId = arguments.getString(DetailFragment.DETAIL_DATA_MOVIE_ID);
            Title = arguments.getString(DetailFragment.DETAIL_DATA_MOVIE_TITLE);
            OverView = arguments.getString(DetailFragment.DETAIL_DATA_MOVIE_OVERVIEW);
            VoteAverage = arguments.getString(DetailFragment.DETAIL_DATA_MOVIE_VOTE_AVERAGE);
            ReleaseData = arguments.getString(DetailFragment.DETAIL_DATA_MOVIE_RELEASE_DATE);
            ImageUrl = arguments.getString(DetailFragment.DETAIL_DATA_MOVIE_IMAGE);
        }




        rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if(!MainActivity.mTwoPane) {
            getActivity().setTitle(Title);
        }


        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FetchMovieReview movieReview = new FetchMovieReview();
        FetchMovieTrailer movieTrailer = new FetchMovieTrailer();



        movieTrailer.execute();
        movieReview.execute();




        tg = (ToggleButton)rootView.findViewById(R.id.toggleButton);


        List<FavouriteMovie> movies = new Select()
                .all()
                .from(FavouriteMovie.class)
                .execute();

        for (int i =0 ; i<movies.size();i++){

            FavouriteMovie data =  movies.get(i);
            String grappedID= data.movieId;
            if(grappedID.equals(MovieId)){
                tg.setChecked(true);
            }

        }



        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        titleTextView = (TextView) rootView.findViewById(R.id.movieTitleText);
        overViewTextView = (TextView) rootView.findViewById(R.id.overviewText);
        averageTextView = (TextView) rootView.findViewById(R.id.voteAverageText);
        ReleaseDateTextView = (TextView) rootView.findViewById(R.id.releaseDateText);

        Picasso.with(getActivity()).load(ImageUrl).into(imageView);
        titleTextView.setText(Title);
        overViewTextView.setText(OverView);
        averageTextView.setText(VoteAverage+" / 10");
        ReleaseDateTextView.setText(ReleaseData);



        tg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(tg.isChecked()==true) {

                    Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();

                    Insert();

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String MovieSortedBy = prefs.getString(getString(R.string.pref_sort_key),
                            getString(R.string.pref_sortedby_popular));

                    if(MovieSortedBy.equals("favourite"))

                    {


                        final List<FavouriteMovie> movies = new Select().from(FavouriteMovie.class).execute();
                        FavouriteMovieAdapter FavouriteMovieAdapter = new FavouriteMovieAdapter(getActivity());
                        FavouriteMovieAdapter.setData(movies);
                        FavouriteMovieAdapter.notifyDataSetChanged();
                        MainActivityFragment.gridViewMovies.invalidateViews();
                        MainActivityFragment.gridViewMovies.setAdapter(FavouriteMovieAdapter);

                    }



                }


                else if (tg.isChecked()==false) {


                    Toast.makeText(getActivity(), "Removed", Toast.LENGTH_SHORT).show();

                    SQLiteUtils.execSql("DELETE FROM FavouriteMovie WHERE movieId="+MovieId);

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String MovieSortedBy = prefs.getString(getString(R.string.pref_sort_key),
                            getString(R.string.pref_sortedby_popular));

                    if(MovieSortedBy.equals("favourite"))

                    {
                        final List<FavouriteMovie> movies = new Select().from(FavouriteMovie.class).execute();
                        FavouriteMovieAdapter FavouriteMovieAdapter = new FavouriteMovieAdapter(getActivity());
                        FavouriteMovieAdapter.setData(movies);
                        FavouriteMovieAdapter.notifyDataSetChanged();
                        MainActivityFragment.gridViewMovies.invalidateViews();
                        MainActivityFragment.gridViewMovies.setAdapter(FavouriteMovieAdapter);

                    }
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String MovieSortedBy = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sortedby_popular));

        if(MovieSortedBy.equals("favourite"))

        {


            final List<FavouriteMovie> movies = new Select().from(FavouriteMovie.class).execute();
            FavouriteMovieAdapter FavouriteMovieAdapter = new FavouriteMovieAdapter(getActivity());
            FavouriteMovieAdapter.setData(movies);
            FavouriteMovieAdapter.notifyDataSetChanged();
            MainActivityFragment.gridViewMovies.invalidateViews();
            MainActivityFragment.gridViewMovies.setAdapter(FavouriteMovieAdapter);

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String MovieSortedBy = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sortedby_popular));

        if(MovieSortedBy.equals("favourite"))

        {


            final List<FavouriteMovie> movies = new Select().from(FavouriteMovie.class).execute();
            FavouriteMovieAdapter FavouriteMovieAdapter = new FavouriteMovieAdapter(getActivity());
            FavouriteMovieAdapter.setData(movies);
            FavouriteMovieAdapter.notifyDataSetChanged();
            MainActivityFragment.gridViewMovies.invalidateViews();
            MainActivityFragment.gridViewMovies.setAdapter(FavouriteMovieAdapter);

        }
    }









    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.detailfragment, menu);
//        MenuItem menuItem = menu.findItem(R.id.action_share);





    }



    @Override
    public boolean onOptionsItemSelected(MenuItem menue) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = menue.getItemId();


        if (id == R.id.action_share) {



            ShareActionProvider mShareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(menue);

            try {
                shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Wow dude , Watch ("+Title+") trailer");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + item.getMovie_keys()[0]);
               } catch (Exception e) {
                      Toast.makeText(getActivity(), "Sorry... there isn't Video to share", Toast.LENGTH_LONG).show();
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hey dude do you know anything about("+Title+")movie?");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "No URL video founded to share");

                                }

                if (mShareActionProvider != null) {

                    mShareActionProvider.setShareIntent(shareIntent);
                } else {
                    Log.d(LOG_TAG, "Share Action Provider is null?");
                }

            return true;
        }


        return super.onOptionsItemSelected(menue);
    }





    public class FetchMovieTrailer extends AsyncTask<Void, Void, String> {


        private final String LOG_TAG = FetchMovieTrailer.class.getSimpleName();





        @Override
        protected void onPreExecute() {
            super.onPreExecute();



            dialog = new ProgressDialog(getActivity());
            dialog.setCancelable(true);
            dialog.setMessage("Please wait ... Loading the movie");
            dialog.show();


        }


        @Override
        protected String doInBackground(Void... params) {





            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            String API = "696a2388a673c61ce09d5781cfc11cc9";


            try {

                final String MOVIE_BASE_URL =
                        "http://api.themoviedb.org/3/movie/" + MovieId + "/videos?";
                final String APPID_PARAM = "api_key";


                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
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
                Log.v(LOG_TAG, "MovieVideo:" + movieJsonStr);

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
            final String OWM_MOVIE_VIDEO_KEY = "key";
            final String OWM_MOVIE_VIDEO_NAME = "name";


            JSONObject movieJson = null;


            try {
                movieJson = new JSONObject(movieJsonStr);
                resultArrayofMovieTrailer = movieJson.getJSONArray(OWM_RESULTS);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            String[] movie_keys = new String[resultArrayofMovieTrailer.length()];
            String[] video_names = new String[resultArrayofMovieTrailer.length()];



            for (int i = 0; i < resultArrayofMovieTrailer.length(); i++) {

                // Get the JSON object representing the FavouriteMovie
                JSONObject MovieData = null;
                try {

                    MovieData = resultArrayofMovieTrailer.getJSONObject(i);
                    movie_keys[i] = MovieData.getString(OWM_MOVIE_VIDEO_KEY);
                    video_names[i] = MovieData.getString(OWM_MOVIE_VIDEO_NAME);


                } catch (Exception e) {
                }


                item.setMovie_keys(movie_keys);
                item.setMovie_videos_names(video_names);

                Log.v(LOG_TAG, "FavouriteMovie Keys Dataaa" + movie_keys.toString());


            }


            if (resultArrayofMovieTrailer != null ) {
                if(resultArrayofMovieTrailer.length() != 0){
                    createDynamicButton();

                }



            }





        }


    }





    public class FetchMovieReview extends AsyncTask<Void, Void, String> {



        @Override
        protected String doInBackground(Void... params) {
            String MovieReviews = null;
            String API = "696a2388a673c61ce09d5781cfc11cc9";

            HttpURLConnection urlConnection2 = null;
            BufferedReader reader2 = null;

            // Will contain the raw JSON response as a string.


            try {


                final String APPID_PARAM = "api_key";

                final String MOVIE_BASE_URL2 = "http://api.themoviedb.org/3/movie/" + MovieId + "/reviews?";

                Uri builtUri2 = Uri.parse(MOVIE_BASE_URL2).buildUpon()
                        .appendQueryParameter(APPID_PARAM, API)
                        .build();


                URL url2 = new URL(builtUri2.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection2 = (HttpURLConnection) url2.openConnection();
                urlConnection2.setRequestMethod("GET");
                urlConnection2.connect();

                // Read the input stream into a String
                InputStream inputStream2 = urlConnection2.getInputStream();
                StringBuffer buffer2 = new StringBuffer();
                if (inputStream2 == null) {
                    // Nothing to do.
                    return null;
                }
                reader2 = new BufferedReader(new InputStreamReader(inputStream2));

                String line2;
                while ((line2 = reader2.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer2.append(line2 + "\n");
                }

                if (buffer2.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                MovieReviews = buffer2.toString();
                Log.v(LOG_TAG, "MovieReviews:" + MovieReviews);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection2 != null) {
                    urlConnection2.disconnect();
                }
                if (reader2 != null) {
                    try {
                        reader2.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return MovieReviews;
        }

        @Override
        protected void onPostExecute(String movieJsonStr) {


            // These are the names of the JSON objects that need to be extracted.
            final String OWM_RESULTS = "results";
            final String OWM_MOVIE_AUTHOR = "author";
            final String OWM_MOVIE_AUTHOR_CONTENT = "content";


            JSONObject movieJson = null;


            try {
                movieJson = new JSONObject(movieJsonStr);
                resultArrayofReviews = movieJson.getJSONArray(OWM_RESULTS);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            String[] movie_authors = new String[resultArrayofReviews.length()];
            String[] movie_authors_content = new String[resultArrayofReviews.length()];




            for (int i = 0; i < resultArrayofReviews.length(); i++) {

                // Get the JSON object representing the FavouriteMovie
                JSONObject MovieData = null;
                try {

                    MovieData = resultArrayofReviews.getJSONObject(i);
                    movie_authors[i] = MovieData.getString(OWM_MOVIE_AUTHOR);
                    movie_authors_content[i] = MovieData.getString(OWM_MOVIE_AUTHOR_CONTENT);

                } catch (Exception e) {
                }


                item.setMovie_authors(movie_authors);
                item.setMovie_authors_content(movie_authors_content);
                Log.v(LOG_TAG, "FavouriteMovie author Dataaa" + movie_authors.toString());


            }
            if (resultArrayofReviews.length() != 0) {

                CreateDynamicTextReview();

            }
            dialog.dismiss();
        }

    }


    public void CreateDynamicTextReview() {


        LinearLayout TextLayout = (LinearLayout) rootView.findViewById(R.id.TextLayout);
        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lllp.gravity = Gravity.LEFT;
        TextLayout.setLayoutParams(lllp);

        LinearLayout.LayoutParams lp;
        if(MainActivity.mTwoPane) {
          lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 3);
        }else {
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 10);
        }

       View space = new View(context);
        LinearLayout.LayoutParams spaceLay = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,10);
        space.setLayoutParams(spaceLay);



         View lineView = new View(context);
        lineView.setLayoutParams(lp);
        lineView.setBackgroundColor(context.getResources().getColor(R.color.Linecolor));

        TextView Headline = new TextView(context);


        Headline.setTextAppearance(context,android.R.style.TextAppearance_Large);
        Headline.setText("Movie Reviews");


        TextLayout.addView(lineView);
        TextLayout.addView(Headline);
        TextLayout.addView(space);

        for (int i = 0; i < resultArrayofReviews.length(); i++) {


            TextView Authother = new TextView(context);
            Authother.setTextSize(18);
            Authother.setTextAppearance(context,android.R.style.TextAppearance_DeviceDefault_Medium);
            Authother.setText("Author \r:");

            TextView AuthotherText = new TextView(context);
            AuthotherText.setTextSize(15);
            AuthotherText.setText(item.getMovie_authors()[i]);


            TextView Content = new TextView(context);
            Content.setTextSize(18);
            Content.setTextAppearance(context,android.R.style.TextAppearance_DeviceDefault_Medium);
            Content.setText("Review \r:");

            TextView ContentText = new TextView(context);
            ContentText.setTextSize(15);
            ContentText.setText( item.getMovie_authors_content()[i]);

            TextLayout.addView(Authother);
            TextLayout.addView(AuthotherText);
            TextLayout.addView(Content);
            TextLayout.addView(ContentText);

            View spaceText = new View(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,10);
            spaceText.setLayoutParams(params);
            TextLayout.addView(spaceText);


            View lightLine = new View(context);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 1);
            lightLine.setLayoutParams(llp);
            lightLine.setBackgroundColor(context.getResources().getColor(R.color.Linecolor));
            TextLayout.addView(lightLine);




        }

    }

    public void createDynamicButton() {


        LinearLayout ImageButtonsLayout = (LinearLayout) rootView.findViewById(R.id.ImageButtonLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageButtonsLayout.setLayoutParams(lp);


        View space = new View(context);
        View space2 = new View(context);


        LinearLayout.LayoutParams spaceLay = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,10);
        space.setLayoutParams(spaceLay);




        TextView Headline = new TextView(context);
        Headline.setTextAppearance(context,android.R.style.TextAppearance_Large);

        Headline.setText("Movie Trailers");



        ImageButtonsLayout.addView(Headline);
        ImageButtonsLayout.addView(space);


        LinearLayout.LayoutParams ButtonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < resultArrayofMovieTrailer.length(); i++) {


            TextView TrailerTextName = new TextView(context);

            TrailerTextName.setTextSize(15);

            TrailerTextName.setText(item.getMovie_videos_names()[i]);


            myImageplay = new ImageView(context);


            myImageplay.setLayoutParams(ButtonParams);

           // myImageplay.setPadding(0, 0, 200, 0);//850



            try
            {
                String[] allKeys = item.getMovie_keys();
                String OneKey = allKeys[i];



                String img_url="http://img.youtube.com/vi/"+OneKey+"/mqdefault.jpg"; // this is link which will give u thumnail image of that video

                // picasso jar file download image for u and set image in imagview

                Picasso.with(context)
                        .load(img_url).resize(400,200)
                        .into(myImageplay);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            ImageButtonsLayout.addView(TrailerTextName);
            ImageButtonsLayout.addView(myImageplay);












            final int num = i;


            myImageplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    try {

                        String[] allKeys = item.getMovie_keys();
                        String OneKey = allKeys[num];

                        Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(context, OneKey, true, true);
                        startActivity(intent);

                    } catch (Exception e) {
                        Toast.makeText(context, "Opps... No video available " , Toast.LENGTH_LONG).show();
                    }
                }

            });

        }

       LinearLayout.LayoutParams spaceLay2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,15);
        space2.setLayoutParams(spaceLay2);

        ImageButtonsLayout.addView(space2);


    }

    public void Insert()
    {

        // Create a new score object with the 4 peices of data extracted from
        // their respective EditText and converted to an Integer
        try {
            FavouriteMovie favouriteMovie = new FavouriteMovie( MovieId,Title,OverView,VoteAverage,ReleaseData,ImageUrl);


            // Save the score object to its own table first.
            favouriteMovie.save();



        } catch (NumberFormatException e) {
            Log.e(MyApplication.TAG,
                    "Error while converting String to Integer");
        }

    }

}