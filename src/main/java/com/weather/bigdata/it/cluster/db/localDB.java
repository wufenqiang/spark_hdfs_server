package com.weather.bigdata.it.cluster.db;

import com.weather.bigdata.it.cluster.util.CasicProperties;
import com.weather.bigdata.it.cluster.util.PropertiesUtils;
import com.weather.bigdata.it.spark.sparksubmit.sparksubmit_PropertiesUtil;
import com.weather.bigdata.it.utils.hdfsUtil.HDFSConfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spark_project.jetty.util.ConcurrentHashSet;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class localDB {
    private static Properties prop = CasicProperties.CONFPROPERTIES;
    private static Logger logger = LoggerFactory.getLogger(localDB.class);

    private static ConcurrentHashMap<String,Long> user_visit = new ConcurrentHashMap<String,Long>();

    public static Long getUserVisit(String key) {
        return user_visit.get(key);
    }

    public static void addUserVisit(String key) {
        user_visit.put(key,System.currentTimeMillis());
    }


    private static String splitFile="";
    public static void initSplitFile(){
        localDB.splitFile= CasicProperties.CONFPROPERTIES.getProperty("sparksubmit_splitFile");
    }

    public static String getSplitFile(){
        return localDB.splitFile;
    }

    private static int serverPort = -1;
    public static void setServerPort(){
    }

    private static ConcurrentHashSet<String> regionSet_online=new ConcurrentHashSet<String>();
    private static ConcurrentHashSet<String> regionSet_online_sec=new ConcurrentHashSet<String>();
    private static ConcurrentHashSet<String> regionSet_patch=new ConcurrentHashSet<String>();
    private static ConcurrentHashSet<String> regionSet_mt=new ConcurrentHashSet<String>();
    private static ConcurrentHashSet<String> regionSet_it=new ConcurrentHashSet<String>();
    public static void initprotectedRegionSet(){
        String[] regions_online=CasicProperties.CONFPROPERTIES.getProperty("regionKeys_online").split(",");
        String[] regions_online_sec=CasicProperties.CONFPROPERTIES.getProperty("regionKeys_online_sec").split(",");
        String[] regions_patch=CasicProperties.CONFPROPERTIES.getProperty("regionKeys_patch").split(",");
        String[] regions_mt=CasicProperties.CONFPROPERTIES.getProperty("regionKeys_mt").split(",");
        String[] regions_it=CasicProperties.CONFPROPERTIES.getProperty("regionKeys_it").split(",");

        for(String region :regions_online){
            localDB.regionSet_online.add(region);
        }
        for(String region :regions_online_sec){
            localDB.regionSet_online_sec.add(region);
        }
        for(String region :regions_patch){
            localDB.regionSet_patch.add(region);
        }
        for(String region :regions_mt){
            localDB.regionSet_mt.add(region);
        }
        for(String region :regions_it){
            localDB.regionSet_it.add(region);
        }
    }

    public static boolean isOnlineRegion(String RegionKey){
        return localDB.regionSet_online.contains(RegionKey);
    }
    public static boolean isOnlineSecRegion(String RegionKey){
        return localDB.regionSet_online_sec.contains(RegionKey);
    }
    public static boolean isPatchRegion(String RegionKey){
        return localDB.regionSet_patch.contains(RegionKey);
    }
    public static boolean isMtRegion(String RegionKey){
        return localDB.regionSet_mt.contains(RegionKey);
    }
    public static boolean isItRegion(String RegionKey){
        return localDB.regionSet_it.contains(RegionKey);
    }


    public static  Properties getAccountRegistration(){
        String accountFile=CasicProperties.CONFPROPERTIES.getProperty("accountRegistration");
        logger.info("accountFile="+accountFile);
        return PropertiesUtils.readProperties1(accountFile);
    }



    public static String getupload_dir(){
        return prop.getProperty("upload_dir");
    }
    public static String getlibPath(String regionKey,String dataType){
        String upload_dir0 = localDB.getupload_dir();
        String upload_dir1= HDFSConfUtil.formatFilename(upload_dir0);
        String upload_dir=upload_dir1;
        if(dataType.equals("mt_commons")) {
            upload_dir = sparksubmit_PropertiesUtil.getmt_commonsLibPath_regionKey(regionKey,upload_dir1);
        }else if(dataType.equals("platform")){
            upload_dir = sparksubmit_PropertiesUtil.getplatformlibPath_regionKey(regionKey,upload_dir1);
        }else{
            upload_dir=sparksubmit_PropertiesUtil.getsignallibPath_dataType_regionKey(dataType,regionKey,upload_dir1);
        }

        return upload_dir;
    }
    private static String ParameterPathRoot=prop.getProperty("parameterpathRoot");
    public static String getParameterPath(String RelativePath){
        return localDB.ParameterPathRoot+"/"+RelativePath;
    }


    public static String getspark_hdfs_server_Path(String exampleKey){
        return prop.getProperty("spark_hdfs_server_rootPath")+"/"+exampleKey+"/";
    }
}
