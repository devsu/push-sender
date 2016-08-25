package com.devsu.push.sender.service.sync;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;
import com.notnoop.exceptions.InvalidSSLConfig;
import com.notnoop.exceptions.RuntimeIOException;


public class SyncApplePushService extends SyncPushServiceBase {

	private Map<String, Date> inactiveDevices;
	
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(getClass()); 

	/**
	 * The APNS service.
	 */
	private ApnsService apnsService;
	
	/**
	 * 3 param constructor.
	 * @param certificatePath The path of the p12 certificate file.
	 * @param certificatePassword The password for the p12 certificate.
	 * @param useProductionServer Indicates if the services uses a Production environment or a Sandbox environment.
	 * @throws RuntimeIOException An IO exception.
	 * @throws InvalidSSLConfig Certificates are corrupted, wrong or password is wrong.
	 */
	public SyncApplePushService(String certificatePath, String certificatePassword, boolean useProductionServer) 
			throws RuntimeIOException, InvalidSSLConfig {
		setDefaultValues();
		if (useProductionServer) {
			setupProductionServer(certificatePath, certificatePassword);
		} 
		else {
			setupDevelopmentServer(certificatePath, certificatePassword);
		}
	}
	
	/**
	 * Sets the default values.
	 */
	private void setDefaultValues() {
		pushEnabled = Defaults.PUSH_ENABLED;
		inactiveDevices = new HashMap<String, Date>();
	}
	
	/*
	 * @see com.devsu.push.sender.service.sync.SyncPushService#sendPush(java.lang.String, java.lang.String, java.util.Map, java.lang.String)
	 */
	@Override
	public boolean sendPush(String title, String message, Map<String, String> additionalFields, String token) throws Exception {
		if (!validateSingleData(log, message, token)) {
			return false;
		}
		apnsService.start();
		PayloadBuilder msgBuilder = generateBuilder(title, message, additionalFields);
		apnsService.push(token, msgBuilder.build());
		apnsService.stop();
		return true;
	}
	
	/**
	 * Sends a single push message.
	 * @param msgBuilder The PayloadBuilder object.
	 * @param token The push token.
	 * @return <i>true</i> if the push message request was sent. 
	 * @throws Exception Any exception that may arise.
	 */
	public boolean sendPush(PayloadBuilder msgBuilder, String token) throws Exception {
		if (!validateToken(log, token)) {
			return false;
		}
		apnsService.start();
		apnsService.push(token, msgBuilder.build());
		apnsService.stop();
		return true;
	}
	
	/*
	 * @see com.devsu.push.sender.service.sync.SyncPushService#sendPushInBulk(java.lang.String, java.lang.String, java.util.Map, java.lang.String[])
	 */
	@Override
	public boolean sendPushInBulk(String title, String message, 
			Map<String, String> additionalFields, String... tokens) {
		if (!validateBulkData(log, message)) {
			return false;
		}
		apnsService.start();
		PayloadBuilder msgBuilder = generateBuilder(title, message, additionalFields);
		apnsService.push(Arrays.asList(tokens), msgBuilder.build());
		
		inactiveDevices.putAll(apnsService.getInactiveDevices());

		apnsService.stop(); 
		return true;
	}
	
	/**
	 * Sends a bulk push message.
	 * @param msgBuilder The PayloadBuilder object.
	 * @param tokens The push token.
	 * @return <i>true</i> if the push message request was sent. 
	 * @throws Exception Any exception that may arise.
	 */
	public boolean sendPushInBulk(PayloadBuilder msgBuilder, String... tokens) throws Exception {
		apnsService.start();
		apnsService.push(Arrays.asList(tokens), msgBuilder.build());
		inactiveDevices.putAll(apnsService.getInactiveDevices());
		apnsService.stop(); 
		return true;
	}
	
	/**
	 * Default payload builder generator.
	 * @param title The push message title.
	 * @param message The push message content.
	 * @param additionalFields The additional fields sent on the push message.
	 * @return The payload builder.
	 */
	private PayloadBuilder generateBuilder(String title, String message, Map<String, String> additionalFields) {
		PayloadBuilder msgBuilder = APNS.newPayload().alertBody(message).sound(Defaults.SOUND);
		if (title != null) {
			msgBuilder.alertTitle(title);
		}
		if (additionalFields == null) {
			return msgBuilder;
		}
		msgBuilder.customFields(additionalFields);
		return msgBuilder;
	}
	
	/**
	 * Sets up the APNS Sandbox environment.
	 * @param certificatePath The path of the p12 certificate file.
	 * @param certificatePassword The password for the p12 certificate.
	 * @throws RuntimeIOException An IO exception.
	 * @throws InvalidSSLConfig Certificates are corrupted, wrong or password is wrong.
	 */
	public void setupDevelopmentServer(String certificatePath, String certificatePassword) throws RuntimeIOException, InvalidSSLConfig {
		apnsService = APNS.newService().withCert(certificatePath, certificatePassword).withSandboxDestination().build();
		apnsService.stop();
	}
	
	/**
	 * Sets up the APNS Production environment.
	 * @param certificatePath The path of the p12 certificate file.
	 * @param certificatePassword The password for the p12 certificate.
	 * @throws RuntimeIOException An IO exception.
	 * @throws InvalidSSLConfig Certificates are corrupted, wrong or password is wrong.
	 */
	public void setupProductionServer(String certificatePath, String certificatePassword) throws RuntimeIOException, InvalidSSLConfig {
		apnsService = APNS.newService().withCert(certificatePath, certificatePassword).withProductionDestination().build();
		apnsService.stop();
	}
	
	public Map<String, Date> getInactiveDevices() {
		return Collections.unmodifiableMap(inactiveDevices);
	}
	
	/**
	 * Class default values.
	 */
	private static class Defaults {
		private static final boolean PUSH_ENABLED = true;
		private static final String SOUND = "default";
	}
}
