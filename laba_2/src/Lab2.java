import java.util.Scanner;
public class Lab2 {
    // метод ввода координат точек с клавиатуры
    public static void pointInput(Point3d point1, Scanner scanner) {
        System.out.print("Введите значение координаты X:");
        int x1 = scanner.nextInt();
        point1.setX(x1);

        System.out.print("Введите значение координаты Y:");
        int y1 = scanner.nextInt();
        point1.setY(y1);

        System.out.print("Введите значение координаты Z:");
        int z1 = scanner.nextInt();
        point1.setZ(z1);
    }
    public static void main(String[] args) {
        // объявление сканнера для ввода с клавиатуры
        Scanner scanner = new Scanner(System.in);

        // создание трех точек пространства
        Point3d obj1 = new Point3d();
        Point3d obj2 = new Point3d();
        Point3d obj3 = new Point3d();

        // ввод координат точек
        System.out.println("Первая точка");
        pointInput(obj1, scanner);
        System.out.println("Вторая точка");
        pointInput(obj2, scanner);
        System.out.println("Третья точка");
        pointInput(obj3, scanner);

        // вывод значения площади треугольнка
        double area = computeArea(obj1, obj2, obj3);

        if (area == 0) {
            System.out.println("Ошибка! По заданным координатам невозможно составить треугольник.");
        } else {
            System.out.print("Площадь треугольника = ");
            System.out.print(String.format("%2.2f", area));
        }

        scanner.close();
    }
    // метод вычисления площади треугольника
    public static double computeArea(Point3d point1, Point3d point2, Point3d point3) {
        // вычисления длин сторон треугольника
        double a = Point3d.toDistance(point1, point2);
        double b = Point3d.toDistance(point2, point3);
        double c = Point3d.toDistance(point3, point1);

        // формула Герона
        double p = (a + b + c) / 2;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }
}
