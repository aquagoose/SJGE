import engine.*;
import math.Color;

public class TestGame extends Game {
    public static void main(String[] args) {
        TestGame game = new TestGame();
        game.run();
    }

    @Override
    protected void draw() {
        super.draw();

        graphics.clear(new Color(1.0f, 0.5f, 0.25f, 1.0f));
    }
}
