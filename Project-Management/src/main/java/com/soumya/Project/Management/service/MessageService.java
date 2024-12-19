package com.soumya.Project.Management.service;

import com.soumya.Project.Management.model.Message;
import com.soumya.Project.Management.model.User;

import java.util.List;

public interface MessageService {
    Message sendMessage(User sender, Long projectId, Message message) throws Exception;
    List<Message> getMessagesByChatId(Long chatId);
    List<Message> getMessagesByProjectId(Long projectId) throws Exception;
    String deleteMessageById(Long messageId,User user) throws Exception;
}
