package br.unipar.devbackend.fixr.service;

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

    public void enviarConfirmacao(String destinatario, String token) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject("Confirme seu cadastro no FIXR");

            String link = "http://localhost:4200/confirmar-email?token=" + token;

            String html = """
                <div style="font-family: Arial, sans-serif; max-width: 480px; margin: 0 auto; padding: 32px; background: #f5f5f5; border-radius: 8px;">
                    <h1 style="color: #1a5f7a; font-size: 28px; margin-bottom: 8px;">FIXR</h1>
                    <h2 style="color: #333; font-size: 18px; font-weight: 400;">Confirme seu cadastro</h2>
                    <p style="color: #555; font-size: 15px; line-height: 1.6;">
                        Olá! Clique no botão abaixo para confirmar seu cadastro e começar a usar o FIXR.
                    </p>
                    <a href="%s"
                       style="display: inline-block; margin: 24px 0; padding: 14px 32px;
                              background: #1a5f7a; color: #ffffff; text-decoration: none;
                              border-radius: 6px; font-size: 15px; font-weight: 600;">
                        Confirmar email
                    </a>
                    <p style="color: #999; font-size: 13px;">
                        Se não foi você quem se cadastrou, ignore este email.
                    </p>
                </div>
            """.formatted(link);

            helper.setText(html, true);
            mailSender.send(msg);

        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar email de confirmação.", e);
        }
    }
}