package com.devsu.push.sender.callback;

import java.util.Map;

/**
 * Callback used for the async services.
 */
public interface PushCallback {

	/**
	 * Callback that is executed when a single push message is sent.
	 * @param result Value that indicates if a push message request was correctly sent to APNS/GCM.
	 * @param title The push message title.
	 * @param message The push message content.
	 * @param additionalFields The additional fields sent on the push message.
	 * @param token The push token.
	 */
	void onSingleSuccess(boolean result, String title, String message, Map<String, String> additionalFields, String token);

	/**
	 * Callback that is executed when a bulk push message is sent.
	 * @param result Value that indicates if a push message request was correctly sent to APNS/GCM.
	 * @param title The push message title.
	 * @param message The push message content.
	 * @param additionalFields The additional fields sent on the push message.
	 * @param tokens The push tokens.
	 */
	void onBulkSuccess(boolean result, String title, String message, Map<String, String> additionalFields, String[] tokens);

	/**
	 * Callback that is executed when a push message request throws an exception.
	 * @param t The exception that occurred.
	 */
	void onError(Throwable t);
}
