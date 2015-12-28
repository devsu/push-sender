package com.devsu.push.sender.service.sync;

import org.slf4j.Logger;

public abstract class SyncPushServiceBase implements SyncPushService {

	/**
	 * Flag that enables/disables the service.
	 */
	protected boolean pushEnabled;

	/**
	 * Validates the Data for a single push message.
	 * @param log The logger
	 * @param message The push message content.
	 * @param token The push token.
	 * @return <i>true</i> if a message or token are null.
	 */
	public boolean validateSingleData(Logger log, String message, String token) {
		if (message == null) {
			log.error("Message is null.");
			return false;
		}
		if (token == null) {
			log.error("Token is null.");
			return false;
		}
		if (!pushEnabled) {
			log.debug("PUSH MOCK - " + token + ": " + message);
			return false;
		}
		return true;
	}
	
	/**
	 * Validates the Data for a bulk push message.
	 * @param log The logger
	 * @param message The push message content.
	 * @return <i>true</i> if a message is null.
	 */
	public boolean validateBulkData(Logger log, String message) {
		if (message == null) {
			log.error("Message is null.");
			return false;
		}
		if (!pushEnabled) {
    		log.debug("BULK PUSH MOCK - " + message);
			return false;
		}
		return true;
	}

	/*
	 * @see com.rion18.pusher.service.sync.PushService#sendPush(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendPush(String message, String token) throws Exception{
		return sendPush(null, message, null, token);
	}
	
	/*
	 * @see com.rion18.pusher.service.sync.PushService#sendPush(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendPush(String title, String message, String token) throws Exception {
		return sendPush(title, message, null, token);
	}
	
	/*
	 * @see com.rion18.pusher.service.sync.PushService#sendPushInBulk(java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean sendPushInBulk(String message, String... tokens) throws Exception {
		return sendPushInBulk(null, message, null, tokens);
	}
	
	/*
	 * @see com.rion18.pusher.service.sync.PushService#sendPushInBulk(java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean sendPushInBulk(String title, String message, String... tokens) throws Exception {
		return sendPushInBulk(title, message, null, tokens);
	};
	
	/**
	 * Enables/disables this service.
	 * @param pushEnabled The parameter that enables/disables this service.
	 */
	public void setPushEnabled(boolean pushEnabled) {
		this.pushEnabled = pushEnabled;
	}
}
