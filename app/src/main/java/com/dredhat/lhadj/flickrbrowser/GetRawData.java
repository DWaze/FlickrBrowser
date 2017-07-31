package com.dredhat.lhadj.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lhadj on 7/30/2017.
 */
enum DownloadStatus{ IDLE, PROCESSING, NOT_INITIALISED,FAILS_OR_EMPTY,OK}

public class GetRawData extends AsyncTask<String,Void,String> {
    private static final String TAG = "GetRawData";
    private final OnDownlaodcomplete mCallback;

    interface OnDownlaodcomplete{
        void onDownloadComplete(String data,DownloadStatus status);
    }

    private DownloadStatus mDownloadStatus;

    public GetRawData(OnDownlaodcomplete callback) {
        this.mDownloadStatus = DownloadStatus.IDLE;
        mCallback =callback;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i(TAG, "onPostExecute: parameters =:"+s);
        if(mCallback != null){
            mCallback.onDownloadComplete(s,mDownloadStatus);
        }
        Log.i(TAG, "onPostExecute: ends");
    }
    
    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection connection= null;
        BufferedReader reader = null;

        if(strings == null){
            mDownloadStatus= DownloadStatus.NOT_INITIALISED;
            return null;
        }

        try{
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);

            connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.i(TAG, "doInBackground: The Response code"+response);

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

//            String line ;
//            while(null != (line= reader.readLine())){
            for(String line=reader.readLine();line!=null;line=reader.readLine()){
                result.append(line).append("\n");
            }

            mDownloadStatus  = DownloadStatus.OK;
            return result.toString();


        } catch (MalformedURLException e){
            Log.e(TAG, "doInBackground: Invalid URL"+ e.getMessage());
        } catch (IOException e){
            Log.e(TAG, "doInBackground: IO Exception Reading data"+e.getMessage() );
        }catch (SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception. Need permission?"+e.getMessage());
        }finally {
            if(connection!=null){
                connection.disconnect();
            }

            if(reader!=null){
                try{
                    reader.close();
                }catch (IOException e){
                    Log.e(TAG, "doInBackground: Error closing stream "+e.getMessage());
                }
            }
        }
        mDownloadStatus=DownloadStatus.FAILS_OR_EMPTY;
        return null;
    }



}
