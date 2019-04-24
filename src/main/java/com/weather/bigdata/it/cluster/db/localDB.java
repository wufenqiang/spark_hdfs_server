package com.weather.bigdata.it.cluster.db;

import com.weather.bigdata.it.cluster.util.CasicProperties;

import java.util.concurrent.ConcurrentHashMap;

public class localDB {

    private static ConcurrentHashMap<String,Long> user_visit = new ConcurrentHashMap<String,Long>();

    public static Long getUserVisit(String key) {
        return user_visit.get(key);
    }

    public static void addUserVisit(String key) {
        user_visit.put(key,System.currentTimeMillis());
    }


    private static String splitFile="";
    public static void setSplitFile(){
        localDB.splitFile= CasicProperties.CONFPROPERTIES.getProperty("sparksubmit_splitFile");
    }

    public static String getSplitFile(){
        return localDB.splitFile;
    }

    private static int serverPort = -1;
    public static void setServerPort(){
    }
}
