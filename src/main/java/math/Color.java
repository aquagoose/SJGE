package math;

public class Color {
    public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);

    public float r;

    public float g;

    public float b;

    public float a;

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    public String toString() {
        return "Color{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }
}
