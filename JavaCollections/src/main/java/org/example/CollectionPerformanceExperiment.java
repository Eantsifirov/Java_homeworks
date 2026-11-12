package org.example;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CollectionPerformanceExperiment {

    private static final int N = 10000;
    private static final int WARMUPS = 2;
    private static final int MEASUREMENTS = 5;
    private static volatile long checksum = 0;

    private static long[] measure(Supplier<List<Integer>> factory, Consumer<List<Integer>> operation, Consumer<List<Integer>> preparation) {
        for (int i = 0; i < WARMUPS; i++) {
            List<Integer> list = factory.get();

            preparation.accept(list);
            operation.accept(list);

            for (Integer value : list) {
                checksum += value;
            }
        }

        long[] times = new long[MEASUREMENTS];

        for (int j = 0; j < MEASUREMENTS; j++) {
            List<Integer> list = factory.get();

            preparation.accept(list);

            long start = System.nanoTime();
            operation.accept(list);
            long elapsed = System.nanoTime() - start;

            times[j] = elapsed;

            for (Integer value : list) {
                checksum += value;
            }
        }

        Arrays.sort(times);
        return times;
    }

    private static long median(long[] times) {
        return times[times.length / 2];
    }

    private static void printRow(String operation, long[] arrayTimes, long[] linkedTimes) {
        System.out.printf("%-28s | %7d | %14.3f | %15.3f%n", operation, N, median(arrayTimes) / 1_000_000.0, median(linkedTimes) / 1_000_000.0);
    }

    public static void main(String[] args) {
        long[] arrayToEndTimes = measure(ArrayList::new, list -> {
            for (int i = 0; i < N; i++) {
                list.add(i);
            }
        }, list -> {
        });
        long[] linkedToEndTimes = measure(LinkedList::new, list -> {
            for (int i = 0; i < N; i++) {
                list.add(i);
            }
        }, list -> {
        });

        long[] arrayToStartTimes = measure(ArrayList::new, list -> {
            for (int i = 0; i < N; i++) {
                list.add(0, i);
            }
        }, list -> {
        });
        long[] linkedToStartTimes = measure(LinkedList::new, list -> {
            for (int i = 0; i < N; i++) {
                list.add(0, i);
            }
        }, list -> {
        });

        long[] arrayToMiddleTimes = measure(ArrayList::new, list -> {
            for (int i = 0; i < N; i++) {
                list.add(list.size() / 2, i);
            }
        }, list -> {
        });
        long[] linkedToMiddleTimes = measure(LinkedList::new, list -> {
            for (int i = 0; i < N; i++) {
                list.add(list.size() / 2, i);
            }
        }, list -> {
        });

        long[] arrayGetTimes = measure(ArrayList::new, list -> {
            long sum = 0;
            for (int i = 0; i < N; i++) {
                sum += list.get(i);
            }
            checksum += sum;
        }, list -> {
            for (int i = 0; i < N; i++) {
                list.add(i);
            }
        });
        long[] linkedGetTimes = measure(LinkedList::new, list -> {
            long sum = 0;
            for (int i = 0; i < N; i++) {
                sum += list.get(i);
            }
            checksum += sum;
        }, list -> {
            for (int i = 0; i < N; i++) {
                list.add(i);
            }
        });

        long[] arrayRemoveFromStartTimes = measure(ArrayList::new, list -> {
            long sum = 0;
            for (int i = 0; i < N; i++) {
                sum += list.remove(0);
            }
            checksum += sum;
        }, list -> {
            for (int i = 0; i < N; i++) {
                list.add(i);
            }
        });
        long[] arrayRemoveFromEndTimes = measure(ArrayList::new, list -> {
            long sum = 0;
            for (int i = 0; i < N; i++) {
                sum += list.remove(list.size() - 1);
            }
            checksum += sum;
        }, list -> {
            for (int i = 0; i < N; i++) {
                list.add(i);
            }
        });
        long[] linkedRemoveFromStartTimes = measure(LinkedList::new, list -> {
            long sum = 0;
            for (int i = 0; i < N; i++) {
                sum += list.remove(0);
            }
            checksum += sum;
        }, list -> {
            for (int i = 0; i < N; i++) {
                list.add(i);
            }
        });
        long[] linkedRemoveFromEndTimes = measure(LinkedList::new, list -> {
            long sum = 0;
            for (int i = 0; i < N; i++) {
                sum += list.remove(list.size() - 1);
            }
            checksum += sum;
        }, list -> {
            for (int i = 0; i < N; i++) {
                list.add(i);
            }
        });

        System.out.println("\n" + "=".repeat(77));
        System.out.println("Checksum: " + checksum);
        System.out.println("=".repeat(77) + "\n");
        System.out.printf("%-28s | %7s | %14s | %15s%n", "Операция", "N", "ArrayList (мс)", "LinkedList (мс)");
        System.out.println("-".repeat(77));

        printRow("Добавление в конец", arrayToEndTimes, linkedToEndTimes);
        printRow("Добавление в начало", arrayToStartTimes, linkedToStartTimes);
        printRow("Вставка в середину", arrayToMiddleTimes, linkedToMiddleTimes);
        printRow("Получение по индексу", arrayGetTimes, linkedGetTimes);
        printRow("Удаление из начала", arrayRemoveFromStartTimes, linkedRemoveFromStartTimes);
        printRow("Удаление из конца", arrayRemoveFromEndTimes, linkedRemoveFromEndTimes);
    }
}
