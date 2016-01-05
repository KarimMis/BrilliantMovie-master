
package app.karim.com.moveapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.karim.com.moveapp.models.FavouriteMovie;

import static com.activeandroid.Cache.getContext;



public class FavouriteMovieAdapter extends BaseAdapter {

    public ImageView iconView;
    private List<FavouriteMovie> mData;


    public FavouriteMovieAdapter(Activity context) {
        // TODO Auto-generated constructor stub

    }

    public List<FavouriteMovie> getData()
    {
        return mData;
    }

    public void setData(List<FavouriteMovie> data)
    {
        this.mData = data;
        if (data != null) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (mData != null && !mData.isEmpty()) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public FavouriteMovie getItem(int position) {
        // TODO Auto-generated method stub
        if (mData != null && !mData.isEmpty()) {
            return (FavouriteMovie) mData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        FavouriteMovie currentFavouriteMovie = mData.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_item, parent, false);
        }


        iconView = (ImageView) convertView.findViewById(R.id.movie_image);

        Picasso.with(getContext()).load(currentFavouriteMovie.imageUrl).into(iconView);


        return convertView;
    }




}
