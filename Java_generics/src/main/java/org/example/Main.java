package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Task 1.\n");

        final Pair<String, Integer> pair = new Pair<>("Age", 25);
        System.out.println(pair);

        final Pair<Integer, String> swapped = pair.swap();
        System.out.println(swapped);

        final Pair<Object, Object> nullPair = new Pair<>(null, null);
        System.out.println(nullPair);

        final Pair<Object, Object> nullSwapped = nullPair.swap();
        System.out.println(nullSwapped);

        System.out.print("\n");
        System.out.println("Task 2.\n");

        final String[] names = {"Alice", "Bob", "Charlie"};
        final int index = ArrayUtils.findFirst(names, "Bob");
        System.out.println(index);

        System.out.print("\n");
        System.out.println("Task 3.\n");

        final Calculator<Integer> intCalc = new Calculator<>();
        final double result = intCalc.sum(5, 3);

        final Calculator<Double> doubleCalc = new Calculator<>();
        final double div = doubleCalc.divide(10.0, 4.0);

        System.out.print(result);
        System.out.print(" ");
        System.out.print(div + "\n");

        System.out.println("\nSuccess!");
    }
}