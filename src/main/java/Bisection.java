import net.objecthunter.exp4j.Expression;
import javax.swing.table.DefaultTableModel;

public class Bisection {
    public static double execute(Expression expression, double a, double b, double tolerance, DefaultTableModel tableModel) {

        if (f(expression, a) * f(expression, b) > 0) return Double.NaN;

        final int maxIterations = 101;
        int iteration = 0;
        double mid;
        double f_of_mid;

        do {
            mid = a + (b - a) / 2; // Same as mid = (a + b) / 2 but is more error free
            f_of_mid = f(expression, mid);

            tableModel.addRow(new Object[]{++iteration, a, b, mid, f_of_mid});

            if(f_of_mid < 0) {
                a = mid;
            }
            else if (f_of_mid > 0){
                b = mid;
            }
            else {
                return mid;
            }
        } while(Math.abs(a - b) >= tolerance && iteration < maxIterations);

        if (iteration >= maxIterations) {
            Errors.iterationLimitError();
        }

        return mid;
    }

    private static double f(Expression expression, double x) {
        expression.setVariable("x", x);
        return expression.evaluate();
    }
}