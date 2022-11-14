package co.empathy.IMDBproject.Help;

import co.empathy.IMDBproject.Model.Filters;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static java.util.Objects.nonNull;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class QueryProvider {

    //queryBuilder
    public BoolQueryBuilder getQuery(Filters filters){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        QueryBuilder queryBuilder;
        if (filters.getGender()!=null) {
            //genres list?

            boolQueryBuilder.filter(termQuery("genres", filters.getGender()));
        }
        if(filters.getType()!=null){
            boolQueryBuilder.filter(termQuery("titleType", filters.getType()));
        }
        if(nonNull(filters.getMaxMinutes()) && nonNull(filters.getMinMinutes())){
            boolQueryBuilder.filter(rangeQuery("runtimeMinutes").gte(filters.getMinMinutes()).lte(filters.getMaxMinutes()));
        }
        if(nonNull(filters.getMinYear()) && nonNull(filters.getMaxYear())){
            //use start or end Year?
            boolQueryBuilder.filter(rangeQuery("startYear").gte(filters.getMinYear()).lte(filters.getMaxYear()));
        }

        if(nonNull(filters.getMaxScore()) && nonNull(filters.getMinScore())){
            //num of votes?
            boolQueryBuilder.filter(rangeQuery("averageRating").gte(filters.getMinScore()).lte(filters.getMaxScore()));
        }

        return boolQueryBuilder;
        /*
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        QueryBuilder qb1 = QueryBuilders.matchQuery("personId", "kimchy");
        boolQueryBuilder.must(qb1);
        sourceBuilder.query(boolQueryBuilder);
        //String searchJson = sourceBuilder.toString(); // if you want to print search json for debugging
        SearchRequest searchRequest = new SearchRequest(indexName);// your index name
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
*/

    }
}
