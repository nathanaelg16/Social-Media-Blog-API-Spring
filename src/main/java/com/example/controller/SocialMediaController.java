package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.*;
import com.example.exception.AccountRegistrationException;
import com.example.exception.DuplicateAccountRegistrationException;
import com.example.exception.MessageException;
import com.example.service.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
 public class SocialMediaController {
    
    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;


    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
        try {
            return ResponseEntity.ok(this.accountService.registerAccount(account));
        } catch (DuplicateAccountRegistrationException e) {
            return ResponseEntity.status(409).build();
        } catch (AccountRegistrationException e) {
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account loggedIn = this.accountService.login(account);
        if (loggedIn == null) return ResponseEntity.status(401).build();
        else return ResponseEntity.ok(loggedIn);
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        try {
            return ResponseEntity.ok(this.messageService.createMessage(message));
        } catch (MessageException e) {
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(this.messageService.getAllMessages());
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        return ResponseEntity.ok(this.messageService.getMessageById(messageId));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
        if (this.messageService.deleteMessage(messageId))
            return ResponseEntity.ok(1);
        else return ResponseEntity.ok().build();
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> patchMessageById(@PathVariable Integer messageId, @RequestBody Message message) {
        try {
            if (this.messageService.patchMessage(messageId, message))
                return ResponseEntity.ok(1);
        } catch (MessageException e) {}
        return ResponseEntity.status(400).build();
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllUserMessages(@PathVariable Integer accountId) {
        return ResponseEntity.ok(this.messageService.getMessagesByUser(accountId));
    }
}
