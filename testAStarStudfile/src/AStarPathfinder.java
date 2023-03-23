package com.company;
import java.util.HashMap;
import java.util.HashSet;



/**
 * Этот класс содержит реализацию алгоритма поиска пути A*.
 */
public class AStarPathfinder
{
    /**
     * Эта константа содержит максимальное ограничение для стоимости путей.  Если
     * случается, что конкретная путевая точка превышает этот предел затрат, то путевая точка является отвергнутой.
     **/
    public static final float COST_LIMIT = 1e6f;


    /**
     * Пытается вычислить путь, который позволяет перемещаться между
     * начальным и конечным местоположениями указанной карты.
     * Если путь может быть найден, возвращается путевая точка последнего шага пути;
     * эта путевая точка может быть использована для обратного перехода к начальной точке.
     * Если путь не может быть найден, возвращается null.
     **/
    public static Waypoint computePath(Map2D map)
    {
        // Переменные, необходимые для поиска A*.
        AStarState state = new AStarState(map);
        Location finishLoc = map.getFinish();

        // Установка начальной путевой точки, чтобы начать поиск A*.
        Waypoint start = new Waypoint(map.getStart(), null);
        start.setCosts(0, estimateTravelCost(start.getLocation(), finishLoc));
        state.addOpenWaypoint(start);

        Waypoint finalWaypoint = null;
        boolean foundPath = false;

        while (!foundPath && state.numOpenWaypoints() > 0)
        {
            // Найдите "лучшую" (т.е. самую дешевую) точку маршрута на данный момент.
            Waypoint best = state.getMinOpenWaypoint();

            //Если лучшее место - это место финиша, то мы закончили!
            if (best.getLocation().equals(finishLoc))
            {
                finalWaypoint = best;
                foundPath = true;
            }

            // Добавим /обновим всех соседей текущего наилучшего местоположения.
            // Это эквивалентно попытке выполнить все "следующие шаги" из этого местоположения.
            takeNextStep(best, state);

            // Наконец, переместим это местоположение из списка "открыто" в список "закрыто".
            state.closeWaypoint(best.getLocation());
        }

        return finalWaypoint;
    }

    /**
     Этот статический вспомогательный метод принимает путевую точку и генерирует все допустимые
     "следующие шаги" из этой путевой точки. Новые путевые точки добавляются в коллекцию
     "открытые путевые точки" переданного объекта A* state.
     **/
    private static void takeNextStep(Waypoint currWP, AStarState state)
    {
        Location loc = currWP.getLocation();
        Map2D map = state.getMap();

        for (int y = loc.yCoord - 1; y <= loc.yCoord + 1; y++)
        {
            for (int x = loc.xCoord - 1; x <= loc.xCoord + 1; x++)
            {
                Location nextLoc = new Location(x, y);

                // Если "следующее местоположение" находится за пределами карты, пропустим его.
                if (!map.contains(nextLoc))
                    continue;

                // Если "следующее местоположение" - это текущее местоположение, пропустим его.
                if (nextLoc == loc)
                    continue;

                // Если это местоположение уже находится в "закрытом" наборе,
                // то переходите к следующему местоположению.
                if (state.isLocationClosed(nextLoc))
                    continue;

                // Укажем точку маршрута для этого "следующего местоположения".

                Waypoint nextWP = new Waypoint(nextLoc, currWP);

                // Хорошо, мы жульничаем и используем смету затрат для вычисления фактической стоимости из предыдущей ячейки.
                // Затем мы добавляем стоимость из ячейки карты, в которую мы заходим, чтобы включить барьеры и т.д.

                float prevCost = currWP.getPreviousCost() +
                        estimateTravelCost(currWP.getLocation(),
                                nextWP.getLocation());

                prevCost += map.getCellValue(nextLoc);

                // Пропустим это "следующее местоположение", если это слишком дорого.
                if (prevCost >= COST_LIMIT)
                    continue;

                nextWP.setCosts(prevCost,
                        estimateTravelCost(nextLoc, map.getFinish()));

                // Добавьте путевую точку к набору открытых путевых точек.
                // Если для этого местоположения уже существует путевая точка,
                // новая путевая точка заменяет старую путевую точку только в том случае,
                // если она дешевле старой.
                state.addOpenWaypoint(nextWP);
            }
        }
    }

    /**
     * Оценивает стоимость проезда между двумя указанными пунктами.
     * Рассчитанная фактическая стоимость - это просто расстояние по прямой между двумя точками.
     **/
    private static float estimateTravelCost(Location currLoc, Location destLoc)
    {
        int dx = destLoc.xCoord - currLoc.xCoord;
        int dy = destLoc.yCoord - currLoc.yCoord;

        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}