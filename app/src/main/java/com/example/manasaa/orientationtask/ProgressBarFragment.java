package com.example.manasaa.orientationtask;

import android.app.Activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;


public class ProgressBarFragment extends android.app.Fragment {
    public static final String TAG_PROGRESSBAR_FRAGMENT = "progressbar_fragment";

    //Interface to interact with Activity
    public static interface TaskStatusCallback{
        void onPreExecute();
        void onProgressUpdate(int Progress);
        void onPostExecute();
        void onCancelled();

    }

    TaskStatusCallback mStatusCallBack;//Interface object
    BackgroundTask mBackgroundTask;//AsyncTask object
    boolean isTaskExecuting=false;//background process running or not

    @Override
    public void onAttach(Context context) {//Attaching the activity to fragment
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity){
            a=(Activity) context;
            mStatusCallBack=(TaskStatusCallback)a;//instantiating interface to activity
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);//save fragment instance across the activity
    }

    @Override
    public void onDetach() {//
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

    public void startBackgroundTask() {//starting it in background
        if (!isTaskExecuting){//Not started
            mBackgroundTask =new BackgroundTask();
            mBackgroundTask.execute();
            isTaskExecuting=true;
        }
    }

    public void cancelBackgroundTask(){//Stoping
        if (isTaskExecuting){//if started
            mBackgroundTask.cancel(true);
            isTaskExecuting=false;
        }
    }

    public void updateExecutingStatus(boolean isExecuting){
        this.isTaskExecuting=isExecuting;
    }
}
