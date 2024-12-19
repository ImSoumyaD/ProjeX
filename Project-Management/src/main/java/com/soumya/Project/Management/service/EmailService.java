package com.soumya.Project.Management.service;

public interface EmailService {
    public void sendEmailWithToken(String userEmail,String link,Long projectId) throws Exception;
}
