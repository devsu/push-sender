package com.devsu.push.sender.service.sync;

import java.util.Map;

public interface SyncPushService {

	/**
	 * Sends a single push message.
	 * @param message The push message content.
	 * @param token The push token.
	 * @return <i>true</i> if the push message request was sent. 
	 * @throws Exception Any exception that may arise.
	 */
	boolean sendPush(String message, String token) throws Exception;

	/**
	 * Sends a single push message.
	 * @param title The push message title.
	 * @param message The push message content.
	 * @param token The push token.
	 * @return <i>true</i> if the push message request was sent. 
	 * @throws Exception Any exception that may arise.
	 */
	boolean sendPush(String title, String message, String token) throws Exception;

	/**
	 * Sends a single push message.
	 * @param title The push message title.
	 * @param message The push message content.
	 * @param additionalFields The additional fields sent on the push message.
	 * @param token The push token.
	 * @return <i>true</i> if the push message request was sent. 
	 * @throws Exception Any exception that may arise.
	 */
	boolean sendPush(String title, String message, Map<String, String> additionalFields, String token) throws Exception;

	/**
	 * Sends a bulk push message.
	 * @param message The push message content.
	 * @param tokens The push tokens.
	 * @return <i>true</i> if the push message request was sent. 
	 * @throws Exception Any exception that may arise.
	 */
	boolean sendPushInBulk(String message, String... tokens) throws Exception;

	/**
	 * Sends a bulk push message.
	 * @param title The push message title.
	 * @param message The push message content.
	 * @param tokens The push tokens.
	 * @return <i>true</i> if the push message request was sent. 
	 * @throws Exception Any exception that may arise.
	 */
	boolean sendPushInBulk(String title, String message, String... tokens) throws Exception;

	/**
	 * Sends a bulk push message.
	 * @param title The push message title.
	 * @param message The push message content.
	 * @param additionalFields The additional fields sent on the push message.
	 * @param tokens The push tokens.
	 * @return <i>true</i> if the push message request was sent. 
	 * @throws Exception Any exception that may arise.
	 */
	boolean sendPushInBulk(String title, String message, Map<String, String> additionalFields, String... tokens) throws Exception;
}
