package com.weather.bigdata.it.cluster;

import com.weather.bigdata.it.cluster.db.localDB;
import com.weather.bigdata.it.cluster.util.CasicProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class verification {
    private static Logger logger = LoggerFactory.getLogger(verification.class);
    private static Properties prop = CasicProperties.CONFPROPERTIES;

    private static String registerKey="register";

    private static String passKey="pass";

    private static String roleKey="role";
    private static String isRootKey="isRoot";
    private static String isAdmin0Key="isAdmin0";
    private static String isAdmin1Key="isAdmin1";
    private static String isMtKey="isMt";
    private static String isItKey="isIt";

    private static String optionKey="option";

    private static String verification_MsgKey="verification_Msg";
    public static Object register(HttpServletRequest req,String user, String password){
        Map<String, String> result = new HashMap<String, String>();
        boolean flag=false;
        String remoteHost=req.getRemoteHost();
        String remoteAddr=req.getRemoteAddr();
        int remotePort=req.getRemotePort();
        String remoteUser=req.getRemoteUser();

        Properties AccountRegistration= localDB.getAccountRegistration();
        if(AccountRegistration.containsKey(user)){
            result.put(verification.registerKey,String.valueOf(true));

            String password0=verification.password(AccountRegistration,user);
            if(password0.equals(password)){

                result.put(verification.passKey,String.valueOf(true));

                String role=verification.role(AccountRegistration,user);
                result.put(verification.roleKey,role);
                logger.info(user+"注册通过,身份为"+role);

                boolean isRoot=verification.isRoot(AccountRegistration,user);
                boolean isAdmin0=verification.isAdmin0(AccountRegistration,user);
                boolean isAdmin1=verification.isAdmin1(AccountRegistration,user);
                boolean isMt=verification.isMt(AccountRegistration,user);
                boolean isIt=verification.isIt(AccountRegistration,user);

                if(isRoot){
                    result.put(verification.isRootKey,String.valueOf(true));
                }else{
                    result.put(verification.isRootKey,String.valueOf(false));
                }

                if(isAdmin0){
                    result.put(verification.isAdmin0Key,String.valueOf(true));
                }else{
                    result.put(verification.isAdmin0Key,String.valueOf(false));
                }

                if(isAdmin1){
                    result.put(verification.isAdmin1Key,String.valueOf(true));
                }else{
                    result.put(verification.isAdmin1Key,String.valueOf(false));
                }

                if(isMt){
                    result.put(verification.isMtKey,String.valueOf(true));
                }else{
                    result.put(verification.isMtKey,String.valueOf(false));
                }

                if(isIt){
                    result.put(verification.isItKey,String.valueOf(true));
                }else{
                    result.put(verification.isItKey,String.valueOf(false));
                }


            }else{
                result.put(verification.passKey,String.valueOf(false));
                result.put("status", "refuse");
                result.put(verification.verification_MsgKey, "拒绝。密码错误");
            }
        }else{
            result.put(verification.registerKey,String.valueOf(false));
            result.put("status", "refuse");
            result.put(verification.verification_MsgKey, "拒绝。该用户未注册");
        }
        result.put(verification.registerKey,String.valueOf(flag));
        logger.info(user+";remoteUser("+remoteUser+"),remoteHost("+remoteHost+":"+remotePort+"),remoteAddr("+remoteAddr+")");
        return result;
    }


    public static boolean isRoot(Object result){
        return verification.isRoot((Map<String, String>) result);
    }
    public static boolean isRoot(Map<String, String> result){
        boolean ispass=verification.isPass(result);
        if(ispass){
            return verification.isFlag(result,isRootKey);
        }else {
            return false;
        }
    }

    public static boolean isAdmin0(Object result){
        return verification.isAdmin0((Map<String, String>) result);
    }
    public static boolean isAdmin0(Map<String, String> result){
        boolean ispass=verification.isPass(result);
        if(ispass){
            return verification.isFlag(result,isAdmin0Key);
        }else {
            return false;
        }
    }

    public static boolean isAdmin1(Object result){
        return verification.isAdmin1((Map<String, String>) result);
    }
    public static boolean isAdmin1(Map<String, String> result){
        boolean ispass=verification.isPass(result);
        if(ispass){
            return verification.isFlag(result,isAdmin1Key);
        }else {
            return false;
        }
    }

    public static boolean isMt(Object result){
        return verification.isMt((Map<String, String>) result);
    }
    public static boolean isMt(Map<String, String> result){
        boolean ispass=verification.isPass(result);
        if(ispass){
            return verification.isFlag(result,isMtKey);
        }else {
            return false;
        }
    }

    public static boolean isIt(Object result){
        return verification.isIt((Map<String, String>) result);
    }
    public static boolean isIt(Map<String, String> result){
        boolean ispass=verification.isPass(result);
        if(ispass){
            return verification.isFlag(result,isItKey);
        }else {
            return false;
        }
    }

    public static boolean isPass(Object result){
        return verification.isPass((Map<String, String>) result);
    }
    public static boolean isPass(Map<String, String> result){
        return verification.isFlag(result,verification.passKey);
    }

    private static boolean isFlag(Map<String, String> result,String flagKey){
        String flagStr=result.get(flagKey);
        return Boolean.valueOf(flagStr);
    }

    private static boolean isRoot(Properties accountRegistration,String user){
        return verification.isRole(accountRegistration,user,"root");
    }
    private static boolean isAdmin0(Properties accountRegistration,String user){
        return verification.isRole(accountRegistration,user,"admin0");
    }
    private static boolean isAdmin1(Properties accountRegistration,String user){
        return verification.isRole(accountRegistration,user,"admin1");
    }
    private static boolean isMt(Properties accountRegistration,String user){
        return verification.isRole(accountRegistration,user,"mt");
    }
    private static boolean isIt(Properties accountRegistration,String user){
        return verification.isRole(accountRegistration,user,"it");
    }
    private static boolean isRole(Properties accountRegistration,String user,String role0){
        String role=verification.role(accountRegistration,user);
        if(role.equals(role0)){
            return true;
        }else{
            return false;
        }
    }

    private static String password(Properties accountRegistration,String user){
        return accountRegistration.getProperty(user).split(",")[0];
    }
    private static String role(Properties accountRegistration,String user){
        return accountRegistration.getProperty(user).split(",")[1];
    }
}
