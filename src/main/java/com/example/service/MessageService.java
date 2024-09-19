package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.MessageException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private AccountRepository accountRepository;

    public Message createMessage(Message message) throws MessageException {
        validateMessageText(message.getMessageText());
        if (message.getPostedBy() == null || !this.accountRepository.existsById(message.getPostedBy()))
            throw new MessageException("Invalid postedBy account ID");
        return this.messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return this.messageRepository.findAll();
    }

    public Message getMessageById(Integer id) {
        return this.messageRepository.findById(id).orElse(null);
    }

    public boolean deleteMessage(Integer messageId) {
        Message message = this.getMessageById(messageId);
        if (message != null) {
            this.messageRepository.deleteById(messageId);
            return true;
        } else return false;
    }

    public boolean patchMessage(Integer messageId, Message patch) throws MessageException {
        Message message = this.getMessageById(messageId);
        if (message != null) {
            validateMessageText(patch.getMessageText());
            message.setMessageText(patch.getMessageText());
            this.messageRepository.save(message);
            return true;
        } else return false;
    }

    public List<Message> getMessagesByUser(int accountID) {
        return this.messageRepository.findByPostedBy(accountID);
    }

    private void validateMessageText(String messageText) throws MessageException {
        if (messageText == null || messageText.isBlank() || messageText.length() > 255)
            throw new MessageException("Invalid message text");
    }
}
