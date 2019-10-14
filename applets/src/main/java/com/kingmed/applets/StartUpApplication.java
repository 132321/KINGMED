package com.kingmed.applets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 主方法
 * @author BIN
 *
 */
@SpringBootApplication
public class StartUpApplication{
	//  extends SpringBootServletInitializer
	
	/*@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(this.getClass());
	}*/

	public static void main(String[] args) {
		SpringApplication.run(StartUpApplication.class, args);
	}
	
	/*@Bean
    public TomcatServletWebServerFactory servletContainer(){
        return new TomcatServletWebServerFactory(80) ;
    }*/
}
