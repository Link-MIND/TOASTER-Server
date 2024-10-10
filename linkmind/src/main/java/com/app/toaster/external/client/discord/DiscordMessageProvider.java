package com.app.toaster.external.client.discord;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.CustomException;
import com.app.toaster.infrastructure.UserRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DiscordMessageProvider {
    private final DiscordClient discordClient;
    private final UserRepository userRepository;

    public void sendSignUpNotification() {
        sendMessageToDiscord(createSingUpMessage());
    }

    private void sendMessageToDiscord(DiscordMessage discordMessage) {
        try {
            discordClient.sendMessage(discordMessage);
        } catch (FeignException e) {
            throw new CustomException(Error.INVALID_DISCORD_MESSAGE, Error.INVALID_APPLE_IDENTITY_TOKEN.getMessage());
        }
    }

    private DiscordMessage createSingUpMessage() {
        return DiscordMessage.builder()
            .content("# 😍 회원가입 이벤트가 발생했습니다.")
            .embeds(
                List.of(
                    DiscordMessage.Embed.builder()
                        .title("ℹ️ 회원가입 정보")
                        .description(
                            "### 🕖 발생 시간\n"
                                + LocalDateTime.now()
                                + "\n"
                                + "### 📜 유저 가입 정보\n"
                                + "토스터의 " + userRepository.count() + "번째 유저가 생성되었습니다!! ❤️"
                                + "\n")
                        .build()
                )
            )
            .build();
    }
}