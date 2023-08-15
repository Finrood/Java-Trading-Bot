package com.exemple.trading_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TradingBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingBotApplication.class, args);
	}

}
