package com.app.toaster.external.client.discord;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.app.toaster.exception.Error;
import com.app.toaster.exception.model.CustomException;
import com.app.toaster.infrastructure.UserRepository;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DiscordMessageProvider {
    private final DiscordSingUpClient discordSingUpClient;
    private final UserRepository userRepository;
    private final Environment environment;

    public void sendNotification(NotificationType type, Exception e, String request) {
        if (!Arrays.asList(environment.getActiveProfiles()).contains("local")) {
            try {
                switch (type){
                    case ERROR -> discordSingUpClient.sendMessage(createErrorMessage(e,request));
                    case SINGUP -> discordSingUpClient.sendMessage(createSignUpMessage());
                }
            } catch (FeignException error) {
                throw new CustomException(Error.INVALID_DISCORD_MESSAGE,
                    Error.INVALID_APPLE_IDENTITY_TOKEN.getMessage());
            }
        }
    }

    private DiscordMessage createSignUpMessage() {
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


    private DiscordMessage createErrorMessage(Exception e, String requestUrl) {
        return DiscordMessage.builder()
            .content("# 🚨 삐용삐용 에러났어요 에러났어요")
            .embeds(
                List.of(
                    DiscordMessage.Embed.builder()
                        .title("ℹ️ 에러 정보")
                        .description(
                            "### 🕖 발생 시간\n"
                                + LocalDateTime.now()
                                + "\n"
                                + "### 🔗 요청 URL\n"
                                + requestUrl
                                + "\n"
                                + "### 📄 Stack Trace\n"
                                + "```\n"
                                + getStackTrace(e).substring(0, 1000)
                                + "\n```")
                        .build()
                )
            )
            .build();
    }

    private String createRequestFullPath(WebRequest webRequest) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        String fullPath = request.getMethod() + " " + request.getRequestURL();

        String queryString = request.getQueryString();
        if (queryString != null) {
            fullPath += "?" + queryString;
        }

        return fullPath;
    }

    private String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}