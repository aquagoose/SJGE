package graphics;

import static org.lwjgl.opengl.GL33.*;

public class Effect {
    public final int handle;

    public Effect(String vertexShader, String fragmentShader) {
        int vShader = glCreateShader(GL_VERTEX_SHADER);
        int fShader = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(vShader, vertexShader);
        glShaderSource(fShader, fragmentShader);

        compileShader(vShader);
        compileShader(fShader);

        this.handle = glCreateProgram();
        glAttachShader(this.handle, vShader);
        glAttachShader(this.handle, fShader);

        glLinkProgram(this.handle);

        if (glGetProgrami(this.handle, GL_LINK_STATUS) != GL_TRUE) {
            throw new RuntimeException("Program failed to link: " + glGetProgramInfoLog(this.handle));
        }

        // Detach and delete shaders as they are no longer needed
        glDetachShader(this.handle, vShader);
        glDetachShader(this.handle, fShader);
        glDeleteShader(vShader);
        glDeleteShader(fShader);
    }

    private static void compileShader(int shader) {
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) != GL_TRUE) {
            throw new RuntimeException("Shader failed to compile: " + glGetShaderInfoLog(shader));
        }
    }
}
