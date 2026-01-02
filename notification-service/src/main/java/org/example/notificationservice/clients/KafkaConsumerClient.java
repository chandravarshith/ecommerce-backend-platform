package org.example.notificationservice.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.notificationservice.dtos.EmailDto;
import org.example.notificationservice.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

@Configuration
public class KafkaConsumerClient {
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${email.password}")
    private String password;

    private final String groupId = "NOTIFICATION_SERVICE";

    private final String signupTopic = "SIGNUP";

    @KafkaListener(topics = signupTopic, groupId = groupId)
    public void sendEmail(String message){
        try{
            EmailDto emailDto = this.objectMapper.readValue(message, EmailDto.class);

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailDto.getSender(), password);
                }
            };
            Session session = Session.getInstance(props, auth);

            EmailUtil.sendEmail(session, emailDto.getRecipient(),
                    emailDto.getSubject(), emailDto.getMessage());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
