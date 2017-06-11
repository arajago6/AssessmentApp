package com.arajago6.assessmentapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.arajago6.assessmentapp.opengl.OpenGLRenderer;
import com.arajago6.assessmentapp.opengl.OpenGLSurfaceView;

public class OpenGLCubeActivity extends AppCompatActivity {

    private OpenGLSurfaceView oGLSurfaceView;
    private OpenGLRenderer oGLRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mIntent = getIntent();
        final int cubeCount = mIntent.getIntExtra("baseCubeCount", 0);
        Toast.makeText(getApplicationContext(), Integer.toString(cubeCount), Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_open_gl_cube);
        oGLSurfaceView = (OpenGLSurfaceView) findViewById(R.id.open_gl_surface_view);

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.
            oGLSurfaceView.setEGLContextClientVersion(2);

            final DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            // Set the renderer to our demo renderer, defined below.
            oGLRenderer = new OpenGLRenderer(this, oGLSurfaceView, cubeCount);
            oGLSurfaceView.setRenderer(oGLRenderer, displayMetrics.density);

        } else {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            return;
        }
    }

    @Override
    protected void onResume() {
        // The activity must call the GL surface view's onResume() on activity
        // onResume().
        super.onResume();
        oGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        // The activity must call the GL surface view's onPause() on activity
        // onPause().
        super.onPause();
        oGLSurfaceView.onPause();
    }
}
