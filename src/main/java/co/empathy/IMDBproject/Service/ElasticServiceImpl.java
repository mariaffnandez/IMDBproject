package co.empathy.IMDBproject.Service;

import co.empathy.IMDBproject.Help.IMDbData;
import co.empathy.IMDBproject.Model.Movie;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;


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
        elasticEngine.indexMultipleDocs(indexName,imdb.readData(multipartFile));


    }







}

