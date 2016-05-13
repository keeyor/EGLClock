package com.ephstrophy.eglckock.objects;

import android.opengl.Matrix;

import com.ephstrophy.eglckock.data.VertexArray;
import com.ephstrophy.eglckock.programs.ColorShaderProgram;
import com.ephstrophy.eglckock.programs.TextureShaderProgram;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.Matrix.setIdentityM;
import static com.ephstrophy.eglckock.Constants.BYTES_PER_FLOAT;

/**
 * Created by Keeyor on 5/12/2016.
 */
public class Pointer {
    private static final int POSITION_COMPONENT_COUNT = 2;

    private static final float[] VERTEX_DATA = {

            // Order of coordinates: X, Y, S, T

            -0.01f, -0.01f,
             0.01f, -0.01f,
             0.0f, 0.35f

    };
    private final VertexArray vertexArray;


    public Pointer() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }
/*    public void bindData(TextureShaderProgram textureProgram) {
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
    }*/
    public void bindData(ColorShaderProgram colorProgram) {
    vertexArray.setVertexAttribPointer(0,
            colorProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT, 0);
    }
    public void draw() {
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }
}
