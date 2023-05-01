import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class FractalExplorer {
    private int displaySize; // сторона квадратного экрана

    private JImageDisplay display;

    private FractalGenerator fractal;

    private Rectangle2D.Double range;
    /** UI элементы для обращения в enableUI*/
    private JButton resetButton;
    private JButton saveButton;
    private JComboBox myComboBox;
    private int rowsRemaining;

    public FractalExplorer(int size) {
        displaySize = size;
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);
    }

    public void createAndShowGUI() {
        display.setLayout(new BorderLayout());
        JFrame Frame = new JFrame("Fractal Explorer");
        // Добавляет и центрует объект изображения
        Frame.add(display, BorderLayout.CENTER);

        /* Создание объекта кнопки сброса */
        resetButton = new JButton("Reset");
        Frame.add(resetButton, BorderLayout.SOUTH);
        ButtonHandler resetHandler = new ButtonHandler();
        // Обработка события нажатия на кнопку
        resetButton.addActionListener(resetHandler);
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        /* Создание объекта combo-box */
        myComboBox = new JComboBox();

        /* Добавляем элементы в combo-box */
        FractalGenerator mandelbrotFractal = new Mandelbrot();
        myComboBox.addItem(mandelbrotFractal);
        FractalGenerator tricornFractal = new Tricorn();
        myComboBox.addItem(tricornFractal);
        FractalGenerator burningShipFractal = new BurningShip();
        myComboBox.addItem(burningShipFractal);

        /* Обрабатывать нажатия будет ButtonHandler */
        ButtonHandler fractalChooser = new ButtonHandler();
        myComboBox.addActionListener(fractalChooser);

        /*
         * Создаём панель и добавляем на неё combo-box.
         * Добавляем также текст пояснения "Fractal:"
         * Наконец, прописываем расположение этой панели наверху
         */
        JPanel myPanel = new JPanel();
        JLabel myLabel = new JLabel("Fractal:");
        myPanel.add(myLabel);
        myPanel.add(myComboBox);
        Frame.add(myPanel, BorderLayout.NORTH);

        /**
         * Создаём кнопку сохранения и добавляем её на созданную панель внизу
         */
        saveButton = new JButton("Save");
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(saveButton);
        myBottomPanel.add(resetButton);
        Frame.add(myBottomPanel, BorderLayout.SOUTH);

        /** Обрабатывать события будет ButtonHandler **/
        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);

        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Frame.pack();
        Frame.setVisible(true);
        Frame.setResizable(false);

    }

    /**
     * Вспомогательный метод для отображения фрактала.
     * Он проходится по пикселям на дисплее и вычисляет количество итераций для координат во фрактале
     * Если кол-во итераций = -1, он устанавливает чёрный цвет пикселя,
     * иначе же выбирает значение в зависимости от количества итераций.
     * Когда всё готово - обновляет дисплей
     */
    private void drawFractal() {
        enableUI(false);
        rowsRemaining = displaySize;
        for (int x = 0; x < displaySize; x++){
            FractalWorker drawRow = new FractalWorker(x);
            drawRow.execute();
        }
    }


    private class MouseHandler extends MouseAdapter {
        /**
         * Когда происходит нажатие мышкой, перемещается на указанные щелчком координаты.
         * Приближение вполовину от нынешнего.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (rowsRemaining != 0) {
                return;
            }
            // Принимает x координату нажатия
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);
            // Принимает y координату нажатия
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            // Перерисовывает фрактал после приближения
            drawFractal();
        }
    }

    /**
     * Внутренний класс для обрабокти событий ActionListener
     **/
    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            /** Если нажатие на combo-box, берётся выбранный фрактал и выводится на дисплей */
            if (e.getSource() instanceof JComboBox) {
                JComboBox mySource = (JComboBox) e.getSource();
                fractal = (FractalGenerator) mySource.getSelectedItem();
                fractal.getInitialRange(range);
                drawFractal();
            }
            /** Если нажатие на кнопку сброса, сбрасывает приближение */
            else if (command.equals("Reset")) {
                fractal.getInitialRange(range);
                drawFractal();
            }
            /** Если нажатие на кнопку сохранения, сохраняет текущее отображение фрактала */
            else if (command.equals("Save")) {

                JFileChooser myFileChooser = new JFileChooser();

                /** Сохраняет только PNG-изображения **/
                FileFilter extensionFilter = new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(extensionFilter);
                /** Удостоверяется, что filechooser не разрешит что-то, помимо *.png*/
                myFileChooser.setAcceptAllFileFilterUsed(false);

                /** Открывает окно с возможностью выбора директории сохранения */
                int userSelection = myFileChooser.showSaveDialog(display);

                /** Если пользователь решает сохранить файл, операция сохранения продолжается */
                if (userSelection == JFileChooser.APPROVE_OPTION) {

                    /** Получает файл и имя файла **/
                    java.io.File file = myFileChooser.getSelectedFile();
                    String file_name = file.toString();

                    /** Пытается сохранить изображение на диск **/
                    try {
                        BufferedImage showImage = display.getImage();
                        javax.imageio.ImageIO.write(showImage, "png", file);
                    }
                    /** Ловит все исключения и пишет сообщение об исключении */ catch (Exception exception) {
                        JOptionPane.showMessageDialog(display, exception.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                    }
                }
                /** Если пользователь передумал сохранять файл, return */
                else return;
            }
        }
    }
    /** Класс для реализации многопоточных вычислений, вычисления фракталов будут происходить в отдельных потоках */
    private class FractalWorker extends SwingWorker<Object, Object> {
        private int yCoord;
        private int[] pixelRGB;
        //Конструктор для фракталВоркера
        public FractalWorker(int y) {
            this.yCoord = y;
        }
        //Метод выполняющий фоновые операции
        @Override
        public Object doInBackground() throws Exception {
            pixelRGB = new int[displaySize]; // инициализация массива для хранения цвета пикселей

            for (int i = 0; i < displaySize; ++i) {
                double x = fractal.getCoord(range.x,
                        range.x + range.width, displaySize, i);
                double y = fractal.getCoord(range.y,
                        range.y + range.height, displaySize, yCoord);
                int iter = fractal.numIterations(x, y);
                if (iter == -1) {
                    pixelRGB[i] = 0;
                } else {
                    float hue = 0.7f + (float) iter / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    pixelRGB[i] = rgbColor;
                }
                display.drawPixel(i, yCoord, pixelRGB[i]);

            }

            return null;
        }
        //Метод вызывается после того как фоновая задача будет выполнена
        @Override
        protected void done() {
            display.repaint(0, 0, yCoord, displaySize, 1);
            rowsRemaining--;
            if (rowsRemaining == 0) {
                enableUI(true);
            }
        }
    }
    /** Функция для запрета действий пользователя во время рендера изображения фрактала*/
    void enableUI(boolean val) {
        saveButton.setEnabled(val);
        resetButton.setEnabled(val);
        myComboBox.setEnabled(val);
    }


    public static void main(String[] args) {
        FractalExplorer displayExplorer = new FractalExplorer(900);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}