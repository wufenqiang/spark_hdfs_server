//package com.weather.bigdata.it.cluster.controller;
//
//import com.weather.bigdata.it.cluster.verification;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Map;
//
//@RestController
//@EnableAutoConfiguration
//public class exitApplication {
//
//	private static Logger logger = LoggerFactory.getLogger(exitApplication.class);
//    private static String exit_MsgKey="exit_Msg";
//
//    @RequestMapping(value = "/operation/exit", method = RequestMethod.POST)
//    public static Object verify(HttpServletRequest req, String user, String password){
//        Map<String, String> result= (Map<String, String>) verification.register(req,user,password);
//        boolean isroot=verification.isRoot(result);
//        if(isroot){
//            String msg="user("+user+")申请关闭本次接口服务";
//            logger.info(msg);
//            result.put("status", "success");
//            result.put(exit_MsgKey, msg);
//            result=exitApplication.exit(result);
//        }else{
//            result.put("status", "refuse");
//            result.put(exit_MsgKey, "权限不够,请联系维护人员");
//        }
//        logger.info("用户"+user+"请求关闭接口");
//        return returnUtil.showReturn(logger,result);
//    }
//
//
//}
