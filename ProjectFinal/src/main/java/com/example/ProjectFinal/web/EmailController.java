package com.example.ProjectFinal.web;
import com.example.ProjectFinal.entities.User;
import com.example.ProjectFinal.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;


@RestController
public class EmailController {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    @Autowired
    public EmailController(JavaMailSender javaMailSender, UserRepository userRepository) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
    }
    @PostMapping("/api/contact")
    public ResponseEntity<String> handleContactForm(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message
    ) {
        try {
            sendEmailToClinic(name, email, subject, message);
            return ResponseEntity.ok("Votre message a été envoyé avec succès à la clinique. Merci !");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de l'envoi du message. Veuillez réessayer plus tard.");
        }
    }

    @PostMapping("/api/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam("email") String userEmail,
                                                       HttpServletRequest request) {
        User user = userRepository.findByLogin(userEmail);
        if (user != null) {
            try {
                String verificationCode = generateVerificationCode();
                sendVerificationEmail(userEmail, verificationCode);
                HttpSession session = request.getSession(true);
                session.setAttribute("email", userEmail);
                session.setAttribute("verificationCode", verificationCode);
                return ResponseEntity.ok().body("email sent");
            }catch (MailException e) {
                String errorMessage;
                if (e.getCause() instanceof java.net.UnknownHostException) {
                    errorMessage = "Impossible de se connecter au serveur de messagerie. Veuillez réessayer plus tard.";
                } else {
                    errorMessage = "Une erreur s'est produite lors de l'envoi de l'e-mail de vérification. Veuillez réessayer plus tard.";
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun utilisateur trouvé avec cette adresse email");
        }
    }


    private void sendEmailToClinic(String name, String email, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");

        String emailContent = "Nom : " + name + "\n\nEmail : " + email + "\n\nSujet : " + subject + "\n\nMessage : " + message;

        helper.setTo("zougarhaya@gmail.com");
        helper.setSubject("Nouveau message de la part de " + name);
        helper.setText(emailContent);

        javaMailSender.send(mimeMessage);
    }

    private void sendVerificationEmail(String userEmail, String verificationCode) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");

        String emailContent = "Bonjour,\n\nVoici votre code de vérification : " + verificationCode;

        helper.setTo(userEmail);
        helper.setSubject("Code de vérification");
        helper.setText(emailContent);

        javaMailSender.send(mimeMessage);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }
}
