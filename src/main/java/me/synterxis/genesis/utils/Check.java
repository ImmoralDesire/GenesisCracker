package me.synterxis.genesis.utils;

import java.util.ArrayList;
import java.util.List;

public class Check {

    public static void notNull(Object... objects) {
        List<Object> nulls = new ArrayList<>();

        for(Object o : objects) {
            if(o == null) {
                nulls.add(o);
            }
        }

        if(!nulls.isEmpty())
            throw new NullPointerException("One of the following objects are null! " + nulls.toString());
    }
}
