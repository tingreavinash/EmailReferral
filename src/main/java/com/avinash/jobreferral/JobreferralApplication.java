package com.avinash.jobreferral;

import com.avinash.jobreferral.bean.Contact;
import com.avinash.jobreferral.services.MailService;
import com.avinash.jobreferral.services.MailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class JobreferralApplication {

	@Autowired
	MailService mailService;

	private static final Logger LOG = LoggerFactory.getLogger(JobreferralApplication.class);

	public static void main(String[] args) throws IOException {

		ApplicationContext ctx = SpringApplication.run(JobreferralApplication.class, args);
		MailService mailService = (MailService) ctx.getBean("mailService");
		mailService.prepareAndSendMail();

		if(!mailService.getSuccessfulDeliveries().isEmpty()){
			LOG.info("Mail delivery successful to "+ mailService.getSuccessfulDeliveries().size()+ " contacts:");
			for(Contact contact: mailService.getSuccessfulDeliveries()){
				LOG.info("Name: "+contact.getFirstName()+ " "+contact.getLastName()+ ", Email: "+ contact.getEmail());
			}
		}

		if(!mailService.getFailedDeliveries().isEmpty()){
			LOG.error("Mail delivery failed to "+ mailService.getFailedDeliveries().size() + " contacts:");
			for(Contact contact: mailService.getFailedDeliveries()){
				LOG.error("Name: "+contact.getFirstName()+ " "+contact.getLastName()+ ", Email: "+ contact.getEmail());
			}
		}

	}

}
