// Standard Version Number Format --- MAJOR.MINOR.PATCH
// Remove suppress warnings when debugging

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class InputOutputUI extends JFrame {

    // Text fields for input (4)
    private final JTextField functionField;
    private final JTextField toleranceField;
    private final JTextField aField;
    private final JTextField bField;

    // Radio buttons for selecting calculation methods (3)
    private final JRadioButton bisectionRadio;
    private final JRadioButton falsePositionRadio;
    private final JRadioButton newtonRaphsonRadio;

    // Checkbox for automating finding process for [a,b] (1)
    @SuppressWarnings("FieldCanBeLocal")
    private final JCheckBox autoFind_AB_Checkbox;

    // Calculate Button and Root (2)
    @SuppressWarnings("FieldCanBeLocal")
    private final JButton calculateButton;
    private final JLabel resultLabel;
    @SuppressWarnings("FieldCanBeLocal")
    private final JButton clearButton;

    // Table to show iterations (2)
    private final JTable table;
    private DefaultTableModel tableModel;

    // Constructor
    public InputOutputUI() { // Changed this line

        // Frame Setup
        this.setTitle("Algebraic & Transcendental Equation Solver (Stable Release 1.0.0)");
        this.setSize(750, 750);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Input Box Creation & Border Addition
        JPanel inputPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel shortPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        functionField = new JTextField();
        toleranceField = new JTextField();
        aField = new JTextField();
        bField = new JTextField();

        autoFind_AB_Checkbox = new JCheckBox("Automatically find interval [a, b]");

        bisectionRadio = new JRadioButton("Bisection Method");
        falsePositionRadio = new JRadioButton("False Position Method");
        newtonRaphsonRadio = new JRadioButton("Newton-Raphson Method");

        ButtonGroup methodGroup = new ButtonGroup();
        methodGroup.add(bisectionRadio);
        methodGroup.add(falsePositionRadio);
        methodGroup.add(newtonRaphsonRadio);

        calculateButton = new JButton("Calculate Root");
        calculateButton.addActionListener(new CalculateButtonListener());

        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ClearButtonListener());

        resultLabel = new JLabel("Root: ");

        // Adding components To JPanel
        inputPanel.add(new JLabel("Enter f(x):"));
        inputPanel.add(functionField);
        inputPanel.add(new JLabel("Enter Tolerance:"));
        inputPanel.add(toleranceField);
        inputPanel.add(new JLabel("Enter Lower Bound (a):"));
        inputPanel.add(aField);
        inputPanel.add(new JLabel("Enter Upper Bound (b):"));
        inputPanel.add(bField);

        inputPanel.add(new JLabel(""));
        inputPanel.add(autoFind_AB_Checkbox);

        inputPanel.add(new JLabel("Select Calculation Method:"));
        inputPanel.add(bisectionRadio);

        inputPanel.add(new JLabel(""));
        inputPanel.add(falsePositionRadio);

        inputPanel.add(new JLabel(""));
        inputPanel.add(newtonRaphsonRadio);

        inputPanel.add(calculateButton);

        shortPanel.add(resultLabel);
        shortPanel.add(clearButton);

        inputPanel.add(shortPanel);

        // Adding the input panel to the top of the BorderLayout
        this.add(inputPanel, BorderLayout.NORTH);

        // Table shenanigans
        tableModel = new DefaultTableModel(); // Dummy table model, needed to create table properly.
        table = new JTable(tableModel);

        // Created a scrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private class CalculateButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String stringExpression = functionField.getText();
                Expression expression = new ExpressionBuilder(stringExpression).variables("x").build();


                // Code snippet to find interval [a, b]
                double a = 0;
                double b = 1;
                boolean foundInterval = false;

// First try: Search for the interval in positive range
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
                    aField.setText(String.valueOf(a));
                    bField.setText(String.valueOf(b));
                }
                else
                {
                    aField.setText("No interval found");
                    bField.setText("No interval found");

                    // Show an error message to the user
                    Errors.noIntervalError();
                }

                // End of code snippet

                double tolerance = Double.parseDouble(toleranceField.getText());
                double root = 0;

                // Clearing previous table data
                tableModel.setRowCount(0);

                if (bisectionRadio.isSelected()) {

                    String[] columnNames = {"Iteration", "Lower Bound (a)", "Upper Bound (b)", "Midpoint", "f(Midpoint)"};
                    tableModel = new DefaultTableModel(columnNames, 0);
                    table.setModel(tableModel);

                    root = Bisection.execute(expression, a, b, tolerance, tableModel);

                } else if (falsePositionRadio.isSelected()) {

                    String[] columnNames = {"Iteration", "Lower Bound (a)", "Upper Bound (b)", "Midpoint", "f(a)", "f(b)", "f(Midpoint)"};
                    tableModel = new DefaultTableModel(columnNames, 0);
                    table.setModel(tableModel);

                    root = FalsePosition.execute(expression, a, b, tolerance, tableModel);

                } else if (newtonRaphsonRadio.isSelected()) {

                    String[] columnNames = {"Iteration", "<html>X<sub>n</sub></html>", "<html>X<sub>n+1</sub></html>"};
                    tableModel = new DefaultTableModel(columnNames, 0);
                    table.setModel(tableModel);

                    root = NewtonRaphson.execute(expression, a, b, tolerance, tableModel);
                }

                resultLabel.setText("Root: " + (Double.isNaN(root) ? "No root in interval" : root));
                resultLabel.setForeground(Color.RED);

            } catch (Exception ex) {
                Errors.exceptionError();
            }
        }
    }

    private class ClearButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            functionField.setText("");
            toleranceField.setText("");
            aField.setText("");
            bField.setText("");

            // Clearing table non need to tableModel.setRowCount(0);
            tableModel = null;
            tableModel = new DefaultTableModel(); // Dummy table model, needed to create table properly.
            table.setModel(tableModel);

            resultLabel.setText("Root: "); // Reset the result label
            resultLabel.setForeground(Color.BLACK);
        }
    }

    private static double f(Expression expression, double x) {
        expression.setVariable("x", x);
        return expression.evaluate();
    }
}