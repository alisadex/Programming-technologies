import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator {

    public static final int MAX_ITERATIONS = 2000;
    /** Переопределение метода установки начальных значений для формулы расчёта */
    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }

    @Override
    /** Метод, рассчитывающий количество итераций для пикселей */
    public int numIterations(double x, double y)
    {
        int iteration = 0;
        double zreal = 0;
        double zimaginary = 0;

        while (iteration < MAX_ITERATIONS &&
                zreal * zreal + zimaginary * zimaginary < 4) {
            double zrealUpdated = zreal * zreal - zimaginary * zimaginary + x;
            double zimaginaryUpdated = -2 * zreal * zimaginary + y;
            zreal = zrealUpdated;
            zimaginary = zimaginaryUpdated;
            iteration += 1;
        }
        //Если счетчик не дойдет до максимума - вернем его значение, иначе -1 (черный цвет)
        return iteration < MAX_ITERATIONS ? iteration : -1;
    }

    /** Возвращает имя фрактала "Tricorn" **/
    public String toString() {
        return "Tricorn";
    }
}
