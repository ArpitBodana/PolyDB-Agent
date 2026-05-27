package com.absys.io.polydb.agent.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agent")
@RequiredArgsConstructor
public class AiController {

    private final ChatClient chatClient;


    @GetMapping("/query")
    public ResponseEntity<String> query(@RequestParam String query, @RequestParam String userId) {
        String result = chatClient
                .prompt()
                .user(query)
                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID,
                        userId
                ))
                .call()
                .content();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
