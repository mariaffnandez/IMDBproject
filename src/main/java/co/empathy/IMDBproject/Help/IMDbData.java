package co.empathy.IMDBproject.Help;

import co.empathy.IMDBproject.Model.Movie;



import java.util.List;


public class IMDbData {



    public Movie readMovieTitleBasicsLines(String line){
        Movie movie = new Movie();
        if (line!=null) {

            String[] fields = line.split("\t");
            movie.setTconst(fields[0]);
            movie.setTitleType(fields[1]);
            movie.setPrimaryTitle(fields[2]);
            movie.setOriginalTitle(fields[3]);
            movie.setIsAdult(Boolean.parseBoolean(fields[4]));
            movie.setStartYear(toInteger(fields[5]));
            movie.setEndYear(toInteger(fields[6]));
            movie.setRuntimeMinutes(toInteger(fields[7]));
            movie.setGenres(fields[8]);
        }
        return movie;
    }
    //only adds to the list the nonAdults movies
    public void moviesList(List<Movie> list, Movie movie){
        if(movie!=null){
            if(movie.getIsAdult()!=null && !movie.getIsAdult())
                list.add(movie);
        }

    }
    //Handle \N value on int fields
    public int toInteger(String field){
        int integer;
        try {
            integer=Integer.parseInt(field);

        } catch (NumberFormatException e) {
            //no valid value
            integer=-1;
        }
        return integer;
    }
    }
