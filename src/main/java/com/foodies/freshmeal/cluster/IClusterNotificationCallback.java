package com.foodies.freshmeal.cluster;

import com.foodies.freshmeal.cluster.message.IEventMessage;

public interface IClusterNotificationCallback {
	public void handleNotification(IEventMessage eventMessage);
}
