package co.empathy.IMDBproject.Help;

import co.empathy.IMDBproject.Model.Movie;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;


public class IMDbData {
    private MultipartFile basicsFile;
    private MultipartFile ratingsFile;
    public IMDbData(MultipartFile basicsFile, MultipartFile ratingsFile){
        this.basicsFile=basicsFile;
        this.ratingsFile=ratingsFile;

    }


    public Movie readMovieTitleBasicsLines(String line) {
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

    public void readMovieRatingsLines(String line, List<Movie> list) {
        Movie movie = new Movie();
        if (line != null) {
            String[] fields = line.split("\t");
            movie.setTconst(fields[0]);
            movie.setAverageRating(Double.parseDouble(fields[1]));
            movie.setNumVotes(Integer.parseInt(fields[2]));
            list.add(movie);
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

    public BufferedReader reader(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            //read the first line (no useful info)
            String types=reader.readLine();
            return reader;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String jsonMapping() throws IOException {
        InputStream mappingInputStream = new ClassPathResource("mapping/mapping.json").getInputStream();
        return new String(mappingInputStream.readAllBytes(), UTF_8);
    }
}
