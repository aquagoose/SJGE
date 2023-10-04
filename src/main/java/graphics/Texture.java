package graphics;

import static org.lwjgl.opengl.GL33.*;

public class Texture {
    public final int handle;

    public Texture(String fileName) {
        this.handle = glGenTextures();
    }
}
