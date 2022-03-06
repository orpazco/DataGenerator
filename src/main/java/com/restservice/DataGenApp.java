package com.restservice;

import com.configuration.Config;
import com.generator.Handler;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@ComponentScan({"com.configuration"})
@SpringBootApplication
@RestController
public class DataGenApp {

	// initialized by spring
	@Autowired
	private Config config;
	private Handler handler = new Handler();

	public static void main(String[] args) {
		SpringApplication.run(DataGenApp.class, args);
	}

	@PostMapping(value = "/createTable", consumes = "application/json", produces = "application/json")
	public String createTable(@RequestBody String receivedEvent) {
		try {
			return handler.handleEvent(receivedEvent, config);
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONObject().put("error", e.getMessage()).toString();
		}
	}
}
