package com.ephstrophy.eglckock.objects;

/**
 * Created by Keeyor on 5/12/2016.
 */
import android.util.Log;

import com.ephstrophy.eglckock.data.VertexArray;
import com.ephstrophy.eglckock.programs.TextureShaderProgram;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static com.ephstrophy.eglckock.Constants.BYTES_PER_FLOAT;

public class Clock {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private static final float HOUR_INNER_RADIUS  = 0.4f;
    private static final float HOUR_OUTER_RADIUS  = 0.5f;
    private static final float HOUR_POINTER_WIDTH = 0.01f;


    static float R2    =  (float) Math.sqrt(HOUR_INNER_RADIUS*HOUR_INNER_RADIUS + HOUR_POINTER_WIDTH*HOUR_POINTER_WIDTH);
    static float R3    =  (float) Math.sqrt(HOUR_OUTER_RADIUS*HOUR_OUTER_RADIUS + HOUR_POINTER_WIDTH*HOUR_POINTER_WIDTH);

    static float angle2 = (float) (Math.asin(HOUR_POINTER_WIDTH/R2)*57.295779513);
    static float angle3 = (float) (Math.asin(HOUR_POINTER_WIDTH/R3)*57.295779513);

    static float x30_1 = (float)  (R2*Math.cos((90-(30-angle2))*(Math.PI/180)));
    static float y30_1 = (float)  (R2*Math.sin((90-(30-angle2))*(Math.PI/180)));

    static float x30_2 = (float)  (R2*Math.cos((90-(30+angle2))*(Math.PI/180)));
    static float y30_2 = (float)  (R2*Math.sin((90-(30+angle2))*(Math.PI/180)));

    static float x30_3 = (float)  (R3*Math.cos((90-(30+angle3))*(Math.PI/180)));
    static float y30_3 = (float)  (R3*Math.sin((90-(30+angle3))*(Math.PI/180)));

    static float x30_4 = (float)  (R3*Math.cos((90-(30-angle3))*(Math.PI/180)));
    static float y30_4 = (float)  (R3*Math.sin((90-(30-angle3))*(Math.PI/180)));

    static float x60_1 = (float)  (R2*Math.cos((90-(60-angle2))*(Math.PI/180)));
    static float y60_1 = (float)  (R2*Math.sin((90-(60-angle2))*(Math.PI/180)));

    static float x60_2 = (float)  (R2*Math.cos((90-(60+angle2))*(Math.PI/180)));
    static  float y60_2 = (float)  (R2*Math.sin((90-(60+angle2))*(Math.PI/180)));

    static float x60_3 = (float)  (R3*Math.cos((90-(60+angle3))*(Math.PI/180)));
    static float y60_3 = (float)  (R3*Math.sin((90-(60+angle3))*(Math.PI/180)));

    static float x60_4 = (float)  (R3*Math.cos((90-(60-angle3))*(Math.PI/180)));
    static float y60_4 = (float)  (R3*Math.sin((90-(60-angle3))*(Math.PI/180)));


    static float x1_line = (float)  (0.7f*Math.cos((90-(6-angle2))*(Math.PI/180)));
    static float y1_line = (float)  (0.7f*Math.sin((90-(6-angle2))*(Math.PI/180)));

    static float x2_line = (float)  (0.7f*Math.cos((90-(12-angle2))*(Math.PI/180)));
    static float y2_line = (float)  (0.7f*Math.sin((90-(12-angle2))*(Math.PI/180)));

    static float x3_line = (float)  (0.7f*Math.cos((90-(18-angle2))*(Math.PI/180)));
    static float y3_line = (float)  (0.7f*Math.sin((90-(18-angle2))*(Math.PI/180)));

    static float x4_line = (float)  (0.7f*Math.cos((90-(24-angle2))*(Math.PI/180)));
    static float y4_line = (float)  (0.7f*Math.sin((90-(24-angle2))*(Math.PI/180)));

    static float x5_line = (float)  (0.7f*Math.cos((90-(30-angle2))*(Math.PI/180)));
    static float y5_line = (float)  (0.7f*Math.sin((90-(30-angle2))*(Math.PI/180)));

    static float x6_line = (float)  (0.7f*Math.cos((90-(36-angle2))*(Math.PI/180)));
    static float y6_line = (float)  (0.7f*Math.sin((90-(36-angle2))*(Math.PI/180)));

    static float x7_line = (float)  (0.7f*Math.cos((90-(42-angle2))*(Math.PI/180)));
    static float y7_line = (float)  (0.7f*Math.sin((90-(42-angle2))*(Math.PI/180)));

    static float x8_line = (float)  (0.7f*Math.cos((90-(48-angle2))*(Math.PI/180)));
    static float y8_line = (float)  (0.7f*Math.sin((90-(48-angle2))*(Math.PI/180)));

    static  float x9_line = (float)  (0.7f*Math.cos((90-(54-angle2))*(Math.PI/180)));
    static float y9_line = (float)  (0.7f*Math.sin((90-(54-angle2))*(Math.PI/180)));

    static float x10_line = (float)  (0.7f*Math.cos((90-(60-angle2))*(Math.PI/180)));
    static float y10_line = (float)  (0.7f*Math.sin((90-(60-angle2))*(Math.PI/180)));

    static float x11_line = (float)  (0.7f*Math.cos((90-(66-angle2))*(Math.PI/180)));
    static float y11_line = (float)  (0.7f*Math.sin((90-(66-angle2))*(Math.PI/180)));

    static float x12_line = (float)  (0.7f*Math.cos((90-(72-angle2))*(Math.PI/180)));
    static float y12_line = (float)  (0.7f*Math.sin((90-(72-angle2))*(Math.PI/180)));

    static float x13_line = (float)  (0.7f*Math.cos((90-(78-angle2))*(Math.PI/180)));
    static float y13_line = (float)  (0.7f*Math.sin((90-(78-angle2))*(Math.PI/180)));

    static float x14_line = (float)  (0.7f*Math.cos((90-(84-angle2))*(Math.PI/180)));
    static float y14_line = (float)  (0.7f*Math.sin((90-(84-angle2))*(Math.PI/180)));

    private static final float[] VERTEX_DATA = {

            // Order of coordinates: X, Y, S, T


            // Triangle 12-1
            - 0.01f, 0.4f,      1f, 1f,
              0.01f, 0.5f,        1f, 1f,
             -0.01f, 0.5f,       1f, 1f,

            // Triangle 12-2
            - 0.01f, 0.4f,  1f, 1f,
            0.01f, 0.4f,    1f, 1f,
            0.01f, 0.5f,    1f, 1f,

            // Triangle 3-1
            0.4f, 0.01f,    1f, 1f,
            0.5f, -0.01f,   1f, 1f,
            0.5f, 0.01f,    1f, 1f,

            // Triangle 3-2
            0.4f, 0.01f, 1f, 1f,
            0.5f, -0.01f, 1f, 1f,
            0.4f, -0.01f, 1f, 1f,

            // Triangle 6-1
            -0.01f, -0.5f, 1f, 1f,
            0.01f, -0.4f, 1f, 1f,
            -0.01f, -0.4f, 1f, 1f,

            // Triangle 6-2
            -0.01f, -0.5f, 1f, 1f,
            0.01f, -0.5f, 1f, 1f,
            0.01f, -0.4f, 1f, 1f,

            // Triangle 9-1
            -0.5f, -0.01f, 1f, 1f,
            -0.4f, 0.01f, 1f, 1f,
            -0.5f, 0.01f, 1f, 1f,

            // Triangle 9-2
            -0.5f, -0.01f, 1f, 1f,
            -0.4f, -0.01f, 1f, 1f,
            -0.4f, 0.01f, 1f, 1f,

            x30_1, y30_1, 1f, 1f,
            x30_2, y30_2, 1f, 1f,
            x30_3, y30_3, 1f, 1f,

            x30_1, y30_1, 1f, 1f,
            x30_3, y30_3, 1f, 1f,
            x30_4, y30_4, 1f, 1f,

            -x30_1, y30_1, 1f, 1f,
            -x30_2, y30_2, 1f, 1f,
            -x30_3, y30_3, 1f, 1f,

            -x30_1, y30_1, 1f, 1f,
            -x30_3, y30_3, 1f, 1f,
            -x30_4, y30_4, 1f, 1f,

            x30_1, -y30_1, 1f, 1f,
            x30_2, -y30_2, 1f, 1f,
            x30_3, -y30_3, 1f, 1f,

            x30_1, -y30_1, 1f, 1f,
            x30_3, -y30_3, 1f, 1f,
            x30_4, -y30_4, 1f, 1f,

            -x30_1, -y30_1, 1f, 1f,
            -x30_2, -y30_2, 1f, 1f,
            -x30_3, -y30_3, 1f, 1f,

            -x30_1, -y30_1, 1f, 1f,
            -x30_3, -y30_3, 1f, 1f,
            -x30_4, -y30_4, 1f, 1f,

            x60_1, y60_1, 1f, 1f,
            x60_2, y60_2, 1f, 1f,
            x60_3, y60_3, 1f, 1f,

            x60_1, y60_1, 1f, 1f,
            x60_3, y60_3, 1f, 1f,
            x60_4, y60_4, 1f, 1f,

            -x60_1, y60_1, 1f, 1f,
            -x60_2, y60_2, 1f, 1f,
            -x60_3, y60_3, 1f, 1f,

            -x60_1, y60_1, 1f, 1f,
            -x60_3, y60_3, 1f, 1f,
            -x60_4, y60_4, 1f, 1f,

            x60_1, -y60_1, 1f, 1f,
            x60_2, -y60_2, 1f, 1f,
            x60_3, -y60_3, 1f, 1f,

            x60_1, -y60_1, 1f, 1f,
            x60_3, -y60_3, 1f, 1f,
            x60_4, -y60_4, 1f, 1f,

            -x60_1, -y60_1, 1f, 1f,
            -x60_2, -y60_2, 1f, 1f,
            -x60_3, -y60_3, 1f, 1f,

            -x60_1, -y60_1, 1f, 1f,
            -x60_3, -y60_3, 1f, 1f,
            -x60_4, -y60_4, 1f, 1f,

            //Minutes dots
            0.7f, 0f,   1f,   1f,
            -0.7f, 0f, 1f, 1f,
            0, 0.7f, 1f, 1f,
            0, -0.7f, 1f, 1f,

            x1_line, y1_line, 1f, 1f,
            -x1_line, y1_line, 1f, 1f,
            x1_line, -y1_line, 1f, 1f,
            -x1_line, -y1_line, 1f, 1f,

            x2_line, y2_line, 1f, 1f,
            -x2_line, y2_line, 1f, 1f,
            x2_line, -y2_line, 1f, 1f,
            -x2_line, -y2_line, 1f, 1f,

            x3_line, y3_line, 1f, 1f,
            -x3_line, y3_line, 1f, 1f,
            x3_line, -y3_line, 1f, 1f,
            -x3_line, -y3_line, 1f, 1f,

            x4_line, y4_line, 1f, 1f,
            -x4_line, y4_line, 1f, 1f,
            x4_line, -y4_line, 1f, 1f,
            -x4_line, -y4_line, 1f, 1f,

            x5_line, y5_line, 1f, 1f,
            -x5_line, y5_line, 1f, 1f,
            x5_line, -y5_line, 1f, 1f,
            -x5_line, -y5_line, 1f, 1f,

            x6_line, y6_line, 1f, 1f,
            -x6_line, y6_line, 1f, 1f,
            x6_line, -y6_line, 1f, 1f,
            -x6_line, -y6_line, 1f, 1f,

            x7_line, y7_line, 1f, 1f,
            -x7_line, y7_line, 1f, 1f,
            x7_line, -y7_line, 1f, 1f,
            -x7_line, -y7_line, 1f, 1f,

            x8_line, y8_line, 1f, 1f,
            -x8_line, y8_line, 1f, 1f,
            x8_line, -y8_line, 1f, 1f,
            -x8_line, -y8_line, 1f, 1f,

            x9_line, y9_line, 1f, 1f,
            -x9_line, y9_line, 1f, 1f,
            x9_line, -y9_line, 1f, 1f,
            -x9_line, -y9_line, 1f, 1f,

            x10_line, y10_line, 1f, 1f,
            -x10_line, y10_line, 1f, 1f,
            x10_line, -y10_line, 1f, 1f,
            -x10_line, -y10_line, 1f, 1f,

            x11_line, y11_line, 1f, 1f,
            -x11_line, y11_line, 1f, 1f,
            x11_line, -y11_line, 1f, 1f,
            -x11_line, -y11_line, 1f, 1f,

            x12_line, y12_line, 1f, 1f,
            -x12_line, y12_line, 1f, 1f,
            x12_line, -y12_line, 1f, 1f,
            -x12_line, -y12_line, 1f, 1f,

            x13_line, y13_line, 1f, 1f,
            -x13_line, y13_line, 1f, 1f,
            x13_line, -y13_line, 1f, 1f,
            -x13_line, -y13_line, 1f, 1f,

            x14_line, y14_line, 1f, 1f,
            -x14_line, y14_line, 1f, 1f,
            x14_line, -y14_line, 1f, 1f,
            -x14_line, -y14_line, 1f, 1f
    };
    private final VertexArray vertexArray;

    public Clock() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }
    public void bindData(TextureShaderProgram textureProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }
    public void draw() {
        for (int i=0;i<=66;i+=6) {
            glDrawArrays(GL_TRIANGLES, i, 6);

        }
        for (int i=72;i<=131;i++) {
            // Draw the center dividing line.
            // glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
            glDrawArrays(GL_POINTS, i, 1);
        }
    }
}
