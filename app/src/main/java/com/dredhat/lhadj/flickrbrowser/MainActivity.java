package com.dredhat.lhadj.flickrbrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends AppCompatActivity implements GetRawData.OnDownlaodcomplete {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
//        Log.i(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GetRawData getRawData = new GetRawData(this);
        getRawData.execute("https://api.flickr.com/services/feeds/photos_public.gne?tags=android,nougat&format=json&nojsoncallback=1"   );

        Log.d(TAG, "onCreate: ends");
//        Log.i(TAG, "onCreate: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDownloadComplete(String data,DownloadStatus status){
        if(status==DownloadStatus.OK){
            Log.i(TAG, "onDownloadComplete: data is "+data);
        }else{
            Log.e(TAG, "onDownloadComplete: faild with status"+status );
        }

    }

}
