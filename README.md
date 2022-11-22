# IMDBproject

## Empathy Academy: Learning project


Creating an IMDB-like search engine

Using Elasticsearch

### Endpoints:
#### /search 
Parameters:
- **genres** (String) - Title genres. Multiple genres should be sent separeted by commas (e.g genres=Action,Sci-Fi)  
- **type** (String) - Title type. Values send in the same way as genres parameter. 
- **maxYear** (Integer) - Maximun start year. 
- **minYear** (Integer) - Miniumn start year 
- **maxMinutes** (Integer) - Maximun runtime minutes
- **minMinutes** (Integer) - Minimun runtime minutes
- **maxScore** (Double) - Maximun average rating
- **minScore** (Double) - Minimun average rating
- **maxNHits** * (Integer)  - Upper bound of the number of hits returned
- **sortRating** (String) - (asc/desc) The ordering of the sort. 
- **sortNumVotes** (String) - (asc/desc) The ordering of the sort. 
- **sortYear** (String) - (asc/desc) The ordering of the sort. 

<sub>* required </sub>

#### /index

