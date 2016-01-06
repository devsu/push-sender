package com.devsu.push.sender.service.async;

import com.devsu.push.sender.callback.PushCallback;
import com.devsu.push.sender.service.sync.SyncApplePushService;
import com.notnoop.exceptions.InvalidSSLConfig;
import com.notnoop.exceptions.RuntimeIOException;

/**
 * The async push service for iOS (APNS).
 */
public class AsyncApplePushService extends AsyncPushServiceBase {
	
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
}
