package co.empathy.IMDBproject.Help;

import co.empathy.IMDBproject.Model.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IMDbData {

    public List<Movie> readData(MultipartFile multipartFile) throws IOException {
        List<Movie> movieList= new ArrayList<>();
        try {
            InputStream inputStream = multipartFile.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            //first line read (no movie's info)
            String types=reader.readLine();

            String readLine;
            //adding all the nonAdults movies to a list
            while ((readLine = reader.readLine()) != null) {
                moviesList(movieList,readMovieTitleBasicsLines(readLine));

            }
            System.out.println("Number of movies: "+movieList.size());
            return movieList;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public Movie readMovieTitleBasicsLines(String line){

        String[] fields = line.split("\t");
        Movie movie = new Movie();
        movie.setTconst(fields[0]);
        movie.setTitleType(fields[1]);
        movie.setPrimaryTitle(  fields[2]);
        movie.setOriginalTitle(fields[3]);
        movie.setIsAdult(Boolean.parseBoolean(fields[4]));
        movie.setStartYear(toInteger(fields[5]));
        movie.setEndYear(toInteger(fields[6]));
        movie.setRuntimeMinutes(toInteger(fields[7]));
        movie.setGenres(fields[8]);

        return movie;
    }
    //excluding isAdult movies
    public void moviesList(List<Movie> list, Movie movie){
        if(!movie.getIsAdult())
            list.add(movie);

    }
    //Handle \N value on int fields
    public int toInteger(String field){
        int integer;
        try {
            integer=Integer.parseInt(field);

        } catch (NumberFormatException e) {
            integer=-1;
        }
        return integer;
    }
    public Collection<List<Movie>> partitionList(List<Movie> list){

    int partitionSize = 200;

    Collection<List<Movie>> partitionedList = IntStream.range(0, list.size())
            .boxed()
            .collect(Collectors.groupingBy(partition -> (partition / partitionSize), Collectors.mapping(elementIndex -> list.get(elementIndex), Collectors.toList())))
            .values();
    System.out.println("Partited list "+partitionedList.size());
    return partitionedList;

}}
