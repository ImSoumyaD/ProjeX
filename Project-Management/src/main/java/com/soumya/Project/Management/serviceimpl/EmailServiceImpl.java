package com.soumya.Project.Management.serviceimpl;

import com.soumya.Project.Management.model.Project;
import com.soumya.Project.Management.service.EmailService;
import com.soumya.Project.Management.service.ProjectService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    ProjectService projectService;

    @Override
    public void sendEmailWithToken(String userEmail, String link,Long projectId) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");

        Project project = projectService.getProjectById(projectId);

        mimeMessage.addHeader("Importance", "high");
        String sub = "Invitation to join the project";
        String text = "<p>Hello,</p>"
                + "<p>You have been invited to join the project <strong>" + project.getName() + "</strong>.</p>"
                + "<p>Project Description: " + project.getDescription() + "</p>"
                + "<p>To join the project team, please click on the link below:</p>"
                + "<p><a href='" + link + "'>"+link+"</a></p>";

        helper.setSubject(sub);
        helper.setText(text,true);
        helper.setTo(userEmail);
        try {
            javaMailSender.send(mimeMessage);
        }catch (MailSendException e){
            throw new MailSendException("Failed to send the email, "+e.getMessage());
        }
    }
}
