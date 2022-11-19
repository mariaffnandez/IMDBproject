package co.empathy.IMDBproject.Help;

import co.empathy.IMDBproject.Model.Movie.*;
import org.springframework.core.io.ClassPathResource;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
            movie.setGenres(fields[8].split(","));
            initializeListMovie(movie);

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
    //receive a line from the file and returns an aka object
    public Akas readAkas(String line){
        Akas aka= new Akas();
        if (line != null) {

            String[] fields = line.split("\t");

            aka.setTitle(fields[2]);
            aka.setRegion(fields[3]);
            aka.setLanguage(fields[4]);
            aka.setIsOriginalTitle(Boolean.parseBoolean(fields[5]));
        }
        return aka;
    }
    public void setAkas(Akas aka, Movie movie) {

        if (aka != null) {
            //add the aka to the akas list
            movie.getAkas().add(aka);
        }
    }
    public Starring readStarring(String line){
        Starring starring= new Starring();
        if (line != null) {
            String[] fields = line.split("\t");
            Name name= new Name();
            name.setNconst(fields[2]);
            starring.setName(name);
            starring.setCharacters(fields[5]);
        }
        return starring;
    }
    public void setStarring(Starring starring, Movie movie) {

        if (starring != null) {

            movie.getStarring().add(starring);
        }
    }
    public void setDirector(String line,Movie movie){

        if (line != null) {
            String[] fields = line.split("\t");
            //read the director field
            String [] directors= fields[1].split(",");
            //add each director to the list
            for (String directorString:directors){
                Director director= new Director();
                director.setNconst(directorString);
                movie.getDirectors().add(director);
            }

        }

    }

    /**
     * Adds the nonAdult movies to the list
     * @param list, a movie´s list
     * @param movie, the movie that will be added to the list
     */

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

    /**
     *
     * @param line1, a readline with an id
     * @param line2, another readline with an id
     * @return true if both lines have the same id
     */

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

    /**
     *
     * @param line1, the line with the first id
     * @param line2, the line with the second id
     * @return true if the id1 is smaller than the id2
     */
    public boolean smallerID(String line1, String line2){
        boolean result= false;
        if(line1!=null && line2!=null){
            String id1 = line1.split("\t")[0];
            String id2 = line2.split("\t")[0];
            int id1Num=Integer.parseInt(id1.split("tt")[1]);
            int id2Num=Integer.parseInt(id2.split("tt")[1]);
            if (id1Num<id2Num)
                result = true;
        }
        return result;

    }
    public void initializeListMovie(Movie movie){
        movie.setAkas(new ArrayList());
        movie.setStarring(new ArrayList());
        movie.setDirectors(new ArrayList<>());
    }


    public InputStream jsonMapping() throws IOException {
        InputStream mappingInputStream = new ClassPathResource("static/mapping.json").getInputStream();
        return mappingInputStream;
    }
}
