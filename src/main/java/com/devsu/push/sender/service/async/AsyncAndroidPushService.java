package com.devsu.push.sender.service.async;

import com.devsu.push.sender.callback.PushCallback;
import com.devsu.push.sender.service.sync.SyncAndroidPushService;

/**
 * The async push service for Android (GCM).
 */
public class AsyncAndroidPushService extends AsyncPushServiceBase {
	
	/**
	 * @see com.devsu.push.sender.service.sync.SyncAndroidPushService#AndroidPushService(String)
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
	 * @see com.devsu.push.sender.service.sync.SyncAndroidPushService#setMaxRetries(int)
	 */
	public void setMaxRetries(int maxRetries) {
		((SyncAndroidPushService)pushService).setMaxRetries(maxRetries);
	}

	/**
	 * @see com.devsu.push.sender.service.sync.SyncAndroidPushService#setMessageKey(java.lang.String)
	 */
	public void setMessageKey(String messageKey) {
		((SyncAndroidPushService)pushService).setMessageKey(messageKey);
	}

	/**
	 * @see com.devsu.push.sender.service.sync.SyncAndroidPushService#setTitleKey(java.lang.String)
	 */
	public void setTitleKey(String titleKey) {
		((SyncAndroidPushService)pushService).setTitleKey(titleKey);
	}

	/**
	 * @see com.devsu.push.sender.service.sync.SyncAndroidPushService#setMaxBulkSize(int)
	 */
	public void setMaxBulkSize(int maxBulkSize) {
		((SyncAndroidPushService)pushService).setMaxBulkSize(maxBulkSize);
	}

	/**
	 * @see com.devsu.push.sender.service.sync.SyncAndroidPushService#setCollapseKeySingle(java.lang.String)
	 */
	public void setCollapseKeySingle(String collapseKeySingle) {
		((SyncAndroidPushService)pushService).setCollapseKeySingle(collapseKeySingle);
	}
	
	/**	 
	 * @see com.devsu.push.sender.service.sync.SyncAndroidPushService#setCollapseKeyBulk(java.lang.String)
	 */
	public void setCollapseKeyBulk(String collapseKeyBulk) {
		((SyncAndroidPushService)pushService).setCollapseKeyBulk(collapseKeyBulk);
	}

	/**
	 * @see com.devsu.push.sender.service.sync.SyncAndroidPushService#setGcmApiKey(java.lang.String)
	 */
	public void setGcmApiKey(String gcmApiKey) {
		((SyncAndroidPushService)pushService).setGcmApiKey(gcmApiKey);
	}
	
	/**
	 * @see com.devsu.push.sender.service.sync.SyncPushServiceBase#setPushEnabled(boolean)
	 */
	public void setPushEnabled(boolean pushEnabled) {
		((SyncAndroidPushService)pushService).setPushEnabled(pushEnabled);
	}
}
