import net.objecthunter.exp4j.Expression;
import javax.swing.table.DefaultTableModel;

public class NewtonRaphson {

    public static double execute(Expression expression, double a, double b, double tolerance, DefaultTableModel tableModel) {

        if (f(expression, a) * f(expression, b) > 0) return Double.NaN;

        final int maxIterations = 101;
        int iteration = 0;
        double x_n; // Starting point
        double x_n_plus_1 = a; // Value inserted to keep while loop logic sound

        double h = 1e-5; // Step size for numerical derivative, 0.00001

        double f_of_x_n;
        double df_of_x_n;

        try {
            do {
                x_n = x_n_plus_1;

                // Evaluate the function at x_n
                // Centered difference approximation for derivative

                f_of_x_n = f(expression, x_n);
                df_of_x_n = (f(expression, x_n + h) - f(expression, x_n - h)) / (2 * h);

                x_n_plus_1 = x_n - (f_of_x_n / df_of_x_n); // Update x_n_plus_1 using Newton-Raphson formula

                tableModel.addRow(new Object[]{++iteration, x_n, x_n_plus_1});
            } while (Math.abs(x_n_plus_1 - x_n) >= tolerance && iteration < maxIterations);

            if (iteration == maxIterations) {
                Errors.iterationLimitError();
            }
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
