import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern; // Это для регулярных выражений


class Crawler<LINK_REGEX> {
    private HashMap<String, URLDepthPair> links = new HashMap<>();  // Это хэш-мап
    // Список всех сайтов, которые мы успешно просмотрели
    private LinkedList<URLDepthPair> pool = new LinkedList<>();     // Список ссылок

    private int depth = 0;

    public Crawler(String url, int dep) {
        depth = dep;
        pool.add(new URLDepthPair(url, 0));  // Добавление вводимой ссылки
    }

    public void run() {   // Здесь происходит вывод всех сайтов
        while (pool.size() > 0)
            parseLink(pool.pop());   // Достаем из списка пулл ссылку (последняя ссылка)

        for (URLDepthPair link : links.values())
            System.out.println(link);   // Вывод ссылок

        System.out.println();
        System.out.printf("Found %d URLS\n", links.size());  // Вывод с количеством найденных ссылок
    }

    public static Pattern LINK_REGEX = Pattern.compile(  // Регулярное выражение, ищущее в HTML коде ссылки
            "<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1"
    );

    private void parseLink(URLDepthPair link) {  // На данном этапе идет проверка ссылки на ошибки
        if (links.containsKey(link.getURL())) {
            URLDepthPair knownLink = links.get(link.getURL());
            knownLink.incrementVisited();
            return;
        }

        links.put(link.getURL(), link);

        if (link.getDepth() >= depth)
            return;
        // Проверка на ошибки
        try {
            URL url = new URL(link.getURL());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            Scanner s = new Scanner(con.getInputStream());
            while (s.findWithinHorizon(LINK_REGEX, 0) != null) {
                String newURL = s.match().group(2);
                if (newURL.startsWith("/"))
                    newURL = link.getURL() + newURL;
                else if (!newURL.startsWith("http"))
                    continue;
                URLDepthPair newLink = new URLDepthPair(newURL, link.getDepth() + 1);
                pool.add(newLink);   // Возвращение списка
            }
        } catch (Exception e) {}
    }

    public static void showHelp() {    // Проверка на то, если вдруг что не так пошло
        System.out.println("usage: java Crawler <URL> <depth>");
        System.exit(1);
    }
    public static void main(String[] args){
        Scanner scan=new Scanner(System.in);  // Ввод ссылки с клавиатуры
        String[] argg = new String[2];        // Это ссылка и глубина
        System.out.println("Enter URL: ");
        argg[0]=scan.nextLine();
        System.out.println("Enter depth: ");
        argg[1]=scan.nextLine();
        if (argg.length !=2) showHelp();      // Проверка на ошибки
        int depth = 0;
        String url=argg[0];
        try {   // Исключение на число, то есть проверка на ошибку ввода
            depth = Integer.parseInt(argg[1]);
        } catch (Exception e) {
            showHelp();
        }
        Crawler crawler = new Crawler(url, depth);
        crawler.run();
    }
}