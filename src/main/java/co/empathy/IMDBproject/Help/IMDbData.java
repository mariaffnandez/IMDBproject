package co.empathy.IMDBproject.Help;

import co.empathy.IMDBproject.Model.Movie;
import org.springframework.core.io.ClassPathResource;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;


public class IMDbData {

    public Movie setBasicsLines(String line) {
        Movie movie = new Movie();
        if (line != null) {

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

    public void setRatings(String line, Movie movie) {

        if (line != null) {

            String[] fields = line.split("\t");
            movie.setAverageRating(Double.parseDouble(fields[1]));
            movie.setNumVotes(Integer.parseInt(fields[2]));

        }


    }

    //only adds to the list the nonAdults movies
    public void moviesList(List<Movie> list, Movie movie) {
        if (movie != null) {
            if (movie.getIsAdult() != null && !movie.getIsAdult())
                list.add(movie);
        }

    }

    //Handle \N value on int fields
    public int toInteger(String field) {
        int integer;
        try {
            integer = Integer.parseInt(field);

        } catch (NumberFormatException e) {
            //no valid value
            integer = -1;
        }
        return integer;
    }
    public boolean sameId(String line1, String line2){
        boolean result= false;
        if(line1!=null && line2!=null){
            String id1 = line1.split("\t")[0];
            String id2 = line2.split("\t")[0];
            if (id1.equalsIgnoreCase(id2))
                result = true;
        }
        return result;
    }


    public String jsonMapping() throws IOException {
        InputStream mappingInputStream = new ClassPathResource("static/mapping.json").getInputStream();
        return new String(mappingInputStream.readAllBytes(), UTF_8);
    }
}
