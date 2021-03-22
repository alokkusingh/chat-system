package com.alok.tools.chat.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Message<?> preSend(Message message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String jwtToken = accessor.getFirstNativeHeader("X-Authorization");
            accessor.removeHeader("X-Authorization");

            accessor.setUser(jwtUtils.getAuthenticatedPrincipal(jwtToken));
        }

        return message;
    }
}
