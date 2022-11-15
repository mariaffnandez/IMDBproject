package co.empathy.IMDBproject.Service;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import co.empathy.IMDBproject.Model.Filters;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class QueryProvider {

    //queryBuilder
    public Query getFilterQuery(Filters filters){

        List<Query> queries=new ArrayList<>();
        Query query;
        if (filters.getGenre()!=null) {
            System.out.println("getGenre");
            //genres list?
            queries.add(termQuery(filters.getGenre(),"genres"));

        }
        if(filters.getType()!=null){
            System.out.println("getType");
            queries.add(termQuery(filters.getType(),"titleType"));


        }
        if(nonNull(filters.getMaxMinutes()) && nonNull(filters.getMinMinutes())){
            System.out.println("getMin");
            queries.add(rangeQuery(filters.getMinMinutes(),filters.getMaxMinutes(),"runtimeMinutes"));
        }
        if(nonNull(filters.getMinYear()) && nonNull(filters.getMaxYear())){
            //use start or end Year?
            System.out.println("getYear");
            queries.add(rangeQuery(filters.getMinYear(),filters.getMaxYear(),"startYear"));
        }

        if(nonNull(filters.getMaxScore()) && nonNull(filters.getMinScore())){
            //num of votes?
            System.out.println("getScore");
            queries.add(rangeQuery(filters.getMinScore(),filters.getMaxScore(),"averageRating"));
        }

        //final query with all the filters
        query=BoolQuery.of(b -> b.filter(queries))._toQuery();
       System.out.println(query.toString());
        return query;


    }

    public Query termQuery(String value, String field){
        Query query = TermQuery.of(m -> m
                .field(field)
                .value(value)
        )._toQuery();
        return query;
    }

    public Query rangeQuery(int minValue, int maxValue, String field){
        return RangeQuery.of(r -> r
                .field(field)
                .gte(JsonData.of(minValue))
                .lte(JsonData.of(maxValue))
        )._toQuery();

    }
    public Query rangeQuery(Double minValue, Double maxValue, String field){
        return RangeQuery.of(r -> r
                .field(field)
                .gte(JsonData.of(minValue))
                .lte(JsonData.of(maxValue))
        )._toQuery();

    }
    public Query multiMatchquery(String value, List <String> fields)  {
        return MultiMatchQuery.of(r -> r
                .fields(fields)
                .query(value)
        )._toQuery();

    }

}
