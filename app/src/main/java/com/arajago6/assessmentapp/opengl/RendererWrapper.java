package com.arajago6.assessmentapp.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView.Renderer;
import com.arajago6.assessmentapp.HomeActivity;

// Custom renderer for the C++ OpenGL calls
public class RendererWrapper implements Renderer {
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        HomeActivity.init(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        HomeActivity.step();
    }
}