package com.walteann.libraryapi.service.impl;

import java.util.List;

import com.walteann.libraryapi.service.EmailService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${application.mail.default-remetent}")
    private String remetent;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMails(String message, List<String> mailList) {
        
        String[] mails = mailList.toArray(new String[mailList.size()]);

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(remetent);
        mailMessage.setSubject("Livro com empréstimo atrasado");
        mailMessage.setText(message);
        mailMessage.setTo(mails);

        javaMailSender.send(mailMessage);
    }
    
}
