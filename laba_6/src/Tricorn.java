import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator {

    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        // Переопределение метода установки начальных значений для формулы расчёта (-2 - 1.5i) - (1 + 1.5i):
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }

    // ��������� � ������������ ����������� ��������
    public static final int MAX_ITERATIONS = 2000;

    public int numIterations(double x, double y) {

        int iterCounter = 0;
        double real = 0;
        double img= 0;
        double zMod = 0; // мнимая часть
        // zn = z(n-1)^2 + c = (real + image)^2 + c = real^2 - 2real*image -image^2 + c
        while(iterCounter < MAX_ITERATIONS && img * img + real * real < 4) {
            iterCounter++;
            double nextReal = real * real - img * img + x;
            double nextImg = -2 * real * img + y;
            real = nextReal;
            img = nextImg;
        }
        return iterCounter < MAX_ITERATIONS ? iterCounter : -1;
    }

    public String toString() { return "Tricorn"; }
}