package engine;

import graphics.Graphics;
import math.Size;

import static org.lwjgl.glfw.GLFW.*;

public class Game {
    public final Window window;
    public final Graphics graphics;

    public Game() {
        this.window = new Window(new Size(1280, 720), "Window");
        this.graphics = new Graphics();
    }

    public final void run() {
        init();

        while (!glfwWindowShouldClose(this.window.handle)) {
            glfwPollEvents();

            update();
            draw();

            glfwSwapInterval(1);
            glfwSwapBuffers(this.window.handle);
        }

        this.window.close();
    }

    protected void init() {}

    protected void update() {}

    protected void draw() {}
}
