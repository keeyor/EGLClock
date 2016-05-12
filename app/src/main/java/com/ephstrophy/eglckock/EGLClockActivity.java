/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package com.ephstrophy.eglckock;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EGLClockActivity extends Activity {
    /**
     * Hold a reference to our GLSurfaceView
     */
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;

    private EGLClockRenderer mRenderer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = new GLSurfaceView(this);

        // Check if the system supports OpenGL ES 2.0.
        ActivityManager activityManager = 
            (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager
            .getDeviceConfigurationInfo();
        // Even though the latest emulator supports OpenGL ES 2.0,
        // it has a bug where it doesn't set the reqGlEsVersion so
        // the above check doesn't work. The below will detect if the
        // app is running on an emulator, and assume that it supports
        // OpenGL ES 2.0.
        final boolean supportsEs2 =
            configurationInfo.reqGlEsVersion >= 0x20000
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 
                 && (Build.FINGERPRINT.startsWith("generic")
                  || Build.FINGERPRINT.startsWith("unknown")
                  || Build.MODEL.contains("google_sdk") 
                  || Build.MODEL.contains("Emulator")
                  || Build.MODEL.contains("Android SDK built for x86")));

        if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.
            glSurfaceView.setEGLContextClientVersion(2);            
            
            // Assign our renderer.
            mRenderer = new  EGLClockRenderer(this);
            glSurfaceView.setRenderer(new EGLClockRenderer(this));

         //  glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

            rendererSet = true;
        } else {
            /*
             * This is where you could create an OpenGL ES 1.x compatible
             * renderer if you wanted to support both ES 1 and ES 2. Since 
             * we're not doing anything, the app will crash if the device 
             * doesn't support OpenGL ES 2.0. If we publish on the market, we 
             * should also add the following to AndroidManifest.xml:
             * 
             * <uses-feature android:glEsVersion="0x00020000"
             * android:required="true" />
             * 
             * This hides our app from those devices which don't support OpenGL
             * ES 2.0.
             */
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.",
                Toast.LENGTH_LONG).show();
            return;
        }

        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        if (rendererSet) {
            glSurfaceView.onPause();
        }
        // MG: Pause TimeListener
        if(TimeChangeReceiver!=null)
         this.unregisterReceiver(TimeChangeReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //MG: Register TimeListener
         this.registerReceiver(TimeChangeReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
         if (rendererSet) {
            glSurfaceView.onResume();
        }
    }

    //MG: Time Listener Action
    private BroadcastReceiver TimeChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().compareTo(Intent.ACTION_TIME_TICK)==0)
            {

                float cHour = mRenderer.getHourAngle();
                float cMinutes = mRenderer.getMinAngle();
                float cSeconds = mRenderer.getSecAngle();

                Calendar calendar = new GregorianCalendar();
                Date trialTime = new Date();
                calendar.setTime(trialTime);
                long hour = calendar.get(Calendar.HOUR);
                long minute =  calendar.get(Calendar.MINUTE);
                long second = calendar.get(Calendar.SECOND);

                float mMinutes;
                float mHour;
                float mSeconds;
                mMinutes = minute + second / 60.0f;
                mHour = hour + mMinutes / 60.0f;
                mSeconds = second;

                if (rendererSet) {
                    mRenderer.setHourAngle(mHour - cHour);
                    mRenderer.setMinAngle(mMinutes - cMinutes);
                    mRenderer.setMinAngle(mSeconds - cSeconds);
                   glSurfaceView.requestRender();
                }
            }
        }
    };
}