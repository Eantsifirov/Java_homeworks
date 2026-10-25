package org.example;

public class Calculator<T extends Number> {
    public double sum(T a, T b) {
        if (a == null || b == null) {
            return Double.NaN;
        }

        double primitiveA = a.doubleValue();
        double primitiveB = b.doubleValue();

        return primitiveA + primitiveB;
    }

    public double subtract(T a, T b) {
        if (a == null || b == null) {
            return Double.NaN;
        }

        double primitiveA = a.doubleValue();
        double primitiveB = b.doubleValue();

        return primitiveA - primitiveB;
    }

    public double multiply(T a, T b) {
        if (a == null || b == null) {
            return Double.NaN;
        }

        double primitiveA = a.doubleValue();
        double primitiveB = b.doubleValue();

        return primitiveA * primitiveB;
    }

    public double divide(T a, T b) {
        if (a == null || b == null) {
            return Double.NaN;
        }

        double primitiveA = a.doubleValue();
        double primitiveB = b.doubleValue();

        if (primitiveB == 0.0) {
            return Double.NaN;
        }

        return primitiveA / primitiveB;
    }
}
