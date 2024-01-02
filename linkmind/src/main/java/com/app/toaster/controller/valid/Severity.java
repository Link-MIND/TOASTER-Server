package com.app.toaster.controller.valid;

import jakarta.validation.Payload;

public class Severity {
	public static class Info implements Payload {};
	public static class Error implements Payload {};
}
