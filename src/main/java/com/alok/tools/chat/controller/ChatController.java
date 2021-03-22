package com.alok.tools.chat.controller;

import com.alok.tools.chat.model.ChatMessage;
import com.alok.tools.chat.model.ChatNotification;
import com.alok.tools.chat.service.ChatMessageService;
import com.alok.tools.chat.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private WebSocketMessageBrokerStats webSocketMessageBrokerStats;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage, Principal principal) {
        chatMessage.setTimestamp(new Date());
        chatMessage.setSenderId(principal.getName());
        chatMessage.setSenderName(principal.getName());

        Optional<String> chatId = chatRoomService
                .getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.get());

        ChatMessage saved = chatMessageService.save(chatMessage);

        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),
                "/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName(),
                        saved.getContent(),
                        saved.getTimestamp()
                )
        );

    }

    @GetMapping("/api/stats")
    public @ResponseBody WebSocketMessageBrokerStats showStats() {
        return webSocketMessageBrokerStats;
    }
}
