package com.weather.bigdata.it.cluster.init;

import com.weather.bigdata.it.cluster.db.localDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


/**
 * 初始化方法
 * @author liweifeng
 *
 */
@Component
public class MyApplicationRunner implements ApplicationRunner {

    private static Logger logger = LoggerFactory.getLogger(MyApplicationRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //localDB.setSplitFile();
        localDB.initprotectedRegionSet();
        //logger.info("我是做初始化信息的。");
    }

}
