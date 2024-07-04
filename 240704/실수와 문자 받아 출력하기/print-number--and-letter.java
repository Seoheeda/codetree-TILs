import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        double a = sc.nextDouble();
        double b = sc.nextDouble();
        char c = sc.next().charAt(0);

        System.out.println(c);
        System.out.printf("%.2f", a);
        System.out.printf("5.2f", b);
    }
}