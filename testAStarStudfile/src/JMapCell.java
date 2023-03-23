package com.company;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;


/**
 * Этот класс является пользовательским компонентом Swing для
 * представления одной ячейки карты на 2D-карте.
 * Ячейка имеет несколько различных типов состояний, но самое основное
 * состояние заключается в том, является ли ячейка проходимой или нет.
 */
public class JMapCell extends JComponent
{
    private static final Dimension CELL_SIZE = new Dimension(12, 12);

    /** Значение True указывает, что ячейка является конечной точкой, либо начальной, либо финишем. **/
    boolean endpoint = false;


    /** Значение True указывает на то, что ячейка проходима; значение false означает, что это не так. **/
    boolean passable = true;

    /**
     * Значение True указывает, что эта ячейка является частью пути между началом и концом.
     **/
    boolean path = false;

    /**
     * Создадим новую ячейку карты с указанной "возможностью"
     * Значение true означает, что ячейка проходима.
     **/
    public JMapCell(boolean pass)
    {
        // Установим предпочтительный размер ячейки, чтобы задать начальный размер окна.
        setPreferredSize(CELL_SIZE);

        setPassable(pass);
    }

    /** Создадим новую ячейку карты, которая по умолчанию является проходимой. **/
    public JMapCell()
    {
        // Вызовите другой конструктор, указав значение true для "passable".
        this(true);
    }

    /** Помечает эту ячейку либо как начальную, либо как конечную. **/
    public void setEndpoint(boolean end)
    {
        endpoint = end;
        updateAppearance();
    }

    /**
     * Устанавливает для этой ячейки значение "проходимый" или "непроходимый".
     *Ввод значения true помечает ячейку как проходимую; ввод значения false помечает ее как непроходимую.
     **/
    public void setPassable(boolean pass)
    {
        passable = pass;
        updateAppearance();
    }

    /**Возвращает true, если эта ячейка проходима, или false в противном случае. **/
    public boolean isPassable()
    {
        return passable;
    }

    /** Переключает текущее "проходимое" состояние ячейки карты. **/
    public void togglePassable()
    {
        setPassable(!isPassable());
    }

    /** Помечает эту ячейку как часть пути, обнаруженного алгоритмом A*. **/
    public void setPath(boolean path)
    {
        this.path = path;
        updateAppearance();
    }

    /**
     * Этот вспомогательный метод обновляет цвет фона, чтобы он соответствовал
     * текущему внутреннему состоянию ячейки.
     **/
    private void updateAppearance()
    {
        if (passable)
        {
            // Проходимая ячейка. Укажите его состояние с помощью границы.
            setBackground(Color.WHITE);

            if (endpoint)
                setBackground(Color.CYAN);
            else if (path)
                setBackground(Color.GREEN);
        }
        else
        {
            // Непроходимая клетка.  Сделаем ее красным.
            setBackground(Color.RED);
        }
    }

    /**
     * Реализация метода paint для нанесения цвета фона в ячейку карты.
     **/
    protected void paintComponent(Graphics g)
    {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}