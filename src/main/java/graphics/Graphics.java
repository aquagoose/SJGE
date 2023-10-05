package graphics;

import math.Color;
import org.lwjgl.opengl.GL;

import java.lang.ref.Cleaner;

import static org.lwjgl.opengl.GL33.*;

public class Graphics {
    public static final Cleaner CLEANER = Cleaner.create();

    public final SpriteRenderer spriteRenderer;

    public Graphics() {
        GL.createCapabilities();

        spriteRenderer = new SpriteRenderer();
    }

    public void clear(Color color) {
        glClearColor(color.r, color.g, color.b, color.a);
        glClear(GL_COLOR_BUFFER_BIT);
    }
}
