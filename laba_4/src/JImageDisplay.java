import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics;

public class JImageDisplay extends JComponent {
    /**
     * Экземпляр буферизированного изображения
     * Управляет изображением, которое мы можем отрисовывать
     */
    private BufferedImage showImage;

    /**
     * Конструктор, принимающий ширину и высоту изображения, после
     * чего инициализирующий объект с такими шириной и высотой
     */
    public JImageDisplay(int width, int height) {
        // Создание объекта изображения
        this.showImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Вызов метода и установка ширины и высоты
        super.setPreferredSize(new Dimension(width, height));
    }

    /**
     * Переопределенный метод. Вызов изображения на экран
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage (showImage, 0, 0, showImage.getWidth(), showImage.getHeight(), null);
    }

    /**
     * Метод, который устанавливает все пиксели изображения в черный цвет
     */
    public void clearImage() {
        // пробегаемся по высоте и ширине
        for (int j = 0; j < showImage.getHeight(); j++) {
            for (int i = 0; i < showImage.getWidth(); i++) {
                // с помощью метода drawPixel красим все пиксели в черный
                this.drawPixel(i, j, 0);
            }
        }
    }

    /**
     * Метод, который устанавливает пиксель в определенный цвет.
     */
    public void drawPixel(int x, int y, int rgbColor) {
            showImage.setRGB(x, y, rgbColor);
    }
}
