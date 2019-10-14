package com.kingmed.applets.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@Component
public class LogAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
	
	@Pointcut("@annotation(com.kingmed.applets.tools.Log)")
	public void LogPointcut() {}
	
	@Before("LogPointcut()")
	public void doBefore(JoinPoint joinPoint) {
		
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
 
        String ip = request.getHeader("x-forwarded-for");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
         }  
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
         }  
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
             ip = request.getHeader("HTTP_CLIENT_IP");  
         }  
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
         }  
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
             ip = request.getRemoteAddr();  
         }
 
         try {
 			String filenameTemp =  "D:\\kingMed\\kingMedLog.txt";
 			File filename = new File(filenameTemp);
 			if (!filename.exists()) {
 				filename.createNewFile();
 			}

 			String word = "IP:" + ip + "  " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
 			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filenameTemp, true)));
 			out.write(word + "\r\n");
 			out.close();
 		} catch (Exception e) {
 			// TODO: handle exception
 			e.printStackTrace();
 		}
	}
	public static void main(String[] args) {
		
	}
}
