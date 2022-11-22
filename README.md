# IMDBproject

## Empathy Academy: Learning project


Creating an IMDB-like search engine

Using Elasticsearch

### Endpoints:
#### /search 
Parameters:
- **genres** (String) - Value of genres to filter by. Multiple genres should be sent separeted by commas (e.g genres=Action,Sci-Fi)  
- **type** (String) - Value of title type to filter by. Values send in the same way as genres parameter 
- **maxYear** (Integer) - Max value of start year to filter by 
- **minYear** (Integer) - Min value of start year to filter by
- **maxMinutes** (Integer) - Max value of runtime minutes to filter by
- **minMinutes** (Integer) - Min value of runtime minutes to filter by
- **maxScore** (Double) - Max value of average rating to filter by
- **minScore** (Double) - Min value of average rating to filter by
- **maxNHits** * (Integer)  - Upper bound of the number of hits returned
- **sortRating** (String) - (asc/desc) The ordering of the sort. 
- **sortNumVotes** (String) - (asc/desc) The ordering of the sort. 
- **sortYear** (String) - (asc/desc) The ordering of the sort. 

<sub>* required </sub>

#### /index

