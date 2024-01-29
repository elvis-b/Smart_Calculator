import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void main(String[] args) {
        start();
    }
    public static void start() {
        Scanner scanner = new Scanner(System.in);

        SmartCalculator smartCalculator = new SmartCalculator();

        while (true) {
            var input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                if (input.startsWith("/")) {
                    if ("/help".contains(input)) {
                        smartCalculator.help();
                    } else if ("/exit".contains(input)) {
                        smartCalculator.exit();
                        return;
                    } else {
                        System.out.println("Unknown command");
                    }
                } else {
                    input = input.replaceAll("\\s+", "")
                            .replaceAll("\\+\\++|(--)+", "+")
                            .replaceAll("\\+-", "-");
                    smartCalculator.calculate(input);
                }
            }
        }
    }
}