import javax.swing.*;

public class Errors {

    public static void noIntervalError() {
        JOptionPane.showMessageDialog(null,
                "Couldn't find upper and lower bound [a, b] in range [-5000, 5000]." +
                        " The equation could have complex roots." +
                        "\nPlease input manually!",
                "An Unexpected Errors Occurred", JOptionPane.ERROR_MESSAGE);
    }

    public static void exceptionError () {
        JOptionPane.showMessageDialog(null, """
                        Please check input and try again.\
                        
                        Do not leave relevant field empty\
                        
                        Do not use special characters in number fields as input.""",
                "An Expected Errors Occurred", JOptionPane.ERROR_MESSAGE);

        // Above "Message" was auto replaced by a "text block" by IntelliJ, I have no idea what I'm doing at this point.
    }

    public static void iterationLimitError () {
        JOptionPane.showMessageDialog(null, "Maximum iterations limit exceeded!",
                "Iteration Limit Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void divisionByZeroError () {
        JOptionPane.showMessageDialog(null, "Cannot divide by zero!",
                "Division By Zero Error", JOptionPane.ERROR_MESSAGE);
    }
}
