/**
 * Этот класс представляет собой простую двумерную карту, состоящую из квадратных ячеек.
 * Каждая ячейка определяет стоимость обхода этой ячейки.
 **/
package com.company;
public class Map2D
{
    /** Ширина карты. **/
    private int width;

    /** Высота карты. **/
    private int height;

    /**
     * Фактические картографические данные, необходимые алгоритму поиска пути для навигации.
     **/
    private int[][] cells;

    /** Начальное местоположение для выполнения поиска пути A*. **/
    private Location start;

    /** Конечное местоположение для выполнения поиска пути A*. **/
    private Location finish;


    /** Создание новой 2D-карты с заданными шириной и высотой. **/
    public Map2D(int width, int height)
    {
        if (width <= 0 || height <= 0)
        {
            throw new IllegalArgumentException(
                    "Ширина и высота должны быть положительными значениями; Получено " + width +
                            "x" + height);
        }

        this.width = width;
        this.height = height;

        cells = new int[width][height];

        //Составим несколько координат для старта и финиша.
        start = new Location(0, height / 2);
        finish = new Location(width - 1, height / 2);
    }


    /**
     * Этот вспомогательный метод проверяет указанные координаты, чтобы увидеть,
     * находятся ли они в пределах границ карты.
     * Если координаты не находятся на карте, то метод выдает исключение
     *  <code>IllegalArgumentException</code>.
     **/
    private void checkCoords(int x, int y)
    {
        if (x < 0 || x > width)
        {
            throw new IllegalArgumentException("x должен быть в пределах [0, " +
                    width + "), Получено  " + x);
        }

        if (y < 0 || y > height)
        {
            throw new IllegalArgumentException("y должен быть в пределах [0, " +
                    height + "), Получено " + y);
        }
    }

    /** Возвращает ширину карты. **/
    public int getWidth()
    {
        return width;
    }

    /** Возвращает высоту карты. **/
    public int getHeight()
    {
        return height;
    }

    /**
     * Возвращает значение true, если указанные координаты содержатся в пределах области карты.
     **/
    public boolean contains(int x, int y)
    {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }


    /**Возвращает значение true, если местоположение содержится в пределах области карты. **/
    public boolean contains(Location loc)
    {
        return contains(loc.xCoord, loc.yCoord);
    }

    /** Returns the stored cost value for the specified cell. **/
    public int getCellValue(int x, int y)
    {
        checkCoords(x, y);
        return cells[x][y];
    }

    /** Возвращает сохраненное значение стоимости для указанной ячейки. **/
    public int getCellValue(Location loc)
    {
        return getCellValue(loc.xCoord, loc.yCoord);
    }

    /** Задает значение себестоимости для указанной ячейки. **/
    public void setCellValue(int x, int y, int value)
    {
        checkCoords(x, y);
        cells[x][y] = value;
    }

    /**
     * Возвращает начальное местоположение для карты.
     * Именно отсюда будет начинаться сгенерированный путь.
     **/
    public Location getStart()
    {
        return start;
    }

    /**
     * Задает начальное местоположение для карты.
     * Именно отсюда будет начинаться сгенерированный путь.
     **/
    public void setStart(Location loc)
    {
        if (loc == null)
            throw new NullPointerException("Местоположение не может быть нулевым");

        start = loc;
    }

    /**
     * Возвращает конечное местоположение для карты.
     * На этом сгенерированный путь завершится.
     **/
    public Location getFinish()
    {
        return finish;
    }

    /**
     * Возвращает конечное местоположение для карты.
     * На этом сгенерированный путь завершится.
     **/
    public void setFinish(Location loc)
    {
        if (loc == null)
            throw new NullPointerException("Местоположение не может быть нулевым");

        finish = loc;
    }
}