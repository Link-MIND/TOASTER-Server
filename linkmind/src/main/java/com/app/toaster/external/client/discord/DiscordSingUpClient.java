package com.app.toaster.external.client.discord;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${discord.error-name}", url = "${discord.webhook-url-sign}")
public interface DiscordSingUpClient {
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	void sendMessage(@RequestBody DiscordMessage discordMessage);
}