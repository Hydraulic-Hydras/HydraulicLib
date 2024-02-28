package com.hydraulichydras.hydralib;

import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;

/**
 * Represents and manipulates polynomial functions.
 */
public class HydraPolynomial {

    // List to store the coefficients of the polynomial
    private final ArrayList<Double> coeffs = new ArrayList<>();

    // Constructor to initialize the polynomial with coefficients from a matrix
    public HydraPolynomial(SimpleMatrix coeffs) {
        for (int i = 0; i < coeffs.getNumElements(); i++) {
            this.coeffs.add(coeffs.get(i));
        }
    }

    // Constructor to initialize the polynomial with coefficients provided as arguments
    public HydraPolynomial(double... coeffs) {
        for (double coeff : coeffs) {
            this.coeffs.add(coeff);
        }
    }

    // Method to evaluate the polynomial at a given x value
    public double calculate(double x) {
        // By default, calculate the polynomial up to the highest degree
        return calculate(x, coeffs.size() - 1);
    }

    // Method to evaluate the polynomial up to a given degree at a given x value
    public double calculate(double x, int n) {
        // Check if the degree is valid
        if (n < 0 || n >= coeffs.size()) {
            throw new IllegalArgumentException("Invalid degree for polynomial calculation");
        }

        double result = 0.0;
        double power = 1.0;

        // Calculate the polynomial value using Horner's method
        for (int i = 0; i <= n; i++) {
            double coeff = coeffs.get(i);
            double term = coeff * power;
            result += term;
            power *= x;
        }

        return result;
    }

    // Method to represent the polynomial as a string
    @Override
    public String toString() {
        return coeffs.toString();
    }
}
