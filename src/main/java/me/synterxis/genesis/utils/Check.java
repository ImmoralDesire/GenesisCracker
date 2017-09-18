package me.synterxis.genesis.utils;

import me.synterxis.genesis.Main;

import java.util.ArrayList;
import java.util.List;

public class Check {

    public static boolean notNull(Object... objects) {
        List<Object> nulls = new ArrayList<>();

        for(Object o : objects) {
            if(o == null) {
                nulls.add(o);
            }
        }

        if(!nulls.isEmpty()) {
            Main.logger.error("One of the following objects are null! " + nulls.toString());
            return false;
        }

        return true;
    }
}
