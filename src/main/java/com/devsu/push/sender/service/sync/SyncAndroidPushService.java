package com.devsu.push.sender.service.sync;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devsu.push.sender.util.ArrayUtil;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class SyncAndroidPushService extends SyncPushServiceBase {
	
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(getClass()); 
	
	/**
	 * Max retries when sending a push message.
	 */
	private int maxRetries;
	
	/**
	 * The message key that will store the key-value pair for the push message content.
	 */
	private String messageKey;
	
	/**
	 * The title key that will store the key-value pair for the push message title.
	 */
	private String titleKey;
	
	/**
	 * The quantity of push messages to be sent simultaneously on multicast requests.
	 */
	private int maxBulkSize;
	
	/**
	 * The collapse key for identifying single messages.
	 */
	private String collapseKeySingle;
	
	/**
	 * The collapse key for identifying bulk messages.
	 */
	private String collapseKeyBulk;
	
	/**
	 * The GCM service.
	 */
	private Sender senderService;
	
	/**
	 * Single param constructor.
	 * @param gcmApiKey The GCM API Key (also known as Sender ID).
	 */
	public SyncAndroidPushService(String gcmApiKey){
		setDefaultValues();
		senderService = new Sender(gcmApiKey); 
	}
	
	/**
	 * Sets the default values.
	 */
	private void setDefaultValues() {
		maxRetries = Defaults.MAX_RETRIES;
		messageKey = Defaults.MESSAGE_KEY;
		titleKey = Defaults.TITLE_KEY;
		maxBulkSize = Defaults.MAX_BULK_SIZE;
		collapseKeySingle = Defaults.COLLAPSE_KEY_SINGLE;
		collapseKeyBulk = Defaults.COLLAPSE_KEY_BULK;
		pushEnabled = Defaults.PUSH_ENABLED;
	}
	
	/*
	 * @see com.devsu.push.sender.service.sync.SyncPushService#sendPush(java.lang.String, java.lang.String, java.util.Map, java.lang.String)
	 */
	@Override
	public boolean sendPush(final String title, final String message, final Map<String, String> additionalFields, 
			final String token) throws IOException {
		if (!validateSingleData(log, message, token)) {
			return false;
		}
		Message.Builder msgBuilder = generateBuilder(title, message, true, additionalFields);
		Result result = senderService.send(msgBuilder.build(), token, maxRetries);
		return resultIsOk(result);
	}
	
	/**
	 * Sends a single push message.
	 * @param msgBuilder The Message.Builder object.
	 * @param token The push token.
	 * @return <i>true</i> if the push message request was sent. 
	 * @throws Exception Any exception that may arise.
	 */
	public boolean sendPush(Message.Builder msgBuilder, String token) throws Exception {
		if (!validateToken(log, token)) {
			return false;
		}
		Result result = senderService.send(msgBuilder.build(), token, maxRetries);
		return resultIsOk(result);
	}
	
	/*
	 * @see com.devsu.push.sender.service.sync.SyncPushService#sendPushInBulk(java.lang.String, java.lang.String, java.util.Map, java.lang.String[])
	 */
	@Override
	public boolean sendPushInBulk(final String title, final String message, 
			final Map<String, String> additionalFields, final String... tokens) throws IOException {
		if (!validateBulkData(log, message)) {
			return false;
		}
		boolean booleanResult = true;
		List<String[]> tokenLimitedList = ArrayUtil.splitArray(tokens, maxBulkSize);
		for (String[] tokenArray: tokenLimitedList){
			Message.Builder msgBuilder = generateBuilder(title, message, false, additionalFields);
			MulticastResult result = senderService.send(msgBuilder.build(), Arrays.asList(tokenArray), maxRetries);
			booleanResult = booleanResult ? resultIsOk(result) : false;
		}
		return booleanResult;
	}
	
	/**
	 * Sends a bulk push message.
	 * @param msgBuilder The Message.Builder object.
	 * @param tokens The push token.
	 * @return <i>true</i> if the push message request was sent. 
	 * @throws Exception Any exception that may arise.
	 */
	public boolean sendPushInBulk(Message.Builder msgBuilder, String... tokens) throws Exception {
		boolean booleanResult = true;
		List<String[]> tokenLimitedList = ArrayUtil.splitArray(tokens, maxBulkSize);
		for (String[] tokenArray: tokenLimitedList){
			MulticastResult result = senderService.send(msgBuilder.build(), Arrays.asList(tokenArray), maxRetries);
			booleanResult = booleanResult ? resultIsOk(result) : false;
		}
		return booleanResult;
	}
	
	/**
	 * Default message builder generator.
	 * @param title The push message title.
	 * @param message The push message content.
	 * @param isSingle Identifies if its a single push message or a bulk push message.
	 * @param additionalFields The additional fields sent on the push message.
	 * @return The message builder.
	 */
	private Message.Builder generateBuilder(String title, String message, boolean isSingle, Map<String, String> additionalFields) {
		String collapseKey = isSingle ? collapseKeySingle : collapseKeyBulk;
		Message.Builder msgBuilder = new Message.Builder().addData(messageKey, message).collapseKey(collapseKey);
		if (title != null) {
			msgBuilder.addData(titleKey, title);
		}
		if (additionalFields == null) {
			return msgBuilder;
		}
		for (Map.Entry<String, String> entry: additionalFields.entrySet()) {
			msgBuilder.addData(entry.getKey(), entry.getValue());
		}
		return msgBuilder;
	}
	
	/**
	 * Validates if a single push message request is valid.
	 * @param result The result retrieved from GCM.
	 * @return <i>true</i> if the result contains no errors.
	 */
	private boolean resultIsOk(final Result result){
		if (result.getErrorCodeName() == null || result.getErrorCodeName().isEmpty()) {
			return true;
		}
		log.error("Error occurred while sending push notification :" + result.getErrorCodeName());
		return false;
	}
	
	/**
	 * Validates if a multicast request is valid.
	 * @param result The result retrieved from GCM.
	 * @return <i>true</i> if the result contains no errors.
	 */
	private boolean resultIsOk(final MulticastResult result){
		boolean isOk = result.getResults() == null || result.getResults().isEmpty();
		if (result.getResults() != null)
			for (Result myResult : result.getResults())
				resultIsOk(myResult);
		return isOk;
	}
	
	/**
	 * Sets the number of max retries when sending a push message.
	 * @param maxRetries The number of max retries when sending a push message.
	 */
	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	/**
	 * Sets the message key that will store the key-value pair for the push message content.
	 * @param messageKey The message key that will store the key-value pair for the push message content.
	 */
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	/**
	 * Sets the title key that will store the key-value pair for the push message title.
	 * @param titleKey The title key that will store the key-value pair for the push message title.
	 */
	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	/**
	 * Sets the quantity of push messages to be sent simultaneously on multicast requests. 
	 * <b>NOTE:</b> As of December 2016, GCM states that there's a max limit of 1.000 simultaneous
	 * registration ids on multicast requests. See the <i>registration_ids</i> parameter <a href="https://developers.google.com/cloud-messaging/http-server-ref">here</a>.
	 * @param maxBulkSize The quantity of push messages to be sent simultaneously on multicast requests. Has a max value of 1000.
	 */
	public void setMaxBulkSize(int maxBulkSize) {
		this.maxBulkSize = maxBulkSize > Defaults.MAX_BULK_SIZE ? Defaults.MAX_BULK_SIZE : maxBulkSize;
	}

	/**
	 * Sets the collapse key for identifying single messages.
	 * @param collapseKeySingle The collapse key for identifying single messages.
	 */
	public void setCollapseKeySingle(String collapseKeySingle) {
		this.collapseKeySingle = collapseKeySingle;
	}

	/**
	 * Sets the collapse key for identifying bulk messages.
	 * @param collapseKeyBulk The collapse key for identifying bulk messages.
	 */
	public void setCollapseKeyBulk(String collapseKeyBulk) {
		this.collapseKeyBulk = collapseKeyBulk;
	}

	/**
	 * Sets the GCM API Key (also known as Sender ID).
	 * @param gcmApiKey The GCM API Key (also known as Sender ID).
	 */
	public void setGcmApiKey(String gcmApiKey) {
		this.senderService = new Sender(gcmApiKey);
	}

	/**
	 * Class default values.
	 */
	private static class Defaults {
		private static final int MAX_RETRIES = 3;
		private static final String MESSAGE_KEY = "message";
		private static final String TITLE_KEY = "title";
		private static final int MAX_BULK_SIZE = 1000; 
		
		private static final String COLLAPSE_KEY_SINGLE = "single";
		private static final String COLLAPSE_KEY_BULK = "bulk";
		
		private static final boolean PUSH_ENABLED = true;
	}
}
