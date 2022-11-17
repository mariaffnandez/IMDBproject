package co.empathy.IMDBproject.Model.Facets;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

@Data
public class Facets {
    String type;
    String facet;
    ArrayList<Values> values;
    public Facets(String type, String facets, ArrayList<Values> list){
        this.type=type;
        this.facet=facets;
        this.values=list;
    }
}
