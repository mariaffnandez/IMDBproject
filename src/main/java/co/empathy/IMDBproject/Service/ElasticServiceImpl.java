package co.empathy.IMDBproject.Service;

import co.empathy.IMDBproject.Help.IMDbData;
import co.empathy.IMDBproject.Model.Movie;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Collection;
import java.util.List;


public class ElasticServiceImpl implements ElasticService {
    private final ElasticEngine elasticEngine;
    private final String indexName="imdb";

    private IMDbData imdb;

    public ElasticServiceImpl(ElasticEngine searchEngine) {
        this.elasticEngine = searchEngine;
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
    public Boolean indexDoc(String indexName, Movie movie) {
        return elasticEngine.indexDoc(indexName, movie);
    }

    @Override
    public void indexIMDBData(MultipartFile multipartFile) throws IOException {

        System.out.println("Indexing "+multipartFile.getName());
        imdb= new IMDbData();
        //return all the data as a movie´s list
        List<Movie> bigList= imdb.readData(multipartFile);
        //divide the big list in smaller lists to index them
        Collection<List<Movie>> partiList= imdb.partitionList(bigList);
        for(List<Movie> list : partiList)
        {

            elasticEngine.indexMultipleDocs(indexName,list);
        }



    }







}

