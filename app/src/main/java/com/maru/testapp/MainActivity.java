package com.maru.testapp;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;


public class MainActivity extends Activity {

    private MediaRecorder mRecorder;

    private boolean mIsRecording;

    private String mFilePath;

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button recordButton = (Button) findViewById(R.id.recording_button);
        if (recordButton != null) {
            recordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mIsRecording) {
                        mRecorder.stop();
                        mRecorder.reset();
                        mIsRecording = false;
                        ((Button) view).setText(R.string.start_recording);
                    } else {
                        try {
                            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                            mRecorder.setOutputFile(mFilePath);
                            mRecorder.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mRecorder.start();
                        mIsRecording = true;
                        ((Button) view).setText(R.string.now_recording);
                    }
                }
            });
        }

        Button playButton = (Button) findViewById(R.id.play_button);
        if (playButton != null) {
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    File file = new File(mFilePath);
                    if (file != null && file.exists()) {
                        try {
                            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    view.setVisibility(View.VISIBLE);
                                    mediaPlayer.reset();
                                }
                            });
                            mMediaPlayer.setDataSource(mFilePath);
                            mMediaPlayer.prepare();
                            mMediaPlayer.start();
                            view.setVisibility(View.INVISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }


        mFilePath = Environment.getExternalStorageDirectory() + "/RecordedFile.3gp";
        mRecorder = new MediaRecorder();

        mMediaPlayer = new MediaPlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecorder.release();
        mMediaPlayer.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
