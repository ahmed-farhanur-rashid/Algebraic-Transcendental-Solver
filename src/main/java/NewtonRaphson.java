import net.objecthunter.exp4j.Expression;
import javax.swing.table.DefaultTableModel;

public class NewtonRaphson {

    public static double execute(Expression expression, double a, double b, double tolerance, DefaultTableModel tableModel) {

        if (f(expression, a) * f(expression, b) > 0) return Double.NaN;

        int iteration = 0;
        double x_n; // Starting point
        double x_n_plus_1 = a; // Value inserted to keep while loop logic sound

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

               // Sending Table Data to the Table
               tableModel.addRow(new Object[]{++iteration, x_n, x_n_plus_1});

           } while (Math.abs(x_n_plus_1 - x_n) >= tolerance);
       }
       catch (ArithmeticException ex) {
           Errors.divisionByZeroError();
       }
        return x_n_plus_1; // Return the approximate root
    }

    private static double f(Expression expression, double x) {
        expression.setVariable("x", x);
        return expression.evaluate();
    }
}
