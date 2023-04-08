import java.awt.geom.Rectangle2D;

/**
 * Класс для расчёта фрактала Burning Ship
 */
public class BurningShip extends FractalGenerator
{
    public static final int MAX_ITERATIONS = 2000;
    /**
     * Установка начальных значений для формулы расчёта
     */
    public void getInitialRange(Rectangle2D.Double range)
    {
        range.x = -2;
        range.y = -2.5;
        range.width = 2;
        range.height = 1.5;
    }
    /** Метод, рассчитывающий количество итераций для пикселей */
    public int numIterations(double x, double y)
    {
        int iteration = 0;
        double zreal = 0;
        double zimaginary = 0;

        while (iteration < MAX_ITERATIONS &&
                zreal * zreal + zimaginary * zimaginary < 4) {
            double zrealUpdated = zreal * zreal - zimaginary * zimaginary + x;
            double zimaginaryUpdated = 2 * Math.abs(zreal) * Math.abs(zimaginary) + y;
            zreal = zrealUpdated;
            zimaginary = zimaginaryUpdated;
            iteration += 1;
        }
        //Если счетчик не дойдет до максимума - вернем его значение, иначе -1 (черный цвет)
        return iteration < MAX_ITERATIONS ? iteration : -1;
    }
    /** Возвращает имя фрактала "Burning Ship" */
    public String toString() { return "Burning Ship"; }
}