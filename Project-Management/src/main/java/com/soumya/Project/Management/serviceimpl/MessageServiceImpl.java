package com.soumya.Project.Management.serviceimpl;

import com.soumya.Project.Management.model.Chat;
import com.soumya.Project.Management.model.Message;
import com.soumya.Project.Management.model.User;
import com.soumya.Project.Management.repository.MessageRepository;
import com.soumya.Project.Management.service.ChatService;
import com.soumya.Project.Management.service.MessageService;
import com.soumya.Project.Management.service.ProjectService;
import jakarta.mail.MessageAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ProjectService projectService;

    @Override
    public Message sendMessage(User sender, Long projectId, Message message) throws Exception {
        Chat chat = projectService.getChatByProjectId(projectId);
        if (!chat.getUsers().contains(sender)){
            throw new Exception("only team members can send messages in this chat..");
        }
        message.setChat(chat);
        message.setCreatedAt(LocalDateTime.now());
        message.setSender(sender);

        Message savedMessage = messageRepository.save(message);
        chat.getMessages().add(savedMessage);
        return savedMessage;
    }

    @Override
    public List<Message> getMessagesByChatId(Long chatId) {
        return messageRepository.findByChatId(chatId);
    }

    @Override
    public List<Message> getMessagesByProjectId(Long projectId) throws Exception {
        Chat chat = projectService.getChatByProjectId(projectId);
        return messageRepository.findByChatIdOrderByCreatedAtAsc(chat.getId());
    }

    @Override
    public String deleteMessageById(Long messageId,User user) throws Exception {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isEmpty()){
            throw new Exception("no message found with this id");
        }
        Message message = optionalMessage.get();
        if (message.getSender() != user){
            throw new Exception("you are not permitted to delete this message!");
        }
        messageRepository.delete(message);
        return "message deleted";
    }
}
