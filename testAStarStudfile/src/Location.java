/**
 * Этот класс представляет определенное местоположение на 2D-карте.
 * Координаты - это целочисленные значения.
 **/
package com.company;
import java.util.Objects;

public class Location
{
    /** Координата X этого местоположения. **/
    public int xCoord;

    /** Координата Y этого местоположения. **/
    public int yCoord;


    /** Создает новое местоположение с указанными целочисленными координатами. **/
    public Location(int x, int y)
    {
        xCoord = x;
        yCoord = y;
    }

    public Location()
    {
        this(0, 0);
    }


    /** Этот метод сравнивает определенную локацию с текущей. **/
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;

        //  instanceof - был ли объект(Х) создан на основе какого-либо класса(Y).
        if (!(obj instanceof Location)) return false;
        Location loc = (Location) obj;

        return xCoord == loc.xCoord && yCoord == loc.yCoord;
    }
    @Override
    public int hashCode()
    {
        return Objects.hash(xCoord, yCoord);
    }
}