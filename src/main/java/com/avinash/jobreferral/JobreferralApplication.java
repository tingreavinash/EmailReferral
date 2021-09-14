package com.avinash.jobreferral;

import com.avinash.jobreferral.services.MailService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
public class JobreferralApplication {

	public static void main(String[] args) throws IOException {

		ApplicationContext ctx = SpringApplication.run(JobreferralApplication.class, args);
		MailService mailService = (MailService) ctx.getBean("mailService");
		mailService.prepareAndSendMail();
	}

}
