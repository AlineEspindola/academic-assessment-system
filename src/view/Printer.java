package view;

public class Printer {

    private static final String LINE  = "═".repeat(58);
    private static final String THIN  = "─".repeat(58);

    public static void header(String title) {
        System.out.println("\n" + LINE);
        System.out.printf("  %s%n", title);
        System.out.println(LINE);
    }

    public static void section(String title) {
        System.out.println("\n" + THIN);
        System.out.printf("  %s%n", title);
        System.out.println(THIN);
    }

    public static void info(String label, String value) {
        System.out.printf("  %-28s %s%n", label + ":", value);
    }

    public static void grade(String label, double value) {
        System.out.printf("  %-28s %.2f%n", label + ":", value);
    }

    public static void success(String msg) {
        System.out.println("  ✔  " + msg);
    }

    public static void warn(String msg) {
        System.out.println("  ⚠  " + msg);
    }

    public static void error(String msg) {
        System.out.println("  ✖  " + msg);
    }

    public static void blank() {
        System.out.println();
    }

    public static void menu(String... options) {
        for (int i = 0; i < options.length; i++) {
            System.out.printf("  [%d] %s%n", i + 1, options[i]);
        }
        System.out.print("\n  Opção: ");
    }
}
