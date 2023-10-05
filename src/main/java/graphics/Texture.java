package graphics;

import math.Size;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    public final int handle;

    public final Size size;

    public Texture(String fileName) {
        this.handle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.handle);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            ByteBuffer data = stbi_load(fileName, width, height, comp, 4);

            this.size = new Size(width.get(), height.get());

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, this.size.width, this.size.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);

            glGenerateMipmap(GL_TEXTURE_2D);
        }

        Graphics.CLEANER.register(this, cleanResource(handle));
    }

    private static Runnable cleanResource(final int handle) {
        return () -> {
            System.out.println("Clean texture. " + handle);
        };
    }
}
