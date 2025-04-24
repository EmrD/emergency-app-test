package com.emr.testEmergency.services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.ContextCompat;

public class services{
    public void sendMessage(Context context, String number) {
        String message = "Merhaba, şu anda kötü bir durumdayım. Lütfen yardım isteyin. Sağlık bilgilerim:\n Sağlık bilgisi1,\n Sağlık bilgisi2"; // Örnek bir acil durum mesajı.
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + number));
        intent.putExtra("sms_body", message);

        ContextCompat.startActivity(context, intent, null);
    }

    public void makeCall(Context context,String number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        ContextCompat.startActivity(context, intent, null);
    }
}