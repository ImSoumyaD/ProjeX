package com.soumya.Project.Management.repository;

import com.soumya.Project.Management.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findByChatId(Long chatId);
    List<Message> findByChatIdOrderByCreatedAtAsc(Long chatId);
}
