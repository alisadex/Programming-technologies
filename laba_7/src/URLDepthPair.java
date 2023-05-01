//Класс для хранения ссылки и ее глубины
public class URLDepthPair {
    private String url;
    private int depth;
    private int visited;
    public URLDepthPair(String URL, int dep) {
        url = URL;
        depth=dep;
        visited=1;
    }
    public String getURL() {   //Возвращает объект класса типа URL, то есть полный путь до сайта
        return url;
    }
    public int getDepth() {    //Возвращает глубину сайта, относительно сайта введёного пользователем
        return depth;
    }
    public void incrementVisited() {
        visited++;
    }
    public String toString() {   //Возвращает строку состаящую из адреса сайта и его глубины
        return "<URL href=\"" + url + "\" visited=\"" + visited + "\" depth=\"" + depth + "\" \\>";
    }
}