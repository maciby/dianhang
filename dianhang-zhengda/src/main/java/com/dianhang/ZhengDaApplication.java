package com.dianhang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = { "com.dianhang" }, exclude = { DataSourceAutoConfiguration.class })
@EnableTransactionManagement
@MapperScan("com.dianhang.*.mapper")
public class ZhengDaApplication extends SpringBootServletInitializer
		implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(this.getClass());
	}

	public static void main(String[] args) {
		SpringApplication.run(ZhengDaApplication.class, args);
	}

	public void onApplicationEvent(ContextRefreshedEvent event) {
		// log.info("onApplicationEvent");
	}

}
