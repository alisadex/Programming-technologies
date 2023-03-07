
public class Primes {
    public static boolean isPrime(int n)
    {
        boolean isPrime = true;
        for(int i = 2; i <= n/i;i++)
        {
            if ((n % i) == 0) isPrime = false;
        }
        return isPrime;
    }
    public static void main(String args[]) {
        for (int i = 2; i < 100; i++) {
            if (isPrime(i))
                System.out.println(i + " - Prime number.");
        }
    }
}