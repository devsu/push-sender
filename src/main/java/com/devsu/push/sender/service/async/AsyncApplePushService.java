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
	 * 3 param constructor.
	 * @param certificatePath The path of the p12 certificate file.
	 * @param certificatePassword The password for the p12 certificate.
	 * @param useProductionServer Indicates if the services uses a Production environment or a Sandbox environment.
	 * @throws RuntimeIOException An IO exception.
	 * @throws InvalidSSLConfig Certificates are corrupted, wrong or password is wrong.
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
	 * @throws RuntimeIOException An IO exception.
	 * @throws InvalidSSLConfig Certificates are corrupted, wrong or password is wrong.
	 */
	public AsyncApplePushService(String certificatePath, String certificatePassword, boolean useProductionServer, PushCallback pushCallback) 
			throws RuntimeIOException, InvalidSSLConfig {
		super(new SyncApplePushService(certificatePath, certificatePassword, useProductionServer), pushCallback);
	}
	
	/**
	 * Sends a single push message.
	 * @param msgBuilder The PayloadBuilder object.
	 * @param token The push token.
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
	 * @param tokens The push token.
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
	 * Sets up the APNS Sandbox environment.
	 * @param certificatePath The path of the p12 certificate file.
	 * @param certificatePassword The password for the p12 certificate.
	 * @throws RuntimeIOException An IO exception.
	 * @throws InvalidSSLConfig Certificates are corrupted, wrong or password is wrong.
	 */
	public void setupDevelopmentServer(String certificatePath, String certificatePassword) throws RuntimeIOException, InvalidSSLConfig {
		((SyncApplePushService)pushService).setupDevelopmentServer(certificatePath, certificatePassword);
	}
	
	/**
	 * Sets up the APNS Production environment.
	 * @param certificatePath The path of the p12 certificate file.
	 * @param certificatePassword The password for the p12 certificate.
	 * @throws RuntimeIOException An IO exception.
	 * @throws InvalidSSLConfig Certificates are corrupted, wrong or password is wrong.
	 */
	public void setupProductionServer(String certificatePath, String certificatePassword) throws RuntimeIOException, InvalidSSLConfig {
		((SyncApplePushService)pushService).setupProductionServer(certificatePath, certificatePassword);
	}
	
	/**
	 * Enables/disables this service.
	 * @param pushEnabled The parameter that enables/disables this service.
	 */
	public void setPushEnabled(boolean pushEnabled) {
		((SyncApplePushService)pushService).setPushEnabled(pushEnabled);
	}
	
	/**
	 * Gets a map of inactive devices.
	 * @return a map of inactive devices.
	 */
	public Map<String, Date> getInactiveDevices() {
		return ((SyncApplePushService)pushService).getInactiveDevices();
	}
}
