package com.app.toaster.external.client.discord;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${discord.name}", url = "${discord.webhook-url}")
public interface DiscordClient {
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	void sendMessage(@RequestBody DiscordMessage discordMessage);
}