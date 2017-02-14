package com.example.manasaa.orientationtask;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ProgressBarFragment.TaskStatusCallback{
    private static final String TAG=MainActivity.class.getSimpleName();
    private Button mStartButton,mStopButton,mActivity2;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private static final int MAX_VALUE=30;
    private Handler handler = new Handler();
    private ProgressBarFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mStartButton=(Button)findViewById(R.id.start_button);
        mStopButton=(Button)findViewById(R.id.stop_button);
        mActivity2=(Button)findViewById(R.id.act2_button);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(MAX_VALUE);
        mTextView = (TextView) findViewById(R.id.textView);
        mStopButton.setVisibility(View.INVISIBLE);

        if (savedInstanceState != null) {
            int progress = savedInstanceState.getInt("progress_value");
            mTextView.setText(progress + "%");
            mProgressBar.setProgress(progress);
        }

        mStartButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);
        mActivity2.setOnClickListener(this);

        FragmentManager fragmentManager=getFragmentManager();
        mFragment=(ProgressBarFragment)fragmentManager.findFragmentByTag(ProgressBarFragment.TAG_PROGRESSBAR_FRAGMENT);

        if(mFragment==null){
            mFragment =new ProgressBarFragment();
            fragmentManager.beginTransaction().add(mFragment,ProgressBarFragment.TAG_PROGRESSBAR_FRAGMENT).commit();
        }

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id) {
            case R.id.start_button:
                            mStartButton.setVisibility(View.INVISIBLE);
                            mStopButton.setVisibility(View.VISIBLE);
                            mFragment.startBackgroundTask();
                            break;
            case R.id.stop_button:
                            mStopButton.setVisibility(View.INVISIBLE);
                            mStartButton.setVisibility(View.VISIBLE);
                            mFragment.cancelBackgroundTask();
                             break;
            case R.id.act2_button:
                        Intent intent =new Intent(this,NoRotation.class);
                        startActivity(intent);
                        break;

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG,"*****BACKBUTTON PRESSED*****");
        mFragment.cancelBackgroundTask();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onProgressUpdate(int Progress) {
        mStartButton.setVisibility(View.INVISIBLE);
        mStopButton.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(Progress);
        mTextView.setText(Progress+"/"+mProgressBar.getMax());
    }

    @Override
    public void onPostExecute() {
        if (mFragment != null) {
            mStopButton.setVisibility(View.INVISIBLE);
            mStartButton.setVisibility(View.VISIBLE);
            mFragment.updateExecutingStatus(false);
        }
    }

    @Override
    public void onCancelled() {


    }

//    @Override
//    protected void onDestroy() {
//
//           // mFragment.cancelBackgroundTask();
//        Log.d(TAG,"CALLED OnDEstroy");
//        super.onDestroy();
//    }
}

//Static class
