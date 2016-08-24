package com.devsu.push.sender.service.async;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.devsu.push.sender.callback.PushCallback;
import com.devsu.push.sender.service.sync.SyncApplePushService;
import com.notnoop.apns.PayloadBuilder;
import com.notnoop.exceptions.InvalidSSLConfig;
import com.notnoop.exceptions.RuntimeIOException;

/**
 * The async push service for iOS (APNS).
 */
public class AsyncApplePushService extends AsyncPushServiceBase {
	
	private static final String BUILDER_OBJECT = PayloadBuilder.class.getSimpleName();

	/**
	 * @see com.devsu.push.sender.service.sync.SyncApplePushService#ApplePushService(String, String, boolean)
	 */
	public AsyncApplePushService(String certificatePath, String certificatePassword, boolean useProductionServer)
			throws RuntimeIOException, InvalidSSLConfig {
		super(new SyncApplePushService(certificatePath, certificatePassword, useProductionServer), null);
	}
	
	/**
	 * 4 param constructor.
	 * @param certificatePath The path for the p12 certificate.
	 * @param certificatePassword The password for the p12 certificate.
	 * @param useProductionServer Indicates if the services uses a Production environment or a Sandbox environment.
	 * @param pushCallback The push callback.
	 * @throws RuntimeIOException
	 * @throws InvalidSSLConfig
	 */
	public AsyncApplePushService(String certificatePath, String certificatePassword, boolean useProductionServer, PushCallback pushCallback) 
			throws RuntimeIOException, InvalidSSLConfig {
		super(new SyncApplePushService(certificatePath, certificatePassword, useProductionServer), pushCallback);
	}
	
	/**
	 * Sends a single push message.
	 * @param msgBuilder The PayloadBuilder object.
	 * @param token The push token.
	 * @return <i>true</i> if the push message request was sent. 
	 * @throws Exception
	 */
	public void sendPush(final PayloadBuilder msgBuilder, final String token) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new Runnable() {
		    @Override
			public void run() {
		    	try {
		    		SyncApplePushService service = (SyncApplePushService) pushService;
					boolean result = service.sendPush(msgBuilder, token);
					if (pushCallback != null) {
						pushCallback.onSingleSuccess(result, BUILDER_OBJECT, msgBuilder.toString(), null, token);
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
	 * @param token The push token.
	 * @return <i>true</i> if the push message request was sent. 
	 * @throws Exception
	 */
	public void sendPushInBulk(final PayloadBuilder msgBuilder, final String... tokens) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new Runnable() {
		    @Override
			public void run() {
		    	try {
		    		SyncApplePushService service = (SyncApplePushService) pushService;
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
	 * @see com.devsu.push.sender.service.sync.SyncApplePushService#setupDevelopmentServer(java.lang.String, java.lang.String)
	 */
	public void setupDevelopmentServer(String certificatePath, String certificatePassword) throws RuntimeIOException, InvalidSSLConfig {
		((SyncApplePushService)pushService).setupDevelopmentServer(certificatePath, certificatePassword);
	}
	
	/**
	 * @see com.devsu.push.sender.service.sync.SyncApplePushService#setupProductionServer(java.lang.String, java.lang.String)
	 */
	public void setupProductionServer(String certificatePath, String certificatePassword) throws RuntimeIOException, InvalidSSLConfig {
		((SyncApplePushService)pushService).setupProductionServer(certificatePath, certificatePassword);
	}
	
	/**
	 * @see com.devsu.push.sender.service.sync.SyncPushServiceBase#setPushEnabled(boolean)
	 */
	public void setPushEnabled(boolean pushEnabled) {
		((SyncApplePushService)pushService).setPushEnabled(pushEnabled);
	}
	
	public Map<String, Date> getInactiveDevices() {
		return ((SyncApplePushService)pushService).getInactiveDevices();
	}
}
