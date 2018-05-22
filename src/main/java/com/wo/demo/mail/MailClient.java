package com.wo.demo.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class MailClient {
	private JavaMailSender mailSender;

	@Autowired
	public MailClient(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void prepareAndSend(String recipient, String message) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setTo(recipient);
			messageHelper.setSubject("Spring demo verification test");
			messageHelper.setText(message);
//			messageHelper.setText("Verification Mail", message);
		};
		try {
			mailSender.send(messagePreparator);
		} catch (MailException e) {
			System.out.println("Mail send Exception : "+e);
		}
	}
}
