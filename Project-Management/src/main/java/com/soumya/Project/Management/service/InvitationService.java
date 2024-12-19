package com.soumya.Project.Management.service;

import com.soumya.Project.Management.model.Invitation;

public interface InvitationService {
    void sendInvitation(String email, Long projectId) throws Exception;
    Invitation acceptInvitation(String token, Long userId) throws Exception;
    String getTokenByUserMail(String userEmail);
    void deleteInvitationByToken(String token);
}
