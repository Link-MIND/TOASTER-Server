package com.app.toaster.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.toaster.external.client.discord.DiscordMessageProvider;
import com.app.toaster.external.client.discord.NotificationDto;
import com.app.toaster.external.client.discord.NotificationType;
import com.app.toaster.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {
    private final DiscordMessageProvider discordMessageProvider;

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}
