package engine;

import math.Size;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    public final long handle;

    public Window(Size size, String title) {
        if (!glfwInit()) {
            throw new RuntimeException("Failed to initialize GLFW.");
        }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        this.handle = glfwCreateWindow(size.width, size.height, title, NULL, NULL);

        glfwMakeContextCurrent(this.handle);

        if (this.handle == NULL) {
            throw new RuntimeException("Window was null. Is OpenGL 3.3 supported on your system?");
        }
    }
}
