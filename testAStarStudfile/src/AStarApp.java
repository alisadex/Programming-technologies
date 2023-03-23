package com.company;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



/**
 * Swing – простой инструмент Джавы, оснащенный графическим интерфейсом.
 * Простое Swing-приложение для демонстрации алгоритма поиска пути A*.
 * Пользователю предоставляется карта, содержащая начальное и конечное местоположение.
 * Пользователь может нарисовать или устранить препятствия на карте,
 * а затем нажать кнопку, чтобы вычислить путь от начала до конца, используя алгоритм поиска пути A *.
 * Если путь найден, он отображается зеленым цветом.
 **/
public class AStarApp {

    /** The number of grid cells in the X direction. **/
    private int width;

    /** Количество ячеек сетки в направлении Y. **/
    private int height;

    /** Местоположение, с которого начинается путь. **/
    private Location startLoc;

    /** Место, где должен заканчиваться путь. **/
    private Location finishLoc;

    /**
     * Это 2D-массив компонентов пользовательского интерфейса,
     * которые обеспечивают отображение ячеек на карте и манипулирование ими.
     ***/
    private JMapCell[][] mapCells;


    /**
     * Этот внутренний класс обрабатывает события мыши в основной сетке ячеек карты,
     * изменяя ячейки на основе состояния кнопки мыши и выполненного первоначального редактирования.
     **/
    private class MapCellHandler implements MouseListener
    {
        /**
         * Это значение будет равно true, если была нажата кнопка мыши и в данный момент
         * мы находимся в процессе операции модификации.
         **/
        private boolean modifying;

        /**
         * Это значение определяет, делаем ли мы ячейки проходимыми или непроходимыми.
         *Что это такое, зависит от исходного состояния ячейки, внутри которой была запущена операция.
         **/
        private boolean makePassable;

        /** Инициирует операцию модификации. **/
        public void mousePressed(MouseEvent e)
        {
            modifying = true;

            JMapCell cell = (JMapCell) e.getSource();

            // Если текущая ячейка проходима, то мы делаем их непроходимыми;
            // Если она непроходима, то мы делаем их проходимыми.

            makePassable = !cell.isPassable();

            cell.setPassable(makePassable);
        }

        /** Завершает операцию модификации. **/
        public void mouseReleased(MouseEvent e)
        {
            modifying = false;
        }

        /**
         * Если мышь была нажата, то операция изменения продолжается в новой ячейке.
         **/
        public void mouseEntered(MouseEvent e)
        {
            if (modifying)
            {
                JMapCell cell = (JMapCell) e.getSource();
                cell.setPassable(makePassable);
            }
        }

        /** Не требуется для этого обработчика. **/
        public void mouseExited(MouseEvent e)
        {
            // Этот мы игнорируем.
        }

        /** Не требуется для этого обработчика. **/
        public void mouseClicked(MouseEvent e)
        {
            // И этот тоже.
        }
    }


    /**
     * Создает новый экземпляр приложения Star с
     * заданными шириной и высотой карты.
     **/
    public AStarApp(int w, int h) {
        if (w <= 0)
            throw new IllegalArgumentException("Ширина должна быть больше нуля; Получено " + w);

        if (h <= 0)
            throw new IllegalArgumentException("Высота должна быть больше нуля; Получено " + h);

        width = w;
        height = h;

        startLoc = new Location(2, h / 2);
        finishLoc = new Location(w - 3, h / 2);
    }


    /**
     * Простой вспомогательный метод для настройки пользовательского интерфейса Swing.
     * то вызывается из потока обработчика событий Swing, чтобы быть потокобезопасным.
     **/
    private void initGUI()
    {
        JFrame frame = new JFrame("Реализация алгоритма A*");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        contentPane.setLayout(new BorderLayout());

        // Используйте GridBagLayout, потому что он
        // учитывает предпочтительный размер

        GridBagLayout gbLayout = new GridBagLayout();
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.weightx = 1;
        gbConstraints.weighty = 1;
        gbConstraints.insets.set(0, 0, 1, 1);

        JPanel mapPanel = new JPanel(gbLayout);
        mapPanel.setBackground(Color.GRAY);

        mapCells = new JMapCell[width][height];

        MapCellHandler cellHandler = new MapCellHandler();

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                mapCells[x][y] = new JMapCell();

                gbConstraints.gridx = x;
                gbConstraints.gridy = y;

                gbLayout.setConstraints(mapCells[x][y], gbConstraints);

                mapPanel.add(mapCells[x][y]);
                mapCells[x][y].addMouseListener(cellHandler);
            }
        }

        contentPane.add(mapPanel, BorderLayout.CENTER);

        JButton findPathButton = new JButton("Найти путь");
        findPathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { findAndShowPath(); }
        });

        contentPane.add(findPathButton, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        mapCells[startLoc.xCoord][startLoc.yCoord].setEndpoint(true);
        mapCells[finishLoc.xCoord][finishLoc.yCoord].setEndpoint(true);
    }


    /** Запускает приложение.  Вызывается из метода {@link #main}. **/
    private void start()
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { initGUI(); }
        });
    }


    /**
     * Этот вспомогательный метод пытается вычислить путь, используя текущее состояние карты.
     *Реализация довольно медленная; создается новый объект {@link Map2D} который
     * инициализируется из текущего состояния приложения. Затем вызывается навигатор A*,
     * и если путь найден, дисплей обновляется, чтобы показать путь, который был найден.
     *  (Лучшим решением было бы использовать шаблон проектирования контроллера представления модели.)
     **/
    private void findAndShowPath()
    {
        //Создаем 2D-объект карты, содержащий текущее состояние пользовательского ввода.

        Map2D map = new Map2D(width, height);
        map.setStart(startLoc);
        map.setFinish(finishLoc);

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                mapCells[x][y].setPath(false);

                if (mapCells[x][y].isPassable())
                    map.setCellValue(x, y, 0);
                else
                    map.setCellValue(x, y, Integer.MAX_VALUE);
            }
        }

        //Попробуем вычислить путь.  Если один из них может быть вычислен, отметим все ячейки в пути

        Waypoint wp = AStarPathfinder.computePath(map);

        while (wp != null)
        {
            Location loc = wp.getLocation();
            mapCells[loc.xCoord][loc.yCoord].setPath(true);

            wp = wp.getPrevious();
        }
    }


    /**
     *Точка входа для приложения.  В данный момент никакие аргументы командной строки не распознаются.
     **/
    public static void main(String[] args) {
        AStarApp app = new AStarApp(40, 30);
        app.start();
    }
}