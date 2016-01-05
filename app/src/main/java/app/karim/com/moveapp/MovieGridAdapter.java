package app.karim.com.moveapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieGridAdapter extends ArrayAdapter<MovieItem> {
    private static final String LOG_TAG = MovieGridAdapter.class.getSimpleName();
    private ArrayList<MovieItem> mMovieData = new ArrayList<MovieItem>();

    public ImageView iconView;

    public MovieGridAdapter(Activity context, ArrayList<MovieItem> movieItems) {

        super(context, 0, movieItems);
    }

    public void setGridData(ArrayList<MovieItem> mGridData) {
        this.mMovieData = mGridData;
        notifyDataSetChanged();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


     MovieItem movieitem = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_item, parent, false);
        }


         iconView = (ImageView) convertView.findViewById(R.id.movie_image);

        Picasso.with(getContext()).load(movieitem.getImage()).into(iconView);


        return convertView;
    }


}
