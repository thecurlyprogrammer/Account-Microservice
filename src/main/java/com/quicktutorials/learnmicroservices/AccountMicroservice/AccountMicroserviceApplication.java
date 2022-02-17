package com.quicktutorials.learnmicroservices.AccountMicroservice;

import com.quicktutorials.learnmicroservices.AccountMicroservice.daos.AccountDao;
import com.quicktutorials.learnmicroservices.AccountMicroservice.daos.OperationDao;
import com.quicktutorials.learnmicroservices.AccountMicroservice.daos.UserDao;
import com.quicktutorials.learnmicroservices.AccountMicroservice.entities.Account;
import com.quicktutorials.learnmicroservices.AccountMicroservice.entities.Operation;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@SpringBootApplication @Configuration
public class AccountMicroserviceApplication implements CommandLineRunner {

	@Autowired
	UserDao userDao;

	@Autowired
	AccountDao accountDao;

	@Autowired
	OperationDao operationDao;



	private static final Logger log = LoggerFactory.getLogger(AccountMicroserviceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AccountMicroserviceApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		//...
		log.info("Hello 1");

	}

	@Bean
	public BasicTextEncryptor textEncryptor(){
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword("mySecretEncriptionKeyBlaBla1234");
		return textEncryptor;
	}
}
