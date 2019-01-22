package com.main.manage;

import com.main.manage.filter.LoginFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


//https://github.com/qchery/funda

@SpringBootApplication
//@EnableConfigurationProperties({
//		SystemProperties.class
//})
public class ManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManageApplication.class, args);
	}

	@Bean
	public LoginFilter loginFilter() {
		return new LoginFilter();
	}

}
