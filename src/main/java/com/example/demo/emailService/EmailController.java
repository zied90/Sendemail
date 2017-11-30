package com.example.demo.emailService;



import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sending")
public class EmailController {
	@Value("${gmail.username}")
	private String username;
	
	@Value("${gmail.password}")
	private String password;
	
@RequestMapping(value="/send",method=RequestMethod.POST)
	public String sendEmail(@RequestBody EmailMessage emailmessage) throws AddressException, MessagingException, IOException{
	sendmail(emailmessage);
	return "Email Send successufully";
}


private void sendmail(EmailMessage emailmessage) throws AddressException, MessagingException, IOException{
	Properties  props=new Properties();
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.starttls.enable", "true");
	props.put("mail.smtp.host", "smtp.gmail.com");
	props.put("mail.smtp.port", "587");
	Session session=Session.getInstance(props, new javax.mail.Authenticator(){
		protected PasswordAuthentication getPasswordAuthentication(){
			return new PasswordAuthentication(username, password);
			
		}
		
	});
	Message msg=new MimeMessage(session);
	msg.setFrom(new InternetAddress(username,false));
	msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailmessage.getTo_address()));
	msg.setSubject(emailmessage.getSubject() );
	msg.setContent(emailmessage.getBody(), "text/html");
	msg.setSentDate(new Date());
	
	MimeBodyPart messageBodyPart=new MimeBodyPart();
	messageBodyPart.setContent(emailmessage.getBody(), "text/html");
	Multipart multipart=new MimeMultipart();
	multipart.addBodyPart(messageBodyPart);
	MimeBodyPart attchPart=new MimeBodyPart();
	attchPart.attachFile("Macintosh HD/Project2/10.jpg");
	multipart.addBodyPart(attchPart);
	msg.setContent(multipart);
	Transport.send(msg);
}
}
