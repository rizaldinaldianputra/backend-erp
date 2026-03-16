package com.erp.erp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, false); // Default to plain text

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendOtpEmail(String to, String otp) {
        String subject = "Kode Verifikasi OTP - SkyERP";

        String htmlContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "</head>" +
                "<body style='margin:0;padding:0;background-color:#f4f6f9;font-family:Arial,Helvetica,sans-serif;'>"

                + "<table width='100%' cellpadding='0' cellspacing='0' style='background:#f4f6f9;padding:40px 0;'>"
                + "<tr>"
                + "<td align='center'>"

                + "<table width='600' cellpadding='0' cellspacing='0' style='background:#ffffff;border-radius:10px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,0.08);'>"

                // HEADER
                + "<tr>"
                + "<td style='background:#2563eb;padding:20px;text-align:center;'>"
                + "<h1 style='color:#ffffff;margin:0;font-size:24px;'>SkyERP</h1>"
                + "<p style='color:#dbeafe;margin:5px 0 0;font-size:13px;'>Enterprise Resource Planning System</p>"
                + "</td>"
                + "</tr>"

                // BODY
                + "<tr>"
                + "<td style='padding:40px;'>"

                + "<h2 style='margin-top:0;color:#111827;'>Verifikasi Akun Anda</h2>"

                + "<p style='color:#374151;font-size:14px;line-height:1.6;'>"
                + "Kami menerima permintaan untuk memverifikasi akun Anda. "
                + "Gunakan kode OTP berikut untuk melanjutkan proses."
                + "</p>"

                // OTP BOX
                + "<div style='margin:35px 0;text-align:center;'>"
                + "<div style='display:inline-block;padding:18px 30px;"
                + "font-size:32px;font-weight:bold;"
                + "letter-spacing:8px;"
                + "background:#eff6ff;"
                + "border:2px dashed #2563eb;"
                + "border-radius:8px;"
                + "color:#1e3a8a;'>"
                + otp +
                "</div>"
                + "</div>"

                + "<p style='font-size:14px;color:#6b7280;text-align:center;'>"
                + "Kode ini berlaku selama <b>5 menit</b>"
                + "</p>"

                + "<hr style='border:none;border-top:1px solid #e5e7eb;margin:30px 0;'>"

                + "<p style='font-size:13px;color:#6b7280;line-height:1.6;'>"
                + "Jika Anda tidak meminta kode ini, silakan abaikan email ini. "
                + "Jangan bagikan kode OTP ini kepada siapa pun demi keamanan akun Anda."
                + "</p>"

                + "</td>"
                + "</tr>"

                // FOOTER
                + "<tr>"
                + "<td style='background:#f9fafb;padding:20px;text-align:center;'>"
                + "<p style='margin:0;font-size:12px;color:#9ca3af;'>"
                + "© 2026 SkyERP System"
                + "</p>"
                + "<p style='margin:5px 0 0;font-size:12px;color:#9ca3af;'>"
                + "Email ini dikirim secara otomatis. Mohon tidak membalas email ini."
                + "</p>"
                + "</td>"
                + "</tr>"

                + "</table>"
                + "</td>"
                + "</tr>"
                + "</table>"

                + "</body>"
                + "</html>";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
}
