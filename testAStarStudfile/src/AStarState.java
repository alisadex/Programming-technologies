package com.company;
import java.util.HashMap;
public class AStarState
        /**
         * Этот класс хранит базовое состояние, необходимое алгоритму A* для вычисления пути по карте.
         * Это состояние включает в себя коллекцию "открытых путевых точек"
         * и другую коллекцию "закрытых путевых точек". Кроме того,
         * этот класс предоставляет основные операции, необходимые
         * алгоритму поиска пути A* для выполнения его обработки.
         **/
{
    /** Это ссылка на карту, по которой перемещается алгоритм A*. **/
    private Map2D map;

    /** Коллекции открытых путевых точек **/
    private HashMap<Location, Waypoint> openWaypoints;

    /** Коллекции закрытых путевых точек **/
    private HashMap<Location, Waypoint> closedWaypoints;
    /**
     * Инициализируем новый объект состояния для использования алгоритма поиска пути A*.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("Карта не может быть нулевой");

        this.map = map;

        openWaypoints = new HashMap<Location, Waypoint>();
        closedWaypoints = new HashMap<Location, Waypoint>();
    }

    /** Возвращает карту, по которой перемещается навигатор A*. **/
    public Map2D getMap()
    {
        return map;
    }

    /**
     * Этот метод должен проверить все вершины в наборе открытых вершин
     * и после этого он должен вернуть ссылку на вершину с наименьшей
     * общей стоимостью. Если в "открытом" наборе нет вершин, функция возвращает NULL.
     **/
    public Waypoint getMinOpenWaypoint()
    {
        Waypoint sol = null;

        float min = Float.POSITIVE_INFINITY;
        float totalCost = 0;

        for(Waypoint p : openWaypoints.values())
        {
            totalCost = p.getTotalCost();

            if(min > totalCost)
            {
                min = totalCost;
                sol = p;
            }
        }

        return sol;
    }

    /**
     * Данный метод должен добавлять указанную вершину только в том
     * случае, если существующая вершина хуже новой.
     * Если в наборе «открытых вершин» в настоящее время нет вершины
     * для данного местоположения, то необходимо просто добавить новую вершину
     * Если в наборе «открытых вершин» уже есть вершина для этой
     * локации, добавьте новую вершину только в том случае, если стоимость пути до
     * новой вершины меньше стоимости пути до текущей.
     * Т. Е. - если путь через новую вершину короче, чем путь через текущую вершину,
     * замените текущую вершину на новую
     **/
    public boolean addOpenWaypoint(Waypoint newWP)
    {
        Waypoint other = openWaypoints.get(newWP.getLocation());

        if(other == null || newWP.getPreviousCost() < other.getPreviousCost())
        {
            openWaypoints.put(newWP.getLocation(), newWP);
            return true;
        }

        return false;
    }


    /** Возвращает текущее количество открытых путевых точек. **/
    public int numOpenWaypoints()
    {
        return openWaypoints.size();
    }


    /**
     * Эта функция перемещает вершину из набора «открытых вершин» в набор «закрытых вершин»
     **/
    public void closeWaypoint(Location loc)
    {
        Waypoint point = openWaypoints.remove(loc);

        if(point != null) {
            closedWaypoints.put(loc, point);
        }
    }

    /**
     * Эта функция должна возвращать значение true, если указанное
     * местоположение встречается в наборе закрытых вершин,
     * и false в противном случае.
     **/
    public boolean isLocationClosed(Location loc)
    {
        return closedWaypoints.containsKey(loc);
    }
}
