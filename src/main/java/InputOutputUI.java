import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class InputOutputUI extends JFrame {

    // Text fields for input (4)
    private JTextField functionField;
    private JTextField aField;
    private JTextField bField;
    private JTextField toleranceField;

    // Radio buttons for selecting calculation methods (3)
    private JRadioButton bisectionRadio;
    private JRadioButton falsePositionRadio;
    private JRadioButton newtonRaphsonRadio;

    // Checkbox for automating finding process for [a,b] (1)
    private JCheckBox autoFind_AB_Checkbox;

    // Calculate Button and Root (2)
    private JButton calculateButton;
    private JLabel resultLabel;

    // Table to show iterations (2)
    private JTable table;
    private DefaultTableModel tableModel;

    // Constructor
    public InputOutputUI() { // Changed this line

        // Frame Setup
        this.setTitle("Algebraic & Transcendental Equation Solver (Beta 0.1)");
        this.setSize(750, 750);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Input Box Creation & Border Addition
        JPanel inputPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        functionField = new JTextField();
        aField = new JTextField();
        bField = new JTextField();
        toleranceField = new JTextField();

        autoFind_AB_Checkbox = new JCheckBox("Automatically find interval [a, b]");

        bisectionRadio = new JRadioButton("Bisection Method");
        falsePositionRadio = new JRadioButton("False Position Method");
        newtonRaphsonRadio = new JRadioButton("Newton-Raphson Method");

        ButtonGroup methodGroup = new ButtonGroup();
        methodGroup.add(bisectionRadio);
        methodGroup.add(falsePositionRadio);
        methodGroup.add(newtonRaphsonRadio);

        calculateButton = new JButton("Calculate Root");
        calculateButton.addActionListener(new calculateButtonListener());

        resultLabel = new JLabel("Root: ");

        // Adding components To JPanel
        inputPanel.add(new JLabel("Enter f(x):"));
        inputPanel.add(functionField);
        inputPanel.add(new JLabel("Enter Lower Bound (a):"));
        inputPanel.add(aField);
        inputPanel.add(new JLabel("Enter Upper Bound (b):"));
        inputPanel.add(bField);
        inputPanel.add(new JLabel("Enter Tolerance:"));
        inputPanel.add(toleranceField);

        inputPanel.add(new JLabel(""));
        inputPanel.add(autoFind_AB_Checkbox);

        inputPanel.add(new JLabel("Select Calculation Method:"));
        inputPanel.add(bisectionRadio);

        inputPanel.add(new JLabel(""));
        inputPanel.add(falsePositionRadio);

        inputPanel.add(new JLabel(""));
        inputPanel.add(newtonRaphsonRadio);

        inputPanel.add(calculateButton);
        inputPanel.add(resultLabel);

        // Adding the input panel to the top of the BorderLayout
        this.add(inputPanel, BorderLayout.NORTH);

        // Setting up the table for further manipulation
        //String[] columnNames = {"Iteration", "Lower Bound (a)", "Upper Bound (b)", "Midpoint", "f(a)", "f(b)", "f(Midpoint)"};
        //tableModel = new DefaultTableModel(columnNames, 0);
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        // Created a scrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private class calculateButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String stringExpression = functionField.getText();
                Expression expression = new ExpressionBuilder(stringExpression).variables("x").build();

                // Code snippet to find interval [a, b]
                double a = 0;
                double b = 1;
                boolean found = false;

                while (b <= 10000) {
                    if ((f(expression, a) * f(expression, b)) < 0) {
                        found = true;
                        break;
                    } else {
                        a = b;
                        b++;
                    }
                }

                if (found) {
                    aField.setText(String.valueOf(a));
                    bField.setText(String.valueOf(b));
                } else {
                    JOptionPane.showMessageDialog(null, "Couldn't find upper and lower bound [a, b] in range [0, 10K].\nPlease input manually.",
                            "An Unexpected Error Occurred", JOptionPane.ERROR_MESSAGE);
                }
                // End of code snippet

                double tolerance = Double.parseDouble(toleranceField.getText());

                // Clearing previous table data
                tableModel.setRowCount(0);

                double root = 0;

                tableModel = new DefaultTableModel();

                if (bisectionRadio.isSelected()) {
                    root = Bisection.execute(expression, a, b, tolerance, tableModel);
                } else if (falsePositionRadio.isSelected()) {
                    root = FalsePosition.execute(expression, a, b, tolerance, tableModel);
                } else if (newtonRaphsonRadio.isSelected()) {
                    root = NewtonRaphson.execute(expression, a, b, tolerance, tableModel);
                }

                resultLabel.setText("Root: " + (Double.isNaN(root) ? "No root in interval" : root));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Please check input and try again.",
                        "An Unexpected Error Occurred", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static double f(Expression expression, double x) {
        expression.setVariable("x", x);
        return expression.evaluate();
    }
}