package com.braintri.chodznaobiad;

import java.util.ArrayList;
import java.util.List;

public class Place {
    public List<String> votes = new ArrayList<>();
    public String name;

    public Place() {
    }

    public Place(ArrayList<String> votes, String name) {
        this.votes = votes;
        this.name = name;
    }
}
