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
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.ephstrophy.eglckock.objects.Mallet;
import com.ephstrophy.eglckock.objects.Puck;
import com.ephstrophy.eglckock.programs.ColorShaderProgram;
import com.ephstrophy.eglckock.programs.TextureShaderProgram;
import com.ephstrophy.eglckock.util.LoggerConfig;
import com.ephstrophy.eglckock.util.MatrixHelper;
import com.ephstrophy.eglckock.util.ShaderHelper;
import com.ephstrophy.eglckock.util.TextResourceReader;
import com.ephstrophy.eglckock.util.TextureHelper;

public class EGLClockRenderer implements Renderer {

    private static final String U_MATRIX = "u_Matrix";
    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4;
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final FloatBuffer vertexData;
    private final Context context;

    private int program;
    private int aPositionLocation;
    private int aColorLocation;

    private final float[] projectionMatrix = new float[16];
    private int uMatrixLocation;

    private final float[] modelMatrix = new float[16];

    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    private Puck puck;
    private Mallet mallet;
    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;

    private int texture;


    private static final float HOUR_INNER_RADIUS  = 0.4f;
    private static final float HOUR_OUTER_RADIUS  = 0.5f;
    private static final float HOUR_POINTER_WIDTH = 0.01f;

    public EGLClockRenderer() {
        // This constructor shouldn't be called -- only kept for showing
        // evolution of the code in the chapter.
        context = null;
        vertexData = null;
    }

    public EGLClockRenderer(Context context) {
        this.context = context;


        float R2    =  (float) Math.sqrt(HOUR_INNER_RADIUS*HOUR_INNER_RADIUS + HOUR_POINTER_WIDTH*HOUR_POINTER_WIDTH);
        float R3    =  (float) Math.sqrt(HOUR_OUTER_RADIUS*HOUR_OUTER_RADIUS + HOUR_POINTER_WIDTH*HOUR_POINTER_WIDTH);

        float angle2 = (float) (Math.asin(HOUR_POINTER_WIDTH/R2)*57.295779513);
        float angle3 = (float) (Math.asin(HOUR_POINTER_WIDTH/R3)*57.295779513);

        Log.d("EGL:", "angle2:" + angle2 + " angle3:" + angle3);

        float x30_1 = (float)  (R2*Math.cos((90-(30-angle2))*(Math.PI/180)));
        float y30_1 = (float)  (R2*Math.sin((90-(30-angle2))*(Math.PI/180)));

        float x30_2 = (float)  (R2*Math.cos((90-(30+angle2))*(Math.PI/180)));
        float y30_2 = (float)  (R2*Math.sin((90-(30+angle2))*(Math.PI/180)));

        Log.d("XXX:", "point1:" + x30_1 + "," + y30_1 + " --- " + x30_2 + "," + y30_2);

        float x30_3 = (float)  (R3*Math.cos((90-(30+angle3))*(Math.PI/180)));
        float y30_3 = (float)  (R3*Math.sin((90-(30+angle3))*(Math.PI/180)));

        float x30_4 = (float)  (R3*Math.cos((90-(30-angle3))*(Math.PI/180)));
        float y30_4 = (float)  (R3*Math.sin((90-(30-angle3))*(Math.PI/180)));

        float x60_1 = (float)  (R2*Math.cos((90-(60-angle2))*(Math.PI/180)));
        float y60_1 = (float)  (R2*Math.sin((90-(60-angle2))*(Math.PI/180)));

        float x60_2 = (float)  (R2*Math.cos((90-(60+angle2))*(Math.PI/180)));
        float y60_2 = (float)  (R2*Math.sin((90-(60+angle2))*(Math.PI/180)));

        Log.d("XXX:", "point1:" + x30_1 + "," + y30_1 + " --- " + x30_2 + "," + y30_2);

        float x60_3 = (float)  (R3*Math.cos((90-(60+angle3))*(Math.PI/180)));
        float y60_3 = (float)  (R3*Math.sin((90-(60+angle3))*(Math.PI/180)));

        float x60_4 = (float)  (R3*Math.cos((90-(60-angle3))*(Math.PI/180)));
        float y60_4 = (float)  (R3*Math.sin((90-(60-angle3))*(Math.PI/180)));


        float x1_line = (float)  (0.7f*Math.cos((90-(6-angle2))*(Math.PI/180)));
        float y1_line = (float)  (0.7f*Math.sin((90-(6-angle2))*(Math.PI/180)));

        float x2_line = (float)  (0.7f*Math.cos((90-(12-angle2))*(Math.PI/180)));
        float y2_line = (float)  (0.7f*Math.sin((90-(12-angle2))*(Math.PI/180)));

        float x3_line = (float)  (0.7f*Math.cos((90-(18-angle2))*(Math.PI/180)));
        float y3_line = (float)  (0.7f*Math.sin((90-(18-angle2))*(Math.PI/180)));

        float x4_line = (float)  (0.7f*Math.cos((90-(24-angle2))*(Math.PI/180)));
        float y4_line = (float)  (0.7f*Math.sin((90-(24-angle2))*(Math.PI/180)));

        float x5_line = (float)  (0.7f*Math.cos((90-(30-angle2))*(Math.PI/180)));
        float y5_line = (float)  (0.7f*Math.sin((90-(30-angle2))*(Math.PI/180)));

        float x6_line = (float)  (0.7f*Math.cos((90-(36-angle2))*(Math.PI/180)));
        float y6_line = (float)  (0.7f*Math.sin((90-(36-angle2))*(Math.PI/180)));

        float x7_line = (float)  (0.7f*Math.cos((90-(42-angle2))*(Math.PI/180)));
        float y7_line = (float)  (0.7f*Math.sin((90-(42-angle2))*(Math.PI/180)));

        float x8_line = (float)  (0.7f*Math.cos((90-(48-angle2))*(Math.PI/180)));
        float y8_line = (float)  (0.7f*Math.sin((90-(48-angle2))*(Math.PI/180)));

        float x9_line = (float)  (0.7f*Math.cos((90-(54-angle2))*(Math.PI/180)));
        float y9_line = (float)  (0.7f*Math.sin((90-(54-angle2))*(Math.PI/180)));

        float x10_line = (float)  (0.7f*Math.cos((90-(60-angle2))*(Math.PI/180)));
        float y10_line = (float)  (0.7f*Math.sin((90-(60-angle2))*(Math.PI/180)));

        float x11_line = (float)  (0.7f*Math.cos((90-(66-angle2))*(Math.PI/180)));
        float y11_line = (float)  (0.7f*Math.sin((90-(66-angle2))*(Math.PI/180)));

        float x12_line = (float)  (0.7f*Math.cos((90-(72-angle2))*(Math.PI/180)));
        float y12_line = (float)  (0.7f*Math.sin((90-(72-angle2))*(Math.PI/180)));

        float x13_line = (float)  (0.7f*Math.cos((90-(78-angle2))*(Math.PI/180)));
        float y13_line = (float)  (0.7f*Math.sin((90-(78-angle2))*(Math.PI/180)));

        float x14_line = (float)  (0.7f*Math.cos((90-(84-angle2))*(Math.PI/180)));
        float y14_line = (float)  (0.7f*Math.sin((90-(84-angle2))*(Math.PI/180)));
        
        float[] tableVerticesWithTriangles = {

             // Mallets
              0f, 0f,   1f, 1f, 1f,

            // Triangle 12-1
             -0.01f, 0.4f,  1f, 0f, 0f,
              0.01f, 0.5f,  1f, 0f, 0f,
             -0.01f, 0.5f,  1f, 0f, 0f,

            // Triangle 12-2
             -0.01f, 0.4f,  1f, 0f, 0f,
              0.01f, 0.4f,  1f, 0f, 0f,
              0.01f,  0.5f, 1f, 0f, 0f,

            // Triangle 3-1
              0.4f,  0.01f, 1f, 0f, 0f,
              0.5f, -0.01f, 1f, 0f, 0f,
              0.5f,  0.01f, 1f, 0f, 0f,

            // Triangle 3-2
              0.4f,  0.01f, 1f, 0f, 0f,
              0.5f, -0.01f, 1f, 0f, 0f,
              0.4f, -0.01f, 1f, 0f, 0f,

            // Triangle 6-1
             -0.01f, -0.5f,  1f, 0f, 0f,
              0.01f, -0.4f,  1f, 0f, 0f,
             -0.01f,  -0.4f, 1f, 0f, 0f,

            // Triangle 6-2
             -0.01f, -0.5f, 1f, 0f, 0f,
              0.01f, -0.5f, 1f, 0f, 0f,
              0.01f, -0.4f, 1f, 0f, 0f,

            // Triangle 9-1
               -0.5f, -0.01f,   1f, 0f, 0f,
               -0.4f,  0.01f,   1f, 0f, 0f,
               -0.5f,  0.01f,   1f, 0f, 0f,

            // Triangle 9-2
               -0.5f, -0.01f,   1f, 0f, 0f,
               -0.4f, -0.01f,   1f, 0f, 0f,
               -0.4f,  0.01f,   1f, 0f, 0f,

                x30_1,y30_1,    1f, 0f, 0f,
                x30_2,y30_2,    1f, 0f, 0f,
                x30_3,y30_3,    1f, 0f, 0f,

                x30_1,y30_1,    1f, 0f, 0f,
                x30_3,y30_3,    1f, 0f, 0f,
                x30_4,y30_4,    1f, 0f, 0f,

                -x30_1,y30_1,   1f, 0f, 0f,
                -x30_2,y30_2,   1f, 0f, 0f,
                -x30_3,y30_3,   1f, 0f, 0f,

                -x30_1,y30_1,   1f, 0f, 0f,
                -x30_3,y30_3,   1f, 0f, 0f,
                -x30_4,y30_4,   1f, 0f, 0f,

                x30_1,-y30_1,   1f, 0f, 0f,
                x30_2,-y30_2,   1f, 0f, 0f,
                x30_3,-y30_3,   1f, 0f, 0f,

                x30_1,-y30_1,   1f, 0f, 0f,
                x30_3,-y30_3,   1f, 0f, 0f,
                x30_4,-y30_4,   1f, 0f, 0f,

                -x30_1,-y30_1,  1f, 0f, 0f,
                -x30_2,-y30_2,  1f, 0f, 0f,
                -x30_3,-y30_3,  1f, 0f, 0f,

                -x30_1,-y30_1,  1f, 0f, 0f,
                -x30_3,-y30_3,  1f, 0f, 0f,
                -x30_4,-y30_4,  1f, 0f, 0f,

                x60_1,y60_1,    1f, 0f, 0f,
                x60_2,y60_2,    1f, 0f, 0f,
                x60_3,y60_3,    1f, 0f, 0f,

                x60_1,y60_1,    1f, 0f, 0f,
                x60_3,y60_3,    1f, 0f, 0f,
                x60_4,y60_4,    1f, 0f, 0f,

                -x60_1,y60_1,   1f, 0f, 0f,
                -x60_2,y60_2,   1f, 0f, 0f,
                -x60_3,y60_3,   1f, 0f, 0f,

                -x60_1,y60_1,   1f, 0f, 0f,
                -x60_3,y60_3,   1f, 0f, 0f,
                -x60_4,y60_4,   1f, 0f, 0f,

                x60_1,-y60_1,   1f, 0f, 0f,
                x60_2,-y60_2,   1f, 0f, 0f,
                x60_3,-y60_3,   1f, 0f, 0f,

                x60_1,-y60_1,   1f, 0f, 0f,
                x60_3,-y60_3,   1f, 0f, 0f,
                x60_4,-y60_4,   1f, 0f, 0f,

                -x60_1,-y60_1,  1f, 0f, 0f,
                -x60_2,-y60_2,  1f, 0f, 0f,
                -x60_3,-y60_3,  1f, 0f, 0f,

                -x60_1,-y60_1,  1f, 0f, 0f,
                -x60_3,-y60_3,  1f, 0f, 0f,
                -x60_4,-y60_4,  1f, 0f, 0f,

                //Minutes dots
                0.7f,0f,    1f, 1f, 1f,
                -0.7f,0f,   1f, 1f, 1f,
                0,0.7f,     1f, 1f, 1f,
                0,-0.7f,    1f, 1f, 1f,

                x1_line,y1_line,    1f, 1f, 1f,
                -x1_line,y1_line,   1f, 1f, 1f,
                x1_line,-y1_line,   1f, 1f, 1f,
                -x1_line,-y1_line,  1f, 1f, 1f,

                x2_line,y2_line,    1f, 1f, 1f,
                -x2_line,y2_line,   1f, 1f, 1f,
                x2_line,-y2_line,   1f, 1f, 1f,
                -x2_line,-y2_line,  1f, 1f, 1f,

                x3_line,y3_line,    1f, 1f, 1f,
                -x3_line,y3_line,   1f, 1f, 1f,
                x3_line,-y3_line,   1f, 1f, 1f,
                -x3_line,-y3_line,  1f, 1f, 1f,

                x4_line,y4_line,    1f, 1f, 1f,
                -x4_line,y4_line,   1f, 1f, 1f,
                x4_line,-y4_line,   1f, 1f, 1f,
                -x4_line,-y4_line   ,1f, 1f, 1f,
                
                x5_line,y5_line,    1f, 1f, 1f,
                -x5_line,y5_line,   1f, 1f, 1f,
                x5_line,-y5_line,   1f, 1f, 1f,
                -x5_line,-y5_line,  1f, 1f, 1f,

                x6_line,y6_line,    1f, 1f, 1f,
                -x6_line,y6_line,   1f, 1f, 1f,
                x6_line,-y6_line,   1f, 1f, 1f,
                -x6_line,-y6_line,  1f, 1f, 1f,
                
                x7_line,y7_line,    1f, 1f, 1f,
                -x7_line,y7_line,   1f, 1f, 1f,
                x7_line,-y7_line,   1f, 1f, 1f,
                -x7_line,-y7_line,  1f, 1f, 1f,
                
                x8_line,y8_line,    1f, 1f, 1f,
                -x8_line,y8_line,   1f, 1f, 1f,
                x8_line,-y8_line,   1f, 1f, 1f,
                -x8_line,-y8_line,  1f, 1f, 1f,
                
                x9_line,y9_line,    1f, 1f, 1f,
                -x9_line,y9_line,   1f, 1f, 1f,
                x9_line,-y9_line,   1f, 1f, 1f,
                -x9_line,-y9_line,  1f, 1f, 1f,
                
                x10_line,y10_line,  1f, 1f, 1f,
                -x10_line,y10_line, 1f, 1f, 1f,
                x10_line,-y10_line, 1f, 1f, 1f,
                -x10_line,-y10_line,1f, 1f, 1f,
                
                x11_line,y11_line,  1f, 1f, 1f,
                -x11_line,y11_line, 1f, 1f, 1f,
                x11_line,-y11_line, 1f, 1f, 1f,
                -x11_line,-y11_line,1f, 1f, 1f,
                
                x12_line,y12_line,  1f, 1f,  1f,
                -x12_line,y12_line, 1f, 1f, 1f,
                x12_line,-y12_line, 1f, 1f, 1f,
                -x12_line,-y12_line,1f, 1f, 1f,
                
                x13_line,y13_line,  1f, 1f, 1f,
                -x13_line,y13_line, 1f, 1f, 1f,
                x13_line,-y13_line, 1f, 1f, 1f,
                -x13_line,-y13_line,1f, 1f, 1f,
                
                x14_line,y14_line,  1f, 1f, 1f,
                -x14_line,y14_line, 1f, 1f, 1f,
                x14_line,-y14_line, 1f, 1f, 1f,
                -x14_line,-y14_line,1f, 1f, 1f,
                
                
                //Hour Pointer

                // Triangle 12-1
                -0.01f, 0f,     0f, 0f, 1f,
                 0.01f, 0.35f,  0f, 0f, 1f,
                -0.01f, 0.35f,  0f, 0f, 1f,

                // Triangle 12-2
                -0.01f, 0f,     0f, 0f, 1f,
                 0.01f, 0.35f,  0f, 0f, 1f,
                 0.01f, 0f,     0f, 0f, 1f,

                //Minutes Pointer

                // Triangle 12-1
                0f, 0.01f,      0f, 1f, 0f,
                0.65f, -0.01f,  0f, 1f, 0f,
                0f, -0.01f,     0f, 1f, 0f,

                // Triangle 12-2
                0f, 0.01f,      0f, 1f, 0f,
                0.65f, -0.01f,  0f, 1f, 0f,
                0.65f, 0.01f,   0f, 1f, 0f
        };


        vertexData = ByteBuffer
            .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer();

        vertexData.put(tableVerticesWithTriangles);
    }


    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        /*
		// Set the background clear color to red. The first component is red,
		// the second is green, the third is blue, and the last component is
		// alpha, which we don't use in this lesson.
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
         */

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        String vertexShaderSource = TextResourceReader
            .readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader
            .readTextFileFromResource(context, R.raw.simple_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(program);
        }

        glUseProgram(program);

        uMatrixLocation     = glGetUniformLocation(program, U_MATRIX);
        aPositionLocation   = glGetAttribLocation(program, A_POSITION);
        aColorLocation      = glGetAttribLocation(program, A_COLOR);
        

        
        // Bind our data, specified by the variable vertexData, to the vertex
        // attribute at location A_POSITION_LOCATION.
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aColorLocation);

        mallet = new Mallet(0.08f, 0.05f, 32);

        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
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
/*
         final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;

        if (width > height) {
            // Landscape
            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            // Portrait or square
            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }*/
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
                / (float) height, 1f, 10f);
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        rotateM(modelMatrix, 0, -45f, 1f, 0f, 0f);
        final float[] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);


    }

    /**
     * OnDrawFrame is called whenever a new frame needs to be drawn. Normally,
     * this is done at the refresh rate of the screen.
     */
    @Override
    public void onDrawFrame(GL10 glUnused) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        // Assign the matrix
         glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);


         for (int i=1;i<=67;i+=6) {
            // Draw the 12
            //glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
            glDrawArrays(GL_TRIANGLES, i, 6);

        }
         for (int i=73;i<=132;i++) {
            // Draw the center dividing line.
           // glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
            glDrawArrays(GL_POINTS, i, 1);
        }
        // Hour Pointer
        //glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 133, 6);

        // Minutes
       // glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 139, 6);

        // Draw the second mallet red.
        //glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 0, 1);

        // Draw the mallets.
     /*   positionObjectInScene(0f, mallet.height / 2f, -0.4f);
        colorProgram.useProgram();
        colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f);
        mallet.bindData(colorProgram);
        mallet.draw();*/

        // Draw the center dividing line.
        // unsused glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        // unsused glDrawArrays(GL_LINES, 6, 2);
        
        // Draw the first mallet blue.        
        // unsused glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        // unsused glDrawArrays(GL_POINTS, 8, 1);


    }

    private void positionTableInScene() {
// The table is defined in terms of X & Y coordinates, so we rotate it
// 90 degrees to lie flat on the XZ plane.
        setIdentityM(modelMatrix, 0);
        rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
    }
    private void positionObjectInScene(float x, float y, float z) {
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, x, y, z);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
    }
}
