package com.orp.utils.email;

import com.orp.dto.ClaimEmailDTTO;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${spring.mail.hostpage}")
    private String hostPage;

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Async
    public void sendHtmlEmail(ClaimEmailDTTO claimEmailDTTO, List<String> toList, String receiver, String mailTemplate, String url, String content) {
        try {
            Context context = getContext(claimEmailDTTO, receiver, url, content);
            String text = templateEngine.process(mailTemplate, context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromEmail);
            helper.setSubject(createSubject(claimEmailDTTO));
            helper.setTo(toList.toArray(new String[0]));
            helper.setText(text, true);
            mailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private Context getContext(ClaimEmailDTTO claimEmailDTTO, String receiver, String url, String content) {
        Integer staffId = claimEmailDTTO.getStaffId();
        String staffName = claimEmailDTTO.getStaffName();
        String projectName = claimEmailDTTO.getProjectName();

        Context context = new Context();
        context.setVariable("receiver", receiver);
        context.setVariable("staffId", staffId);
        context.setVariable("staffName", staffName);
        context.setVariable("projectName", projectName);
        context.setVariable("content", content);
        context.setVariable("url", hostPage + url);

        return context;
    }

    private String createSubject(ClaimEmailDTTO claimEmailDTTO) {
        return String.format("Overtime Request for %s by %s-%s", claimEmailDTTO.getProjectName(), claimEmailDTTO.getStaffName(), claimEmailDTTO.getStaffId());
    }

    private MimeMessage getMimeMessage() {
        return mailSender.createMimeMessage();
    }
}
