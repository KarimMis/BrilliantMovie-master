
package app.karim.com.moveapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


   @Table(name = "FavouriteMovie")

    public class FavouriteMovie extends Model {

    @Column(name = "movieId",unique = true,onUniqueConflict = Column.ConflictAction.REPLACE)
    public String movieId;


    @Column(name = "originalTitle")
    public String originalTitle;

    @Column(name = "overview")
    public String overview;

    @Column(name = "voteAverage")
    public String voteAverage;

    @Column(name = "releaseDate")
    public String releaseDate;

    @Column(name = "imageUrl")
    public String imageUrl;



    public FavouriteMovie()
    {
        super();
    }

    public FavouriteMovie(String movieId,
                          String originalTitle,
                          String overview,
                          String voteAverage,
                          String releaseDate,
                          String imageUrl )
    {
        super();
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate=releaseDate;
        this.imageUrl =imageUrl;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub

        return  "movie Id:"+ movieId +


                "movie URL:"+ imageUrl;



    }
}
