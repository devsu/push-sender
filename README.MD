# Push Sender #

Push Sender is a Java library that allows sending push messages easily, based on the awesome libraries [gcm-server](https://github.com/theganyo/gcm-server) by [theganyo](https://github.com/theganyo) and (java-apns)[https://github.com/notnoop/java-apns] by (notnoop)[https://github.com/notnoop]. Send push messages with one line of code, forget about the rest, and focus on making on your application's business logic!

## Overview ###

Using Push Sender is very simple. You can import Push Sender on your POM file using:

```
	<dependency>
		<groupId>com.devsu</groupId>
		<artifactId>push-sender</artifactId>
		<version>1.0.0</version>
	</dependency>
```

## Synchronous and asynchronous push services ###

You can send your push messages synchronously and asynchronously by choosing any of the implementations of `PushService`.

```java
public class MySender {

	SyncAndroidPushService syncAndroidService = new SyncAndroidPushService(GCM_API_KEY);
	AsyncAndroidPushService asyncAndroidService = new SyncAndroidPushService(GCM_API_KEY, null);
	
	SyncApplePushService syncAppleService = new SyncApplePushService(P12_FILEPATH, P12_PASSWORD, IS_PRODUCTION_ENVIRONMENT);
	AsyncApplePushService asyncAppleService = new AsyncApplePushService(P12_FILEPATH, P12_PASSWORD, IS_PRODUCTION_ENVIRONMENT, null);
	
	...
	
	public void sendSomePushMessages() {
		String title = "My Push Message Title";
		String content = "Hello! This is a push message!";
		
		syncAndroidService.sendPush(title, content, ANDROID_PUSH_TOKEN);
		asyncAndroidService.sendPush(title, content, ANDROID_PUSH_TOKEN);
		
		syncAppleService.sendPush(title, content, IOS_PUSH_TOKEN);
		asyncAppleService.sendPush(title, content, IOS_PUSH_TOKEN);
	}

	...
}
```

## PushCallback ###

You can implement a PushCallback for any of the asynchronous services.

```java
public class MySender {

	...
		
	public void sendSomePushMessages() {
		AsyncAndroidPushService asyncAndroidService = new SyncAndroidPushService(GCM_API_KEY, new PushCallback() {
			
			@Override
			public void onSingleSuccess(boolean result, String title, String message, 
					Map<String, String> additionalFields, String token) {
				System.out.println("The single message was successfully sent!");
			}
			
			@Override
			public void onError(Throwable t) {
				System.err.println("Oops... Something happened!");
			}
			
			@Override
			public void onBulkSuccess(boolean result, String title, String message, 
					Map<String, String> additionalFields, String[] tokens) {
				System.out.println("The bulk message was successfully sent!");
			}
		});
	}

	...
}
```

## Authors ##
Feel free to contact Alvaro López at rion18@hotmail.com!

## License ###

Copyright 2015 Devsu Software

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.