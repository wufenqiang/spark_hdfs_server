package com.weather.bigdata.it.cluster.controller;

import com.weather.bigdata.it.cluster.verification;
import com.weather.bigdata.it.spark.sparksubmit.sparksubmit;
import com.weather.bigdata.it.utils.hdfsUtil.HDFSReadWriteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@EnableAutoConfiguration
public class job {
    private static Logger logger = LoggerFactory.getLogger(job.class);
    private static String job_MsgKey="job_Msg";
    @RequestMapping(value = "/submit/job", method = RequestMethod.POST)
    public static Object verify(HttpServletRequest req, String user, String password, String splitFile, String signalFile){
        Map<String, String> result= (Map<String, String>) verification.register(req,user,password);
        boolean isroot=verification.isRoot(result);
        boolean isit=verification.isIt(result);
        boolean ismt=verification.isMt(result);
        if(isit || ismt || isroot){
            result =submit(result,splitFile,signalFile);
            result.put("splitFile", splitFile);
            result.put("signalFile", signalFile);
        }else{
            result.put(job_MsgKey,"请使用测试账号运行测试信号");
        }
        logger.info("用户"+user+"运行测试信号,splitFile("+splitFile+"),signalFile("+signalFile+")");
        return returnUtil.showReturn(logger,result);
    }

    private static Map<String, String> submit(Map<String, String> result, String splitFile,String signalFile){


        String[] signalMsgs= HDFSReadWriteUtil.readTXT(signalFile);
        for(String signalMsg:signalMsgs){
            boolean flag=sparksubmit.jobsubmit_byMsg_sparksubmitOpen(signalMsg,splitFile,new String[]{signalMsg,splitFile});
            result.put(signalMsg, String.valueOf(flag));
        }
        return result;
    }
}
