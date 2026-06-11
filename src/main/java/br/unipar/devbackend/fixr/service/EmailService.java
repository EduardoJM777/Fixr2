package br.unipar.devbackend.fixr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarConfirmacao(String destinatario, String token) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(destinatario);
        msg.setSubject("Confirme seu cadastro no FIXR");
        msg.setText(
                "Olá! Clique no link abaixo para confirmar seu cadastro:\n\n" +
                        "http://localhost:4200/confirmar-email?token=" + token + "\n\n" +
                        "Se não foi você, ignore este email."
        );
        mailSender.send(msg);
    }
}