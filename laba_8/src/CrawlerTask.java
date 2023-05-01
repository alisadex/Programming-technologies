import java.net.*;
import java.util.Scanner;

public class CrawlerTask extends Thread {
    private URLPool pool;

    public CrawlerTask(URLDepthPair link) {
        pool = new URLPool();  // Это объект
        pool.addLink(link);    // Добавление ссылки
    }
    // Здесь идет переопределение метода
    @Override
    public void run() {
        URLDepthPair link = pool.getLink();
        System.out.println(link.toString());   // Здесь вывод ссылки
        System.out.println(Thread.activeCount());  // Выводится количество активных потоков
        Crawler.CountURLs++;
        if(link.getDepth() == Crawler.getMaxDepth()) return;  // Идет проверка на ошибку и если глубина = максимальной глубине, то выходим

        findLinks(link);
    }

    private void findLinks(URLDepthPair link)
    {
        try {   // Проверка ссылки
            URL url = new URL(link.getURL());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            Scanner scanner = new Scanner(connection.getInputStream());

            while (scanner.findWithinHorizon("<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1", 0) != null) {
                String newURL = scanner.match().group(2);
                URLDepthPair newLink =  createNewLink(newURL, link);
                if (newLink == null) continue;
                CreateNewThread(newLink);  // Создаем поток
            }
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private URLDepthPair createNewLink(String newURL, URLDepthPair link){
        if (newURL.startsWith("/")) {
            newURL = link.getURL() + newURL;
        }
        else if (!newURL.startsWith("https")) return null; // Проверка

        return new URLDepthPair(newURL, link.getDepth() + 1);
    }

    private void CreateNewThread(URLDepthPair link)  {   // Создаём поток для сcылки
        CrawlerTask task = new CrawlerTask(link);
        task.start();
    }
}