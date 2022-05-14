package com.dailydrink.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.dailydrink.common.entity", "com.dailydrink.admin.user"})
public class DailyDrinkBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailyDrinkBackEndApplication.class, args);
	}

}
