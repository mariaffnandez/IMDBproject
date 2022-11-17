package co.empathy.IMDBproject.Help;

import co.empathy.IMDBproject.Model.*;
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
        ArrayList<Akas> list;
        if (aka != null) {
            //if it is the first aka, create a new akas array
            if(movie.getAkas()==null) {
                list= new ArrayList();
                list.add(aka);

            }
            else{
                list=movie.getAkas();
                list.add(aka);

            }
            movie.setAkas(list);
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
        ArrayList<Starring> list;
        if (starring != null) {
            //if it is the first starring, create a new starring array
            if(movie.getStarring()==null) {
                list= new ArrayList();
                list.add(starring);
            }
            else{
                list=movie.getStarring();
                list.add(starring);

            }
            movie.setStarring(list);
        }
    }
    public void setDirector(String line,Movie movie){
        ArrayList<Director> list= new ArrayList<>();
        if (line != null) {
            String[] fields = line.split("\t");
            //read the director field
            String [] directors= fields[1].split(",");
            //add each director to the list
            for (String directorString:directors){
                Director director= new Director();
                director.setNconst(directorString);
                list.add(director);
            }
            movie.setDirectors(list);
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

    //checks if 2 lines have the same id
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
