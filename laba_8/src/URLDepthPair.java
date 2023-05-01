public class URLDepthPair {  // Класс для пары ссылки и глубины
    private String URL;
    private int depth;

    public int getDepth()  { return depth; }
    public String getURL() { return URL; }

    public URLDepthPair(String URL, int depth){
        this.URL = URL;
        this.depth = depth;
    }

    @Override
    /**
     * Метод, необходимый для вывода пары ссылка-глубина на экран
     */
    public String toString() {
        return "depth: " + depth + " URL: ["+ URL + "]";
    }
}