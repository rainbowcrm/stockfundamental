package com.primus.utils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import com.primus.common.CommonErrorCodes;
import com.primus.common.Configuration;
import com.primus.common.PrimusError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    Configuration configuration ;

    private Properties getProperties()
    {
        Properties props = new Properties();
        //props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
//        props.setProperty("mail.smtp.port", "25");
        props.put("mail.smtp.starttls.enable", "true");
    //    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.port", "587");

        return props;
    }
    public void sendPasswordEmail(String recEmail,String password) throws PrimusError
    {
        try {
          //  Session session = Session.getInstance(getProperties());
            Session session = Session.getInstance(getProperties(),
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(configuration.getEmailUser(), configuration.getEmailPassword());
                        }
                    });
            MimeMessage message = new MimeMessage(session);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent("<img>", "text/html");
            message.setText("Your password is " + password);
            message.setFrom(new InternetAddress(configuration.getEmailUser()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recEmail));
            message.setSubject("Password Recovery");

            Transport.send(message);


        }catch (MessagingException mex)
        {
            mex.printStackTrace();;
            throw new PrimusError(CommonErrorCodes.EMAIL_NOT_SEND,"Could not send email");
        }


    }
}
