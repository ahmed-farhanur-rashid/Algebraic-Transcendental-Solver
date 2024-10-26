import net.objecthunter.exp4j.Expression;
import javax.swing.table.DefaultTableModel;

public class FalsePosition {
    public static double execute(Expression expression, double a, double b, double tolerance, DefaultTableModel tableModel) {

        final int maxIterations = 100;
        int iteration = 0;
        double prev_mid;
        double mid = 0;
        double f_of_mid;

        double f_of_a = f(expression, a);
        double f_of_b = f(expression, b);

        if (f_of_a * f_of_b > 0) return Double.NaN;

        do {
            prev_mid = mid;
            mid = (a * f_of_b - b * f_of_a) / (f_of_b - f_of_a);
            f_of_mid = f(expression, mid);

            tableModel.addRow(new Object[]{++iteration, a, b, mid, f_of_a, f_of_b, f_of_mid});

            if(f_of_mid < 0) {
                a = mid;
                f_of_a = f_of_mid;
            }
            else if (f_of_mid > 0){
                b = mid;
                f_of_b = f_of_mid;
            }
            else {
                return mid;
            }

        } while (Math.abs(prev_mid - mid) > tolerance && iteration < maxIterations);

        if (iteration == maxIterations) {
            Errors.iterationLimitError();
        }

        return mid;
    }

    private static double f(Expression expression, double x) {
        expression.setVariable("x", x);
        return expression.evaluate();
    }
}