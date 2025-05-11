package org.example.otp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String userEmail, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);

            helper.setFrom("yashrajladhani9@gmail.com");
            helper.setTo(userEmail);
            helper.setSubject("Otp Verification");
            helper.setText("Your OTP for registration is: " + otp,  true);
            mailSender.send(mimeMessage);


        } catch (MessagingException e) {
            System.out.println("Error occurred while sending email: " + e.getMessage());
            throw new RuntimeException("Error sending email: " + e.getMessage());
        }
    }


}
