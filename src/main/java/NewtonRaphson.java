import net.objecthunter.exp4j.Expression;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class NewtonRaphson {

    public static double execute(Expression expression, double a, double b, double tolerance, DefaultTableModel tableModel) {

        double x_n; // Starting point
        double x_n_plus_1 = a; // Next approximation

        double h = 1e-5; // Step size for numerical derivative, 0.00001

        double f_of_x_n;
        double df_of_x_n;

       try {
           // Initialize the first iteration
           do {
               x_n = x_n_plus_1;

               f_of_x_n = f(expression, x_n); // Evaluate the function at x_n
               df_of_x_n = (f(expression, x_n + h) - f(expression, x_n - h)) / (2 * h); // Centered difference approximation for derivative

               // Update x_n_plus_1 using the Newton-Raphson formula
               x_n_plus_1 = x_n - (f_of_x_n / df_of_x_n);

           } while (Math.abs(x_n_plus_1 - x_n) > tolerance); // Continue until the function value is within tolerance
       } catch (ArithmeticException ex) {
           JOptionPane.showMessageDialog(null, "Cannot divide by zero!",
                   "Arithmetic Exception Occurred!", JOptionPane.ERROR_MESSAGE);
       }
        return x_n_plus_1; // Return the approximate root
    }

    private static double f(Expression expression, double x) {
        expression.setVariable("x", x);
        return expression.evaluate();
    }
}
