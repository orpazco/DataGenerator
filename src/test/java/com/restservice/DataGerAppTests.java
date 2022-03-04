package com.restservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest
class DataGerAppTests {

	@Autowired
	private DataGenApp controller;
	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort
	private int port;

//	@Test
//	void contextLoads() {
//		assertThat(controller).isNotNull();
//	}
//
//	@Test
//	void greetingShouldReturnDefaultMessage() throws Exception {
//		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
//				String.class)).contains("Hello, World");
//	}

}
