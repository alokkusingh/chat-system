package com.alok.tools.chat.service;

import com.alok.tools.chat.model.ChatRoom;
import com.alok.tools.chat.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService {

    @Autowired private ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatId(
            String senderId, String recipientId, boolean createIfNotExist) {

        return Optional.ofNullable(chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .orElseGet(() -> createChatId(senderId, recipientId, createIfNotExist)));
    }

    private String createChatId(String senderId, String receipientId, boolean createIfNotExist) {
        if (!createIfNotExist)
            return null;

        String chatId = String.format("%s_%s", senderId, receipientId);

        ChatRoom senderRecipient = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(receipientId)
                .build();

        ChatRoom recipientSender = ChatRoom.builder()
                .chatId(chatId)
                .senderId(receipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }
}
