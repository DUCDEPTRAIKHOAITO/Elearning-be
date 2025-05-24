package org.elearning.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailSender {

    public static void sendPasswordEmail(String toEmail, String userName, String password) {
        final String fromEmail = "vovanhoang2222ef@gmail.com"; // Email người gửi
        final String fromPassword = "eoac sagy mfvz wlkp";       // Mật khẩu ứng dụng (App Password)

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP của Gmail
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, fromPassword);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "Elearning"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Tài khoản của bạn");

            String content = String.format(
                    "Chào %s,\n\nTài khoản của bạn đã được tạo. Dưới đây là thông tin đăng nhập:\n\nUsername: %s\nPassword: %s\n\n\nTrân trọng,\nElearnig",
                    userName, userName, password
            );

            message.setText(content);

            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to send email.");
        }
    }
}
