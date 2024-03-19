package com.example.chatroom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.example.chatroom.mapper")
@SpringBootApplication
public class ChatroomApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatroomApplication.class, args);
	}

}
