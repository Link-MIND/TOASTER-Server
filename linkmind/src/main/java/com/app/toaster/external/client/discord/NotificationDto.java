package com.app.toaster.external.client.discord;


public record NotificationDto(
	NotificationType type,
	Exception e,
	String request
) {
}
