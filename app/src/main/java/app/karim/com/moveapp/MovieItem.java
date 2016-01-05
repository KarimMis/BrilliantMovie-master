package app.karim.com.moveapp;


public class MovieItem {
    String original_title;
    String overview;
    String vote_average;
    String release_date;
    String image;
    String movie_id;
    String[] movie_keys;
    String[] movie_videos_names;
    String[] movie_authors;
    String[] movie_authors_content;


    public String[] getMovie_authors_content() {
        return movie_authors_content;
    }

    public void setMovie_authors_content(String[] movie_authors_content) {
        this.movie_authors_content = movie_authors_content;
    }

    public String[] getMovie_authors() {
        return movie_authors;
    }

    public void setMovie_authors(String[] movie_authors) {
        this.movie_authors = movie_authors;
    }

    public String[] getMovie_keys() {
        return movie_keys;
    }


    public String[] getMovie_videos_names() {
        return movie_videos_names;
    }

    public void setMovie_videos_names(String[] movie_videos_names) {
        this.movie_videos_names = movie_videos_names;
    }

    public void setMovie_keys(String[] movie_keys) {
        this.movie_keys = movie_keys;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MovieItem( )
    {


    }

}