package com.devsu.push.sender.service.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.devsu.push.sender.callback.PushCallback;
import com.devsu.push.sender.service.sync.SyncAndroidPushService;
import com.google.android.gcm.server.Message;

/**
 * The async push service for Android (GCM).
 */
public class AsyncAndroidPushService extends AsyncPushServiceBase {
	
	private static final String BUILDER_OBJECT = Message.Builder.class.getSimpleName();
	
	/**
	 * Single param constructor.
	 * @param gcmApiKey The GCM API Key (also known as Sender ID).
	 */
	public AsyncAndroidPushService(String gcmApiKey){
		super(new SyncAndroidPushService(gcmApiKey), null);
	}
	
	/**
	 * 2 param constructor.
	 * @param gcmApiKey The GCM API Key (also known as Sender ID).
	 * @param pushCallback The push callback.
	 */
	public AsyncAndroidPushService(String gcmApiKey, PushCallback pushCallback) {
		super(new SyncAndroidPushService(gcmApiKey), pushCallback);
	}
	
	/**
	 * Sends a single push message.
	 * @param msgBuilder The Message.Builder object.
	 * @param token The push token.
	 */
	public void sendPush(final Message.Builder msgBuilder, final String token) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new Runnable() {
		    @Override
			public void run() {
		    	try {
		    		SyncAndroidPushService service = (SyncAndroidPushService) pushService;
					boolean result = service.sendPush(msgBuilder, token);
					if (pushCallback != null) {
						pushCallback.onSingleSuccess(result, BUILDER_OBJECT, msgBuilder.build().toString(), null, token);
					}
		    	} catch (Exception e) {
		    		if (pushCallback != null) {
		    			pushCallback.onError(e);
		    		}
		    	}
		    }
		});
		executorService.shutdown();
	}
	
	/**
	 * Sends a bulk push message.
	 * @param msgBuilder The Message.Builder object.
	 * @param tokens The push tokens.
	 */
	public void sendPushInBulk(final Message.Builder msgBuilder, final String... tokens) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new Runnable() {
		    @Override
			public void run() {
		    	try {
		    		SyncAndroidPushService service = (SyncAndroidPushService) pushService;
					boolean result = service.sendPushInBulk(msgBuilder, tokens);
					if (pushCallback != null) {
						pushCallback.onBulkSuccess(result, BUILDER_OBJECT, msgBuilder.build().toString(), null, tokens);
					}
		    	} catch (Exception e) {
		    		if (pushCallback != null) {
		    			pushCallback.onError(e);
		    		}
		    	}
		    }
		});
		executorService.shutdown();
	}
	
	/**
	 * Sets the number of max retries when sending a push message.
	 * @param maxRetries The number of max retries when sending a push message.
	 */
	public void setMaxRetries(int maxRetries) {
		((SyncAndroidPushService)pushService).setMaxRetries(maxRetries);
	}

	/**
	 * Sets the message key that will store the key-value pair for the push message content.
	 * @param messageKey The message key that will store the key-value pair for the push message content.
	 */
	public void setMessageKey(String messageKey) {
		((SyncAndroidPushService)pushService).setMessageKey(messageKey);
	}

	/**
	 * Sets the title key that will store the key-value pair for the push message title.
	 * @param titleKey The title key that will store the key-value pair for the push message title.
	 */
	public void setTitleKey(String titleKey) {
		((SyncAndroidPushService)pushService).setTitleKey(titleKey);
	}

	/**
	 * Sets the quantity of push messages to be sent simultaneously on multicast requests. 
	 * <b>NOTE:</b> As of December 2016, GCM states that there's a max limit of 1.000 simultaneous
	 * registration ids on multicast requests. See the <i>registration_ids</i> parameter <a href="https://developers.google.com/cloud-messaging/http-server-ref">here</a>.
	 * @param maxBulkSize The quantity of push messages to be sent simultaneously on multicast requests. Has a max value of 1000.
	 */
	public void setMaxBulkSize(int maxBulkSize) {
		((SyncAndroidPushService)pushService).setMaxBulkSize(maxBulkSize);
	}

	/**
	 * Sets the collapse key for identifying single messages.
	 * @param collapseKeySingle The collapse key for identifying single messages.
	 */
	public void setCollapseKeySingle(String collapseKeySingle) {
		((SyncAndroidPushService)pushService).setCollapseKeySingle(collapseKeySingle);
	}
	
	/**
	 * Sets the collapse key for identifying bulk messages.
	 * @param collapseKeyBulk The collapse key for identifying bulk messages.
	 */
	public void setCollapseKeyBulk(String collapseKeyBulk) {
		((SyncAndroidPushService)pushService).setCollapseKeyBulk(collapseKeyBulk);
	}

	/**
	 * Sets the GCM API Key (also known as Sender ID).
	 * @param gcmApiKey The GCM API Key (also known as Sender ID).
	 */
	public void setGcmApiKey(String gcmApiKey) {
		((SyncAndroidPushService)pushService).setGcmApiKey(gcmApiKey);
	}
	
	/**
	 * Enables/disables this service.
	 * @param pushEnabled The parameter that enables/disables this service.
	 */
	public void setPushEnabled(boolean pushEnabled) {
		((SyncAndroidPushService)pushService).setPushEnabled(pushEnabled);
	}
}
