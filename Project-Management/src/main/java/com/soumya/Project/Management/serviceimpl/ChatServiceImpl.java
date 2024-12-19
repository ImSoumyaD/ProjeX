package com.soumya.Project.Management.serviceimpl;

import com.soumya.Project.Management.model.Chat;
import com.soumya.Project.Management.repository.ChatRepository;
import com.soumya.Project.Management.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    ChatRepository chatRepository;

    @Override
    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }
    @Override
    public Chat findChatById(Long chatId) throws Exception {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isEmpty()){
            throw new Exception("chat not found with this id");
        }
        return chatOptional.get();
    }
}
