/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package com.ephstrophy.eglckock;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.ephstrophy.eglckock.objects.Clock;
import com.ephstrophy.eglckock.objects.Mallet;
import com.ephstrophy.eglckock.objects.Mpointer;
import com.ephstrophy.eglckock.objects.Pointer;
import com.ephstrophy.eglckock.objects.Puck;
import com.ephstrophy.eglckock.objects.Spointer;
import com.ephstrophy.eglckock.objects.Table;
import com.ephstrophy.eglckock.programs.ColorShaderProgram;
import com.ephstrophy.eglckock.programs.TextureShaderProgram;
import com.ephstrophy.eglckock.util.MatrixHelper;
import com.ephstrophy.eglckock.util.TextureHelper;

public class EGLClockRenderer implements Renderer {

    private final Context context;
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] modelHourViewProjectionMatrix = new float[16];
    private final float[] modelMinutesViewProjectionMatrix = new float[16];
    private final float[] modelSecondViewProjectionMatrix = new float[16];

    private Clock clock;
    private Pointer hPointer;
    private Mpointer mPointer;
    private Spointer sPointer;
    private Table table;

    private Puck puck;

    private Mallet mallet;
    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
    private int texture;

    private float hourAngle;
    private float minAngle;



    private float secAngle;

    public EGLClockRenderer(Context context) {
        this.context = context;
    }


    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        table = new Table();

        clock = new Clock();

        hPointer = new Pointer();
        mPointer = new Mpointer();
        sPointer = new Spointer();

        mallet = new Mallet(0.08f, 0.15f, 32);
        puck = new Puck(0.06f, 0.02f, 32);


        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);

        Calendar calendar = new GregorianCalendar();
        Date trialTime = new Date();
        calendar.setTime(trialTime);
        long hour = calendar.get(Calendar.HOUR);
        long minute =  calendar.get(Calendar.MINUTE);
        long second = calendar.get(Calendar.SECOND);

        float mMinutes;
        float mHour;
        float mSecond;
        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;
        mSecond = second;
        setHourAngle(mHour);
        setMinAngle(mMinutes);
        setSecAngle(mSecond);
    }

    /**
     * onSurfaceChanged is called whenever the surface has changed. This is
     * called at least once when the surface is initialized. Keep in mind that
     * Android normally restarts an Activity on rotation, and in that case, the
     * renderer will be destroyed and a new one created.
     * 
     * @param width
     *            The new width, in pixels.
     * @param height
     *            The new height, in pixels.
     */
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix, 40, (float) width
                / (float) height, 1f, 10f);

       setLookAtM(viewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
       // setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);
    }

    /**
     * OnDrawFrame is called whenever a new frame needs to be drawn. Normally,
     * this is done at the refresh rate of the screen.
     */
    @Override
    public void onDrawFrame(GL10 glUnused) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

       Calendar calendar = new GregorianCalendar();
        Date trialTime = new Date();
        calendar.setTime(trialTime);
        long hour = calendar.get(Calendar.HOUR);
        long minute =  calendar.get(Calendar.MINUTE);
        long second = calendar.get(Calendar.SECOND);

        float mMinutes;
        float mHour;
        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;



        //positionTableInScene();
        //textureProgram.useProgram();
        //textureProgram.setUniforms(modelViewProjectionMatrix, texture);
        //table.bindData(textureProgram);
        //table.draw();

        // Draw the mallets.
       // positionObjectInScene(0f, mallet.height / 2f, -0.4f);
        //colorProgram.useProgram();
        //colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f);
       // mallet.bindData(colorProgram);
       //  mallet.draw();


        //positionObjectInScene(0f, mallet.height / 2f, 0.4f);
       // colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f);
        // Note that we don't have to define the object data twice -- we just
        // draw the same mallet again but in a different position and with a
        // different color.
        //mallet.draw();

        // Draw the puck.
       //positionObjectInScene(0f, puck.height / 2f, 0f);
       // colorProgram.setUniforms(viewProjectionMatrix, 0.8f, 0.8f, 1f);
       // puck.bindData(colorProgram);
       // puck.draw();

        // Draw the Clock
        //textureProgram.setUniforms(modelViewProjectionMatrix, texture);
        //clock.bindData(textureProgram);
        //clock.draw();
        colorProgram.useProgram();
        colorProgram.setUniforms(viewProjectionMatrix, 1f, 1f, 1f);
        clock.bindData(colorProgram);
        clock.draw();

        float hTransform =  getHourAngle() / 12.0f * 360.0f;

        positionHourPointerInScene(hTransform);
        //textureProgram.useProgram();
        //textureProgram.setUniforms(modelHourViewProjectionMatrix, texture);
        colorProgram.useProgram();
        colorProgram.setUniforms(modelHourViewProjectionMatrix, 0f, 1f, 0f);
        hPointer.bindData(colorProgram);
        hPointer.draw();

        float mTransform =  getMinAngle() / 60.0f * 360.0f;

        positionMinutePointerInScene(mTransform);
        colorProgram.setUniforms(modelMinutesViewProjectionMatrix,1f, 0f, 0f);
        mPointer.bindData(colorProgram);
        mPointer.draw();

        float sTransform =  getSecAngle() / 60.0f * 360.0f;

        // positionSecondPointerInScene(sTransform);
         //colorProgram.setUniforms(modelSecondViewProjectionMatrix,0f, 1f, 1f);
        // sPointer.bindData(colorProgram);
        // sPointer.draw();

         setHourAngle(mHour);
         setMinAngle(mMinutes);
         setSecAngle(second);
    }

    private void positionTableInScene() {
    // The table is defined in terms of X & Y coordinates, so we rotate it
    // 90 degrees to lie flat on the XZ plane.
        setIdentityM(modelMatrix, 0);
        rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
    }

   private void positionHourPointerInScene(float hTransform) {
    // The table is defined in terms of X & Y coordinates, so we rotate it
    // 90 degrees to lie flat on the XZ plane.

       setIdentityM(modelMatrix, 0);
        rotateM(modelMatrix, 0,  hTransform, 0.0f, 0.0f, -1f);
        multiplyMM(modelHourViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
    }
    private void positionMinutePointerInScene(float mTransform) {
        // The table is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        setIdentityM(modelMatrix, 0);
        rotateM(modelMatrix, 0, mTransform, 0.0f, 0.0f, -1f);
        multiplyMM(modelMinutesViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
    }
    private void positionSecondPointerInScene(float sTransform) {
        // The table is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        setIdentityM(modelMatrix, 0);
        rotateM(modelMatrix, 0, sTransform, 0.0f, 0.0f, -1f);
        multiplyMM(modelSecondViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
    }
    public float getHourAngle() {
        return hourAngle;
    }

    private void positionObjectInScene(float x, float y, float z) {
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, x, y, z);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
    }

    public void setHourAngle(float hourAngle) {
        this.hourAngle = hourAngle;
    }
    public float getMinAngle() {
        return minAngle;
    }

    public void setMinAngle(float minAngle) {
        this.minAngle = minAngle;
    }
    public float getSecAngle() {
        return secAngle;
    }

    public void setSecAngle(float secAngle) {
        this.secAngle = secAngle;
    }
}
