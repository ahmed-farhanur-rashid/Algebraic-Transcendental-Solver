import net.objecthunter.exp4j.Expression;

public class IntervalFinder {

    public static Interval findInterval (Expression expression) {

            double a = 0.0;
            double b = 1.0;
            boolean foundInterval = false;

            while (b <= 5000) {
                if (f(expression, a) * f(expression, b) < 0) {
                    foundInterval = true; // Found an interval
                    break;
                }
                a = b;
                b++;
            }

            // If no interval found, search in the negative range
            if (!foundInterval) {
                a = -5000;
                b = -4999;

                while (b < 0) {
                    if (f(expression, a) * f(expression, b) < 0) {
                        foundInterval = true; // Found an interval
                        break;
                    }
                    a = b;
                    b++;
                }
            }

            if (foundInterval) {
                return new Interval(a, b);
            }
            else
            {
                return new Interval(Double.NaN, Double.NaN);
            }
    }

    private static double f(Expression expression, double x) {
        expression.setVariable("x", x);
        return expression.evaluate();
    }
}

