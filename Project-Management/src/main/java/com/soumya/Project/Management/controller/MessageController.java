package com.soumya.Project.Management.controller;

import com.soumya.Project.Management.model.Message;
import com.soumya.Project.Management.model.User;
import com.soumya.Project.Management.service.MessageService;
import com.soumya.Project.Management.service.UserService;
import jakarta.mail.MessageAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @PostMapping("/send/{projectId}")
    public ResponseEntity<?> createMessage(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long projectId,
            @RequestBody Message message) throws Exception {

        User user = userService.findProfileByJwt(jwt);
        Message sentMessage = messageService.sendMessage(user, projectId, message);
        return new ResponseEntity<>(sentMessage, HttpStatus.CREATED);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> getMessagesByChatId(
            @PathVariable Long chatId) throws Exception {
        List<Message> messageList = messageService.getMessagesByChatId(chatId);
        return new ResponseEntity<>(messageList,HttpStatus.OK);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getMessagesByProjectId(
            @PathVariable Long projectId) throws Exception {
        List<Message> messageList = messageService.getMessagesByProjectId(projectId);
        return new ResponseEntity<>(messageList,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<?> deleteMessageById(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long messageId) throws Exception {

        User user = userService.findProfileByJwt(jwt);
        String response = messageService.deleteMessageById(messageId, user);
        return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }
}
