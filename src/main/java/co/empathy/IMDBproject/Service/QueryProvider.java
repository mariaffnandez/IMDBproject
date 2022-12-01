package co.empathy.IMDBproject.Service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import co.empathy.IMDBproject.Model.Query.Filters;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.nonNull;

public class QueryProvider {

    //queryBuilder

    /**
     *
     * @param filters you want to apply
     * @return a query to send in the request with all the filters
     */
    public Query getFilterQuery(Filters filters){

        List<Query> queries=new ArrayList<>();
        Query query;
        if (filters.getGenre()!=null) {

            queries.add(termsQuery(filters.getGenre(),"genres"));
            System.out.println(queries);
        }
        if(filters.getType()!=null){

            queries.add(termsQuery(filters.getType(),"titleType"));

        }
        if(nonNull(filters.getMaxMinutes()) && nonNull(filters.getMinMinutes())){

            queries.add(rangeQuery(filters.getMinMinutes(),filters.getMaxMinutes(),"runtimeMinutes"));
        }
        if(nonNull(filters.getMinYear()) && nonNull(filters.getMaxYear())){


            queries.add(rangeQuery(filters.getMinYear(),filters.getMaxYear(),"startYear"));
        }

        if(nonNull(filters.getMaxScore()) && nonNull(filters.getMinScore())){

            queries.add(rangeQuery(filters.getMinScore(),filters.getMaxScore(),"averageRating"));
        }

        //final query with all the filters
        query=BoolQuery.of(b -> b.filter(queries))._toQuery();
       System.out.println(query.toString());
        return query;


    }
    /**
     *
     * @param value, the exact term you wish to find
     * @param field you wish to search
     * @return the query that will be sent in the request
     */

    public Query termQuery(String value, String field){
        return  TermQuery.of(m -> m
                .field(field)
                .value(value)
        )._toQuery();

    }

    /**
     *
     * @param values, an array with the multiple values
     * @param field you wish to search
     * @return the query that will be sent in the request
     */

    public Query termsQuery(String[] values, String field){

        List<String> list= Arrays.asList(values);
        TermsQueryField terms = new TermsQueryField.Builder()
                .value(list.stream().map(FieldValue::of).toList())
                .build();
        return TermsQuery.of(m -> m
                .field(field)
                .terms(terms)
        )._toQuery();

    }

    /**
     *
     * @param minValue, the min value of the term (integer)
     * @param maxValue, the max value of the term (integer)
     * @param field you wish to search
     * @return the query that will be sent in the request
     */
    public Query rangeQuery(int minValue, int maxValue, String field){
        return RangeQuery.of(r -> r
                .field(field)
                .gte(JsonData.of(minValue))
                .lte(JsonData.of(maxValue))
        )._toQuery();

    }
    /**
     *
     * @param minValue, the min value of the term (double)
     * @param maxValue, the max value of the term (double)
     * @param field you wish to search
     * @return the query that will be sent in the request
     */
    public Query rangeQuery(Double minValue, Double maxValue, String field){
        return RangeQuery.of(r -> r
                .field(field)
                .gte(JsonData.of(minValue))
                .lte(JsonData.of(maxValue))
        )._toQuery();

    }

    /**
     *
     * @param value, the query string
     * @param fields, the fields to be queried
     * @return the query that will be sent in the request
     */
    public Query multiMatchquery(String value, List <String> fields)  {
        return MultiMatchQuery.of(r -> r
                .fields(fields)
                .query(value)
        )._toQuery();


    }



}
