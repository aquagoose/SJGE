package graphics;

import math.Color;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL33.*;

public class Graphics {
    public Graphics() {
        GL.createCapabilities();
    }

    public void clear(Color color) {
        glClearColor(color.r, color.g, color.b, color.a);
        glClear(GL_COLOR_BUFFER_BIT);
    }
}
