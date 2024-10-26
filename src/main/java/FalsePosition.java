import net.objecthunter.exp4j.Expression;
import javax.swing.table.DefaultTableModel;

public class FalsePosition {
    public static double execute(Expression expression, double a, double b, double tolerance, DefaultTableModel tableModel) {

        int iteration = 0;
        double mid = 0;

        double f_of_a = f(expression, a);
        double f_of_mid;
        double f_of_b = f(expression, b);

        while(Math.abs(a - b) > tolerance) {

            mid = (a * f(expression, b) - b * f(expression, a)) / (f(expression, b) - f(expression, a));
            f_of_mid = f(expression, mid);

            tableModel.addRow(new Object[]{++iteration, a, b, mid, f_of_a, f_of_b, f_of_mid});

            if(f(expression, mid) < 0) {
                a = mid;
                f_of_a = f_of_mid;
            }
            else if (f(expression, mid) > 0){
                b = mid;
                f_of_b = f_of_mid;

            }
            else {
                return mid;
            }
        }
        return mid;
    }

    private static double f(Expression expression, double x) {
        expression.setVariable("x", x);
        return expression.evaluate();
    }
}