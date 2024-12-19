package com.soumya.Project.Management.service;

import com.soumya.Project.Management.model.Chat;


public interface ChatService {
    Chat createChat(Chat chat);
    Chat findChatById(Long chatId) throws Exception;
}
