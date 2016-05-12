package com.ephstrophy.eglckock.objects;

/**
 * Created by Keeyor on 5/12/2016.
 */

import com.ephstrophy.eglckock.data.VertexArray;
import com.ephstrophy.eglckock.programs.TextureShaderProgram;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static com.ephstrophy.eglckock.Constants.BYTES_PER_FLOAT;


/**
 * Created by Keeyor on 5/12/2016.
 */
public class Mpointer {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;


    private static final float[] VERTEX_DATA = {

            // Order of coordinates: X, Y, S, T
            //Minutes Pointer
            // Triangle 12-1
            -0.01f, -0.10f,     0f, 0f,
            0.01f, 0.65f,      0f, 0f,
            -0.01f, 0.65f,      0f, 0f,

            // Triangle 12-2
            -0.01f, -0.1f, 0f, 0f,
            0.01f, 0.65f, 0f, 0f,
            0.01f, -0.1f, 0f, 0f,
    };
    private final VertexArray vertexArray;

    public Mpointer() {
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
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }
}
