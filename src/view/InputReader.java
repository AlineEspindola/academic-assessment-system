package view;

import java.util.Scanner;

public class InputReader {
    private final Scanner scanner;

    public InputReader() {
        this.scanner = new Scanner(System.in);
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                Printer.error("Entrada inválida. Digite um número inteiro.");
            }
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim().replace(",", ".");
            try {
                double v = Double.parseDouble(line);
                if (v < 0.0 || v > 10.0) {
                    Printer.error("Nota deve estar entre 0.0 e 10.0.");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                Printer.error("Entrada inválida. Digite um número (ex: 7.5).");
            }
        }
    }

    public int readMenuOption(int max) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v >= 1 && v <= max) return v;
                Printer.error("Digite uma opção entre 1 e " + max + ".");
                System.out.print("  Opção: ");
            } catch (NumberFormatException e) {
                Printer.error("Entrada inválida.");
                System.out.print("  Opção: ");
            }
        }
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public void close() {
        scanner.close();
    }
}
