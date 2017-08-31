public class Loop1 {
    public static void main(String[] args) {
        int sum = 0;

        for (int x = 1; x <= 10; x++) {
            if (x % 2 == 0)
                sum += x * x;
        }

        System.out.println("Sum of even numbers from 1 to 10 is " + sum);
    }
}
