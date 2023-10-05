import engine.*;
import graphics.Texture;
import math.Color;
import org.joml.Vector2f;

public class TestGame extends Game {
    private Texture texture;

    public static void main(String[] args) {
        TestGame game = new TestGame();
        game.run();
    }

    @Override
    protected void init() {
        super.init();

        this.texture = new Texture("C:\\Users\\ollie\\Pictures\\awesomeface.png");
    }

    @Override
    protected void draw() {
        super.draw();

        graphics.clear(new Color(1.0f, 0.5f, 0.25f, 1.0f));

        graphics.spriteRenderer.begin();
        for (int i = 0; i < 10; i++) {
            graphics.spriteRenderer.draw(this.texture, new Vector2f(i * 50), Color.WHITE);
        }
        graphics.spriteRenderer.end();
    }
}
