package co.empathy.IMDBproject.Service;

import co.empathy.IMDBproject.Help.IMDbReader;
import co.empathy.IMDBproject.Help.IMDbData;
import co.empathy.IMDBproject.Model.Query.Filters;
import co.empathy.IMDBproject.Model.Movie.Movie;

import co.empathy.IMDBproject.Model.Response;
import co.empathy.IMDBproject.Model.Query.Sort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ElasticServiceImpl implements ElasticService {
    private final ElasticEngine elasticEngine;
    private final String imdbIndex = "imdb";

    //number of movies that will be indexed together
    int blockMovies = 50000;

    private IMDbReader imdb;
    public IMDbData data;

    public ElasticServiceImpl(ElasticEngine searchEngine) {

        this.elasticEngine = searchEngine;
        this.data= new IMDbData();
    }

    @Override
    public Boolean createIndex(String name, InputStream mapping) {
        return elasticEngine.createIndex(name, mapping);
    }

    @Override
    public Boolean deleteIndex(String name) {
        return elasticEngine.deleteIndex(name);
    }

    @Override
    public Boolean indexDoc(String indexName, Movie movie) {
        return elasticEngine.indexDoc(indexName, movie);
    }


    @Override
    public Boolean indexIMDBData(MultipartFile basicsFile, MultipartFile ratingFile, MultipartFile akasFile,MultipartFile crewFile,MultipartFile principalsFile) throws IOException {

        try {
            //checks that there are not empty files
            if (basicsFile.isEmpty()||ratingFile.isEmpty()||akasFile.isEmpty()||crewFile.isEmpty()||principalsFile.isEmpty())
                throw new IOException();

            imdb = new IMDbReader(basicsFile, ratingFile, akasFile, crewFile, principalsFile);
            //starts reading the first lines
            imdb.initializeLines();
            //create imdb index and set the mapping properties
            createIndex(imdbIndex, jsonMapping());


            List<Movie> movieList = new ArrayList<>();
            Movie movie;
            int countMovies = 0;

            while (imdb.moreLines) {
                movie = imdb.readMovie();

                if (movie != null) {
                    //add the movie to the list
                    data.moviesList(movieList, movie);
                    countMovies++;
                }
                if (countMovies == blockMovies) {
                    //index a "small" movie's list
                    elasticEngine.indexMultipleDocs(imdbIndex, movieList);

                    //prepare the next list
                    countMovies = 0;
                    movieList.clear();
                }
            }
            //index the last list if is not empty

            elasticEngine.indexMultipleDocs(imdbIndex, movieList);

        }
        catch (IOException e){
            throw e;
        }


        return true;
}

    public Response getQuery(Filters filter, Sort sort) throws IOException {
        return elasticEngine.getQuery(filter,sort);

    }

    public Response getSearchQuery(String searchText) throws IOException {
        return elasticEngine.getSearchQuery(searchText);

    }

    /**
     *
     * @return an input stream with the .json from the resources folder
     * @throws IOException
     */
    public InputStream jsonMapping() throws IOException {
        InputStream mappingInputStream = new ClassPathResource("static/mapping.json").getInputStream();
        return mappingInputStream;
    }



}

