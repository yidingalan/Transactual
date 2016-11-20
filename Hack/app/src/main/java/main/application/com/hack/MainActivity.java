package main.application.com.hack;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView_explanation);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        Toast.makeText(this, "Purchase: $24.53", Toast.LENGTH_LONG).show();

        if (!mNfcAdapter.isEnabled()) {
            mTextView.setText("NFC is disabled.");
        } else {
            mTextView.setText("NFC enabled on phone");
        }

        takeScreenshot();
    }


    private void takeScreenshot() {
        Log.e("log", "screenshot taken");
        File dir = new File("/storage/0403-0201/Android/data/HackDir/");
        dir.mkdirs();
        try {
            // create bitmap screen capture
           /* View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap imageToSave = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);*/

            Bitmap bitmap = screenshot();
            if (bitmap == null){
                Log.e("Error", "cannot take screenshot");
            }

           // ScreentShotUtil.getInstance().takeScreenshot(getApplicationContext(), "/storage/0403-0201/Android/data/lab4_204_12.uwaterloo.ca.lab4_204_12/files");


          //  ImageReader mImageReader = ImageReader.newInstance(480, 360, PixelFormat.RGBA_8888, 2);
           // mVirtualDisplay = sMediaProjection.createVirtualDisplay(SCREENCAP_NAME, mWidth, mHeight, mDensity, VIRTUAL_DISPLAY_FLAGS, mImageReader.getSurface(), null, mHandler);
           // mImageReader.setOnImageAvailableListener(new ImageAvailableListener(), mHandler);

            //  saveBitmap(bitmap);

        } catch (Throwable e) {
            Log.e("error", "exception 2");
            e.printStackTrace();
        }
    }

    public void saveBitmap(Bitmap bitmap) {
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }
    public Bitmap screenshot() {

        ScreentShotUtil.getInstance().takeScreenshot(getBaseContext(), "/storage/0403-0201/Android/data/lab4_204_12.uwaterloo.ca.lab4_204_12/files");

        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    private void openScreenshot(File imageFile) {
        Log.e("log", "open screenshot");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
}
