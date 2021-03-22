package com.alok.tools.chat.service;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
public class StompEventListener implements ApplicationListener<SessionConnectEvent> {

    @Override
    public void onApplicationEvent(SessionConnectEvent sessionConnectEvent) {
        Principal principal = sessionConnectEvent.getUser();
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(sessionConnectEvent.getMessage());

        System.out.format("Command: %s, session id: %s, user id: %s\n",
                stompHeaderAccessor.getCommand(), stompHeaderAccessor.getSessionId(), principal.getName());
    }

    @EventListener
    public void onSocketConnect(SessionConnectedEvent sessionConnectedEvent) {
        Principal principal = sessionConnectedEvent.getUser();
        System.out.format("[Connected] %s\n", principal == null ? null: principal.getName());
    }

    @EventListener
    public void onSocketDisconnect(SessionDisconnectEvent sessionDisconnectEvent) {
        Principal principal = sessionDisconnectEvent.getUser();
        System.out.format("[Disconnected] %s\n", principal == null ? null: principal.getName());

    }
}
