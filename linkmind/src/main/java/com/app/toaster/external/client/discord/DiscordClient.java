package com.app.toaster.external.client.discord;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "discord-feign-client", url = "URI")
public interface DiscordClient {
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	void sendMessage(URI uri, @RequestBody DiscordMessage discordMessage);
}