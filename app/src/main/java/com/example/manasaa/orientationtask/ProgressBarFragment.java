package com.example.manasaa.orientationtask;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;

public class ProgressBarFragment extends android.app.Fragment {
    public static final String TAG_PROGRESSBAR_FRAGMENT = "progressbar_fragment";

    public static interface TaskStatusCallback{
        void onPreExecute();
        void onProgressUpdate(int Progress);
        void onPostExecute();
        void onCancelled();

    }

    TaskStatusCallback mStatusCallBack;
    BackgroundTask mBackgroundTask;
    boolean isTaskExecuting=false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mStatusCallBack=(TaskStatusCallback)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mStatusCallBack=null;
    }

    private class BackgroundTask extends AsyncTask<Void,Integer,Void>{
        @Override
        protected void onPreExecute() {
            if(mStatusCallBack!=null){
                mStatusCallBack.onPreExecute();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            int progress =0;
            while (progress<30 && !isCancelled() ){
                progress++;
                publishProgress(progress);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(mStatusCallBack!=null){
                mStatusCallBack.onPostExecute();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if(mStatusCallBack!=null){
                mStatusCallBack.onProgressUpdate(values[0]);
            }
        }

        @Override
        protected void onCancelled() {
            if (mStatusCallBack!=null){
                mStatusCallBack.onCancelled();
            }
        }
    }

    public void startBackgroundTask() {
        if (!isTaskExecuting){
            mBackgroundTask =new BackgroundTask();
            mBackgroundTask.execute();
            isTaskExecuting=true;
        }
    }

    public void cancelBackgroundTask(){
        if (isTaskExecuting){
            mBackgroundTask.cancel(true);
            isTaskExecuting=false;
        }
    }
    public void updateExecutingStatus(boolean isExecuting){
        this.isTaskExecuting=isExecuting;
    }
}
