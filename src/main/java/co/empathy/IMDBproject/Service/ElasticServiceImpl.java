package co.empathy.IMDBproject.Service;

import co.empathy.IMDBproject.Help.IMDBReader;
import co.empathy.IMDBproject.Help.IMDbData;
import co.empathy.IMDBproject.Model.Filters;
import co.empathy.IMDBproject.Model.Movie;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ElasticServiceImpl implements ElasticService {
    private final ElasticEngine elasticEngine;
    private final String imdbIndex = "test";

    //number of movies that will be index together
    int blockMovies = 50000;

    private IMDBReader imdb;
    public IMDbData data;

    public ElasticServiceImpl(ElasticEngine searchEngine) {

        this.elasticEngine = searchEngine;
        this.data= new IMDbData();
    }

    @Override
    public String listIndices() throws IOException {
        return elasticEngine.listIndices();
    }

    @Override
    public Boolean createIndex(String name, String source) {
        return elasticEngine.createIndex(name, source);
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
    public Boolean indexIMDBData(MultipartFile basicsFile, MultipartFile ratingFile, MultipartFile akasFile) throws IOException {
        imdb = new IMDBReader(basicsFile, ratingFile, akasFile);

        //create imdb index and
        createIndex(imdbIndex,data.jsonMapping());


        List<Movie> movieList = new ArrayList<>();
        Movie movie;
        int countMovies = 0;

        while(imdb.moreLines) {
            movie=imdb.readMovie();

            if (movie!=null) {
                data.moviesList(movieList, movie);
                countMovies++;
            }
            if (countMovies==blockMovies ){
                //index a "small" movie's list
                elasticEngine.indexMultipleDocs(imdbIndex,movieList);

                //prepare the next list
                countMovies=0;
                movieList.clear();
            }
        }
        //index the last list
        elasticEngine.indexMultipleDocs(imdbIndex,movieList);


        System.out.println("Indexed");
        return true;
}
    public List<Movie> getQuery(Filters filter) throws IOException {
        return elasticEngine.getQuery(filter);

    }

    public List<Movie> getSearchQuery(String searchText) throws IOException {
        return elasticEngine.getSearchQuery(searchText);

    }


}

