package co.empathy.IMDBproject.Model.Facets;

import lombok.Data;

@Data
public class Values {
    String value;
    Long count;
    public Values(String value, long count){
        this.value=value;
        this.count=count;
    }


}
