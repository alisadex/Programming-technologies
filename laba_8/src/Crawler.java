public class Crawler {
    private String URL;
    private static int maxDepth;
    public static int CountThreads;

    public static int WaitingThreads = 0;
    public static int CountURLs = 0;

    public static int getMaxDepth() { return maxDepth; }

    public Crawler(String URL, int maxDepth, int countThreads){
        this.URL = URL;
        Crawler.maxDepth = maxDepth;
        Crawler.CountThreads = countThreads;
    }

    public void run() {
        CrawlerTask task = new CrawlerTask(new URLDepthPair(URL,0));
        task.start();
    }

    public static void main(String[] args){
        Crawler crawler = new Crawler("https://vk.com/truecrimebitch",2 ,10);   // Максимальная глубина = 2, количество потоков = 10
        crawler.run();

        Runtime.getRuntime().addShutdownHook(new Thread(()->printResult()));
    }

    private static void printResult(){
        System.out.println();
        System.out.println("Number of URLs: " + CountURLs);
    }
}