package main.application.com.transactions;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PatternMatcher;
import android.util.Log;
import android.widget.Toast;

public class TakeScreenshotService extends IntentService {
    private static final String TAG = "NFCReadTag";
    private NfcAdapter mNfcAdapter;
    private IntentFilter[] mNdefExchangeFilters;
    private PendingIntent mNfcPendingIntent;


    public TakeScreenshotService(){
        super("Screenshot");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        try {
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

            mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                    getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);


            IntentFilter smartwhere = new IntentFilter (NfcAdapter.ACTION_NDEF_DISCOVERED);
            smartwhere.addDataScheme("http");
            smartwhere.addDataAuthority("www.smartwhere.com", null);
            smartwhere.addDataPath(".*", PatternMatcher.PATTERN_SIMPLE_GLOB);

            mNdefExchangeFilters = new IntentFilter[] { smartwhere };
           // Thread.sleep(5000); // wait 5 seconds simulate time until nfc signal
            //Log.e("HELLO!!!", "starting screenshot intent");
          //  Intent dialogIntent = new Intent(this, Screenshot.class);
           // dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(dialogIntent);
        } catch (Exception e) {
            Log.e("ERROR", "NFC Error");
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Service", "ServiceStarted!");
        return super.onStartCommand(intent,flags,startId);
    }

}
