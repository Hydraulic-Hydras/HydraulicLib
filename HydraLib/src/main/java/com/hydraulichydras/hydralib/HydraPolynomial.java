package com.hydraulichydras.hydralib;

import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;

public class HydraPolynomial {
    private final ArrayList<Double> coeffs = new ArrayList<>();

    public HydraPolynomial(SimpleMatrix coeffs) {
        for (int i = 0; i < coeffs.getNumElements(); i++) {
            this.coeffs.add(coeffs.get(i));
        }
    }

    public HydraPolynomial(double... coeffs) {
        for (double coeff : coeffs) {
            this.coeffs.add(coeff);
        }
    }

    public double calculate(double x) {
        return calculate(x, coeffs.size() - 1);
    }

    public double calculate(double x, int n) {
        if (n < 0 || n >= coeffs.size()) {
            throw new IllegalArgumentException("Invalid degree for polynomial calculation");
        }

        double result = 0.0;
        double power = 1.0;

        for (int i = 0; i <= n; i++) {
            double coeff = coeffs.get(i);
            double term = coeff * power;
            result += term;
            power *= x;
        }

        return result;
    }

    @Override
    public String toString() {
        return coeffs.toString();
    }
}
