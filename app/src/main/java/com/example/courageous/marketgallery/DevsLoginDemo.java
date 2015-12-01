package com.example.courageous.marketgallery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class DevsLoginDemo extends Activity {

    VideoView videoView;
    ProgressDialog progressDialog;
    MediaController mediaControls;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devs_login_demo);

        // Asking the phone to keep the screen on while playing the video.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Sets the media controller buttons.
        if (mediaControls == null) {
            mediaControls = new MediaController(DevsLoginDemo.this);
        }
        // Initializes the VideoView.
        videoView = (VideoView) findViewById(R.id.video_view);

        // Creates a progress bar while the video file is loading.
        progressDialog = new ProgressDialog(DevsLoginDemo.this);
        // Sets a title for the progress bar.
        progressDialog.setTitle("Demo Video");
        // Sets a message for the progress bar.
        progressDialog.setMessage("Loading...");
        // Sets the progress bar not cancelable on users' touch.
        progressDialog.setCancelable(false);
        // Shows the progress bar.
        progressDialog.show();

        try {
            // Sets the media controller in the VideoView.
            videoView.setMediaController(mediaControls);
            // Sets the uri of the video to be played.
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.demo));
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();
        // We also set an setOnPreparedListener in order to know when the video file is ready for playback
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                // Closes the progress bar and plays the video.
                progressDialog.dismiss();

                // If we have a position on savedInstanceState, the video playback should start from here.
                videoView.seekTo(position);
                if (position == 0) {
                    videoView.pause();
                } else {
                    // If we come from a resumed activity, video playback will be paused.
                    videoView.pause();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Pauses video playback if user would like to come back to it at a later time.
        videoView.pause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // We use onSaveInstanceState in order to store the video playback position for orientation change.
        savedInstanceState.putInt("Position", videoView.getCurrentPosition());
        videoView.pause();
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // We use onRestoreInstanceState in order to play the video playback from the stored position.
        position = savedInstanceState.getInt("Position");
        videoView.seekTo(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_devs_login_demo, menu);
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
