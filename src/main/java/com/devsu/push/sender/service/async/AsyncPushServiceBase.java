package com.devsu.push.sender.service.async;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.devsu.push.sender.callback.PushCallback;
import com.devsu.push.sender.service.sync.SyncPushService;

/**
 * Abstract base class for every async push service.
 */
public abstract class AsyncPushServiceBase implements AsyncPushService {
	
	/**
	 * The push service that will be used in this async service.
	 */
	protected SyncPushService pushService;
	
	/**
	 * The push service callback.
	 */
	protected PushCallback pushCallback;
	
	/**
	 * 2-param constructor.
	 * @param pushService The push service.
	 * @param pushCallback The push callback.
	 */
	protected AsyncPushServiceBase(SyncPushService pushService, PushCallback pushCallback){
		this.pushService = pushService;
		this.pushCallback = pushCallback;
	}
	
	/* 
	 * @see com.rion18.push.sender.service.async.AsyncPushService#sendPush(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendPush(String message, String token) {
		sendPush(null, message, null, token);
	}

	/* 
	 * @see com.rion18.push.sender.service.async.AsyncPushService#sendPush(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendPush(String title, String message, String token) {
		sendPush(title, message, null, token);
	}
	
	/*
	 * @see com.rion18.push.sender.service.async.AsyncPushService#sendPush(java.lang.String, java.lang.String, java.util.Map, java.lang.String)
	 */
	@Override
	public void sendPush(final String title, final String message, 
			final Map<String, String> additionalFields, final String token) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new Runnable() {
		    @Override
			public void run() {
		    	try {
					boolean result = pushService.sendPush(title, message, additionalFields, token);
					if (pushCallback != null) {
						pushCallback.onSingleSuccess(result, title, message, additionalFields, token);
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
	
	/* 
	 * @see com.rion18.push.sender.service.async.AsyncPushService#sendPushInBulk(java.lang.String, java.lang.String[])
	 */
	@Override
	public void sendPushInBulk(String message, String... tokens) {
		sendPushInBulk(null, message, null, tokens);
	}
	
	/*
	 * @see com.rion18.push.sender.service.async.AsyncPushService#sendPushInBulk(java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Override
	public void sendPushInBulk(String title, String message, String... tokens) {
		sendPushInBulk(title, message, null, tokens);
	}
	
	/*
	 * @see com.rion18.push.sender.service.async.AsyncPushService#sendPushInBulk(java.lang.String, java.lang.String, java.util.Map, java.lang.String[])
	 */
	@Override
	public void sendPushInBulk(final String title, final String message, 
			final Map<String, String> additionalFields, final String... tokens) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new Runnable() {
		    @Override
			public void run() {
		    	try {
					boolean result = pushService.sendPushInBulk(title, message, additionalFields, tokens);
					if (pushCallback != null) {
						pushCallback.onBulkSuccess(result, title, message, additionalFields, tokens);
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
	
	/*
	 * @see com.rion18.push.sender.service.async.AsyncPushService#setPushCallback(com.rion18.push.sender.callback.PushCallback)
	 */
	@Override
	public void setPushCallback(PushCallback pushCallback) {
		this.pushCallback = pushCallback;
	}
}
