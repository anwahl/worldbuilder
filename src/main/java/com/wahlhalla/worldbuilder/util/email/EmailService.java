package com.wahlhalla.worldbuilder.util.email;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


@Component
public class EmailService {

    @Value("${zeptomail.key}")
    private String zeptomailKey;

    public void sendMail(String subject, String body, String address) throws AddressException, MessagingException {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "smtp.zeptomail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.from", "fromaddress");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        Session session = Session.getDefaultInstance(properties);
        Message message = new MimeMessage(session);
		message.setSubject(subject);
		message.setText(body);
        Address addr = new InternetAddress(address);
        Address[] addresses = new Address[1];
        addresses[0] = addr;
		message.setRecipient(RecipientType.TO, addr);
		message.setFrom(new InternetAddress("no-reply@wahlhalla.com"));
        try {
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.zeptomail.com", 587, "emailapikey", zeptomailKey);
            transport.sendMessage(message, addresses); 
            transport.close();
            System.out.println("Mail successfully sent");
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }
    }
}
