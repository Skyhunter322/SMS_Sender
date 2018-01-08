package trainedge.contact_demo;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMSActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        final EditText etText = findViewById(R.id.etText);
        final EditText etContact = findViewById(R.id.etContact);
        btnSend = findViewById(R.id.btnSend);
        if (getIntent() != null) {
            String number = getIntent().getStringExtra("number");
            etContact.setText(number);
        }
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = etContact.getText().toString();
                String text = etText.getText().toString();
                if (contact.isEmpty() || contact.length() < 10
                        || contact.length() > 13) {
                    etContact.setError("Contact number is invalid");
                    return;
                }
                if (text.isEmpty() || text.length() > 160) {
                    etText.setError("Message can't be empty or above 160 characters");
                    return;
                }
                sendSMS(contact, text);

            }
        });

        checkForSmsPermission();

    }

    private void sendSMS(String contact, String text) {
        String scAddress = null;
        PendingIntent sentIntent = null, deliveryIntent = null;
        SmsManager smsManager = SmsManager.getDefault();
        /* smsManager.sendTextMessage(contact, scAddress, text,
                sentIntent, deliveryIntent);*/
        smsManager.sendTextMessage(contact, scAddress, text,
                sentIntent, deliveryIntent);
    }

    private void disableSmsButton() {
        Toast.makeText(this, "SMS usage disabled, goto app setting to manage it. ", Toast.LENGTH_LONG).show();
        btnSend.setEnabled(false);
    }

    private void enableSmsButton() {
        btnSend.setEnabled(true);
    }

    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            /* ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.SEND_SMS
                    },
                    MY_PERMISSIONS_REQUEST_SEND_SMS);*/
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.SEND_SMS
                    },
                    MY_PERMISSIONS_REQUEST_SEND_SMS);

        } else {
            enableSmsButton();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase
                        (Manifest.permission.SEND_SMS)
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    enableSmsButton();
                } else {
                    Toast.makeText(this, "You are unworthy",
                            Toast.LENGTH_SHORT).show();
                    disableSmsButton();
                }
            }
        }
    }

}


//built in intent