package com.devsu.push.sender.service.async;

import java.util.Map;

import com.devsu.push.sender.callback.PushCallback;

/**
 * Interface for async Push Services.
 */
public interface AsyncPushService {

	/**
	 * Sends a single push message.
	 * @param message The push message content.
	 * @param token The push token.
	 */
	void sendPush(String message, String token);

	/**
	 * Sends a single push message.
	 * @param title The push message title.
	 * @param message The push message content.
	 * @param token The push token.
	 */
	void sendPush(String title, String message, String token);
	
	/**
	 * Sends a single push message.
	 * @param title The push message title.
	 * @param message The push message content.
	 * @param additionalFields The additional fields sent on the push message.
	 * @param token The push token.
	 */
	void sendPush(String title, String message, Map<String, String> additionalFields, String token);

	/**
	 * Sends a bulk push message.
	 * @param message The push message content.
	 * @param tokens The push tokens.
	 */
	void sendPushInBulk(String message, String... tokens);

	/**
	 * Sends a bulk push message.
	 * @param title The push message title.
	 * @param message The push message content.
	 * @param tokens The push tokens.
	 */
	void sendPushInBulk(String title, String message, String... tokens);
	
	/**
	 * Sends a bulk push message.
	 * @param title The push message title.
	 * @param message The push message content.
	 * @param additionalFields The additional fields sent on the push message.
	 * @param tokens The push tokens.
	 */
	void sendPushInBulk(String title, String message, Map<String, String> additionalFields, String... tokens);

	/**
	 * Sets the pushCallback.
	 * @param pushCallback The push callback.
	 */
	void setPushCallback(PushCallback pushCallback);
}