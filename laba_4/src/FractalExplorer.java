import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;


public class FractalExplorer {
    /** Ширина и высота для отображения **/
    private int displaySize;

    /** JImageDisplay для обновления отображения изображения**/
    private JImageDisplay display;

    /** Объект для разных типов фракталов (будет добавлено позже)**/
    private FractalGenerator fractal;

    /** Объект, определяющий размер текущего диапозона просмотра (То, что показывается в настоящий момент) **/
    private Rectangle2D.Double range;

    /**
     * Конструктор, принимающий размер дисплея и сохраняющий его,
     * после чего инициализирующий объекты диапозона и генератора фрактала
     */
    public FractalExplorer(int size) {
        displaySize = size; // Сохранение значения размера отображения в переменной displaySize
        // Инициализация объектов диапазона и фрактального генератора
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);
    }

    /** Метод, который инициализирует графический интерфейс Swing (вывод результата) и кнопку сброса **/
    public void createAndShowGUI() {
        display.setLayout(new BorderLayout());
        JFrame frame = new JFrame("Fractal Explorer"); // Даем заголовок нашему окну
        frame.add(display, BorderLayout.CENTER); // Добавляет и центрирует объект изображения
        frame.setSize(displaySize, displaySize); // Определение размера окна

        // Инициализация панели с кнопкой сброса
        JPanel myPanel = new JPanel();
        frame.add(myPanel, BorderLayout.NORTH);
        JButton resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(200, 100));
        frame.add(resetButton, BorderLayout.SOUTH);

        ButtonHandler resetHandler = new ButtonHandler();
        //Обработка события нажатия на кнопку Reset
        resetButton.addActionListener(resetHandler);

        //Обработка события нажатия мышкой
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Закрытие окна по клику на крестик

        frame.pack();
        frame.setVisible(true); // Делаем рамку видимой
        frame.setResizable (false);
    }

    /**
     * Вспомогательный метод для отображения фрактала.
     * Он проходится по пикселям на дисплее и вычисляет количество итераций для координат во фрактале
     * Если кол-во итераций = -1, он устанавливает чёрный цвет пикселя,
     * иначе же выбирает значение в зависимости от количества итераций.
     * Когда всё готово - обновляет дисплей
     */
    private void drawFractal() {
        for (int x = 0; x < displaySize; x++){
            for (int y = 0; y < displaySize; y++){
                double xCoord = fractal.getCoord(range.x,
                        range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y,
                        range.y + range.height, displaySize, y);
                int i = fractal.numIterations(xCoord, yCoord);
                if (i == -1){
                    display.drawPixel(x, y, 0);
                }
                else{
                    float hue = 0.7f + (float) i / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    display.drawPixel(x, y, rgbColor);
                }
            }
        }
        display.repaint();
    }


    /** Внутренний класс для обрабокти событий MouseAdapter
     * Когда происходит нажатие мышкой, перемещается на указанные щелчком координаты.
     * Приближение в 0.5 от нынешнего
     * **/
   private class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked (MouseEvent e) {
            // Принимает х координату после нажатия
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);
            // Принимает у координату после нажатия
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            // Перерисовывает фрактал после приближения
            drawFractal();
        }
    }
    /** Внутренний класс для обрабокти событий ActionListener **/
    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Reset")) {
                fractal.getInitialRange(range);
                drawFractal();
            }
        }
    }

    public static void main(String[] args) {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}
