package graphics;

import math.Color;
import math.Vector2;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

public class SpriteRenderer {
    public static final int MAX_SPRITES = 1 << 14;

    // 4 vertices per sprite, consisting of 8 floats each
    // vec2(pos) + vec2(tex) + vec4(tint)
    private static final int NUM_VERTICES = 4 * 8;

    private static final int NUM_INDICES = 6;

    private static final int MAX_VERTICES = NUM_VERTICES * MAX_SPRITES;

    private static final int MAX_INDICES = NUM_INDICES * MAX_SPRITES;

    private float[] vertices;
    private int[] indices;

    private final int vao;
    private final int vbo;
    private final int ebo;

    private final Effect defaultEffect;

    private Texture currentTexture;

    private int currentSprite;

    private FloatBuffer matrixBuffer;

    public SpriteRenderer() {
        this.vao = glGenVertexArrays();
        glBindVertexArray(this.vao);

        this.vertices = new float[MAX_VERTICES];
        this.indices = new int[MAX_INDICES];

        this.vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
        // Multiply by 4 for the size in bytes of float.
        glBufferData(GL_ARRAY_BUFFER, MAX_VERTICES * 4, GL_DYNAMIC_DRAW);

        this.ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ebo);
        // Multiply by 4 for the size in bytes of int.
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, MAX_INDICES * 4, GL_DYNAMIC_DRAW);

        // sizeof(vec2) + sizeof(vec2) + sizeof(vec4) = 8 + 8 + 16 = 32
        final int stride = 32;

        // aPosition
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);

        // aTexCoord
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, 8);

        // aTint
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, 4, GL_FLOAT, false, stride, 16);

        this.defaultEffect = new Effect(VERTEX_SHADER, FRAGMENT_SHADER);

        this.matrixBuffer = BufferUtils.createFloatBuffer(16);
    }

    public void begin() {
        Matrix4f projection = new Matrix4f().ortho(0, 1280, 720, 0, -1, 1);
        Matrix4f view = new Matrix4f().identity();

        int projLoc = glGetUniformLocation(this.defaultEffect.handle, "uProjection");
        glUniformMatrix4fv(projLoc, false, projection.get(this.matrixBuffer));

        int viewLoc = glGetUniformLocation(this.defaultEffect.handle, "uView");
        glUniformMatrix4fv(viewLoc, false, view.get(this.matrixBuffer));
    }

    public void end() {
        this.flush();
    }

    public void draw(Texture texture, Vector2 topLeft, Vector2 topRight, Vector2 bottomLeft, Vector2 bottomRight, Color tint) {
        if (this.currentTexture != texture || this.currentSprite >= MAX_SPRITES) {
            this.flush();
        }

        this.currentTexture = texture;

        int vOffset = this.currentSprite * NUM_VERTICES;
        int iOffset = this.currentSprite * NUM_INDICES;

        this.vertices[vOffset + 0] = topLeft.x;
        this.vertices[vOffset + 1] = topLeft.y;
        this.vertices[vOffset + 2] = 0;
        this.vertices[vOffset + 3] = 0;
        this.vertices[vOffset + 4] = tint.r;
        this.vertices[vOffset + 5] = tint.g;
        this.vertices[vOffset + 6] = tint.b;
        this.vertices[vOffset + 7] = tint.a;

        this.vertices[vOffset + 8] = topRight.x;
        this.vertices[vOffset + 9] = topRight.y;
        this.vertices[vOffset + 10] = 1;
        this.vertices[vOffset + 11] = 0;
        this.vertices[vOffset + 12] = tint.r;
        this.vertices[vOffset + 13] = tint.g;
        this.vertices[vOffset + 14] = tint.b;
        this.vertices[vOffset + 15] = tint.a;

        this.vertices[vOffset + 16] = bottomRight.x;
        this.vertices[vOffset + 17] = bottomRight.y;
        this.vertices[vOffset + 18] = 1;
        this.vertices[vOffset + 19] = 1;
        this.vertices[vOffset + 20] = tint.r;
        this.vertices[vOffset + 21] = tint.g;
        this.vertices[vOffset + 22] = tint.b;
        this.vertices[vOffset + 23] = tint.a;

        this.vertices[vOffset + 24] = bottomLeft.x;
        this.vertices[vOffset + 25] = bottomLeft.y;
        this.vertices[vOffset + 26] = 0;
        this.vertices[vOffset + 27] = 1;
        this.vertices[vOffset + 28] = tint.r;
        this.vertices[vOffset + 29] = tint.g;
        this.vertices[vOffset + 30] = tint.b;
        this.vertices[vOffset + 31] = tint.a;

        this.indices[iOffset + 0] = 0 + vOffset;
        this.indices[iOffset + 1] = 1 + vOffset;
        this.indices[iOffset + 2] = 3 + vOffset;
        this.indices[iOffset + 3] = 1 + vOffset;
        this.indices[iOffset + 4] = 2 + vOffset;
        this.indices[iOffset + 5] = 3 + vOffset;

        this.currentSprite++;
    }

    private void flush() {
        // No need to flush if there are no sprites to draw.
        if (this.currentSprite == 0) {
            return;
        }

        glBindVertexArray(this.vao);

        glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ebo);

        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, indices);

        glUseProgram(this.defaultEffect.handle);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this.currentTexture.handle);

        glDrawElements(GL_TRIANGLES, this.currentSprite * NUM_INDICES, GL_UNSIGNED_INT, 0);

        this.currentSprite = 0;
    }

    private static final String VERTEX_SHADER = """
            #version 330 core
            
            layout (location = 0) in vec2 aPosition;
            layout (location = 1) in vec2 aTexCoord;
            layout (location = 2) in vec4 aTint;
            
            out vec2 frag_texCoord;
            out vec4 frag_tint;
            
            uniform mat4 uProjection;
            uniform mat4 uView;
            
            void main() {
                gl_Position = uProjection * uView * vec4(aPosition, 0.0, 1.0);
                
                frag_texCoord = aTexCoord;
                frag_tint = aTint;
            }""";

    private static final String FRAGMENT_SHADER = """
            #version 330 core
            
            in vec2 frag_texCoord;
            in vec4 frag_tint;
            
            out vec4 out_color;
            
            uniform sampler2D uSprite;
            
            void main() {
                out_color = texture(uSprite, frag_texCoord) * frag_tint;
            }""";
}
