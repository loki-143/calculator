package com.example.smsmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Patterns;
import java.util.regex.Pattern;

public class SMSReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final Pattern ALERT_PATTERN = Pattern.compile("ALERT:\\s.*"); // Customize this pattern

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String messageBody = messages[i].getMessageBody();
                    if (ALERT_PATTERN.matcher(messageBody).matches()) {
                        NotificationHelper.showNotification(context, 
                            "Alert SMS Received", 
                            messageBody);
                    }
                }
            }
        }
    }
}
