import net.objecthunter.exp4j.Expression;

public class ExpressionEvaluator {

    public static double f(Expression expression, double x) {
        expression.setVariable("x", x);
        return expression.evaluate();
    }
}
