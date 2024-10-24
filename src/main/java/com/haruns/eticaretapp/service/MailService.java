package com.haruns.eticaretapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private JavaMailSenderImpl mailSender;
	
	public void sendEmail(String alici,String token){
		String url="http://localhost:8080/v1/dev/user/verify-account?token="+token;
		String body="Hesabınızı onaylamak için aşağıdaki linke tıklayınız. "+url;
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(alici);
		message.setSubject("Kayıt Onayı");
		message.setText(body);
		mailSender.send(message);
	}
}