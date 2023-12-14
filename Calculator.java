import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame {

    private JTextField textField = new JTextField("0", 20);
    private JLabel expressionLabel = new JLabel("", SwingConstants.RIGHT);
    private String firstInput = "";
    private String secondInput = "";
    private String currentOperator = "";
    private String radix = "Decimal";
    private String resultRadix = "Decimal";
    private long longResult = 0L;
    private JRadioButton decimalButton = new JRadioButton("Decimal");
    private JRadioButton binaryButton = new JRadioButton("Binary");
    private JRadioButton hexButton = new JRadioButton("Hexadecimal");
    private final String[] buttonLabels = {
            "A", "<<", ">>", "CE", "DEL",
            "B", "(", ")", "%", "/",
            "C", "7", "8", "9", "*",
            "D", "4", "5", "6", "-",
            "E", "1", "2", "3", "+",
            "F", "+/-", "0", ".", "="
    };
    private final String[] logicalLabels = {
            "AND", "OR", "XOR", "NOT", "NAND", "NOR"
    };
    private JPanel buttonPanel = new JPanel(new GridLayout(6, 6));
    private JPanel logicalPanel = new JPanel(new GridLayout(6, 1));

    public Calculator() {
        super("Logical Calculator");
        buttonPanel.setForeground(Color.BLACK);
        logicalPanel.setForeground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        setSize(400, 400);

        setLocationRelativeTo(null);

        textField.setBounds(30, 40, 280, 30);
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        textField.setFont(new Font("Arial", Font.BOLD, 20));
        textField.setEditable(false);
        add(textField, BorderLayout.NORTH);

        expressionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(expressionLabel, BorderLayout.SOUTH);
        initializeButtons();
        initializeRadixButtons();
        initializeLogicButtons();
        updateButtonsState();
        updateDisplay();
        setVisible(true);
    }

    private void initializeButtons() {
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setBackground(Color.white);
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }
        add(buttonPanel, BorderLayout.CENTER);
        pack();
    }

    private void initializeLogicButtons() {
        for (String label : logicalLabels) {
            JButton button = new JButton(label);
            button.setBackground(Color.white);
            button.addActionListener(new ButtonClickListener());
            logicalPanel.add(button);
        }
        add(logicalPanel, BorderLayout.WEST);
        pack();
    }

    private void initializeRadixButtons() {
        JPanel radixPanel = new JPanel(new GridLayout(3, 1));
        radixPanel.add(decimalButton);
        radixPanel.add(binaryButton);
        radixPanel.add(hexButton);
        decimalButton.setBackground(Color.white);
        binaryButton.setBackground(Color.white);
        hexButton.setBackground(Color.white);
        add(radixPanel, BorderLayout.EAST);
        ButtonGroup radixGroup = new ButtonGroup();
        radixGroup.add(decimalButton);
        radixGroup.add(binaryButton);
        radixGroup.add(hexButton);
        decimalButton.setSelected(true);
        decimalButton.addActionListener(e -> {
            radix = "Decimal";
            updateButtonsState();
            updateResultRadix();
            pack();
        });
        binaryButton.addActionListener(e -> {
            radix = "Binary";
            updateButtonsState();
            updateResultRadix();
            pack();
        });
        hexButton.addActionListener(e -> {
            radix = "Hexadecimal";
            updateButtonsState();
            updateResultRadix();
            pack();
        });
    }

    private void updateResultRadix() {
        resultRadix = radix;
    }

    private void updateButtonsState() {
        boolean isBinary = radix.equals("Binary");
        boolean isHexadecimal = radix.equals("Hexadecimal");

        for (Component component : buttonPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                String label = button.getText();
                if (label.matches("[A-F]") && !isHexadecimal) {
                    button.setEnabled(false);
                } else if (label.matches("[2-9]") && isBinary) {
                    button.setEnabled(false);
                } else if (label.equals("+/-") && isBinary) {
                    button.setEnabled(false);
                } else if (label.equals("+/-") && isHexadecimal) {
                    button.setEnabled(false);
                } else {
                    button.setEnabled(true);
                }
            }
        }
    }

    private void updateDisplay() {
        String expression = expressionLabel.getText();
        String result = convertResultToCurrentRadix(longResult);
        textField.setText(result);
        expressionLabel.setText(expression.isEmpty() ? "" : expression + " = " + result);
    }

    private void calculateResult() {
        if (currentOperator.isEmpty()) {
            displayError("Error: Incomplete expression");
            return;
        }

        if (firstInput.isEmpty() && secondInput.isEmpty()) {
            displayError("Error: Incomplete expression");
            return;
        }

        if (radix.equals("Binary") || radix.equals("Hexadecimal")) {
            // Convert inputs to decimal for calculation
            firstInput = convertToDecimal(firstInput, radix);
            secondInput = convertToDecimal(secondInput, radix);
        }

        long firstValue = firstInput.isEmpty() ? 0L : Long.parseLong(firstInput);
        long secondValue = secondInput.isEmpty() ? 0L : Long.parseLong(secondInput);

        String expression = firstInput + " " + currentOperator + " " + secondInput;

        switch (currentOperator) {
            case "+":
                longResult = firstValue + secondValue;
                break;
            case "-":
                if (firstInput.isEmpty()) {
                    longResult = -secondValue;
                } else {
                    longResult = firstValue - secondValue;
                }
                break;
            case "*":
                longResult = firstValue * secondValue;
                break;
            case "/":
                if (secondValue != 0) {
                    longResult = firstValue / secondValue;
                } else {
                    displayError("Error: Division by zero");
                    return;
                }
                break;
            case "%":
                longResult = firstValue % secondValue;
                break;
            case "+/-":
                if (!secondInput.isEmpty()) {
                    secondValue *= -1;
                    secondInput = String.valueOf(secondValue);
                } else {
                    firstValue *= -1;
                    firstInput = String.valueOf(firstValue);
                }
                updateDisplay();
                return;
            case "AND":
                longResult = firstValue & secondValue;
                break;
            case "OR":
                longResult = firstValue | secondValue;
                break;
            case "XOR":
                longResult = firstValue ^ secondValue;
                break;
            case "NOT":
                longResult = ~secondValue;
                expression = currentOperator + " " + secondInput;
                break;
            case "NAND":
                longResult = ~(firstValue & secondValue);
                break;
            case "NOR":
                longResult = ~(firstValue | secondValue);
                break;
            default:
                displayError("Error: Unknown operator");
                return;
        }

        firstInput = convertResultToCurrentRadix(longResult);
        secondInput = "";
        currentOperator = "";
        expressionLabel.setText(expression);
        updateDisplay();
    }


    private String convertToDecimal(String input, String sourceRadix) {
        if (input.isEmpty()) {
            return "0";
        }

        if (sourceRadix.equals("Binary")) {
            return String.valueOf(Long.parseLong(input, 2));
        } else if (sourceRadix.equals("Hexadecimal")) {
            return String.valueOf(Long.parseLong(input, 16));
        }
        return input;
    }


    private String convertResultToCurrentRadix(long result) {
        if (resultRadix.equals("Binary")) {
            return Long.toBinaryString(result);
        } else if (resultRadix.equals("Hexadecimal")) {
            return Long.toHexString(result).toUpperCase();
        } else {
            return String.valueOf(result);
        }
    }

    private void appendToSecondInput(String input) {
        if (secondInput.equals("0") && !input.equals(".")) {
            secondInput = "";
        }
        if (input.equals("-") && secondInput.isEmpty()) {
            secondInput = "0" + input;
        } else {
            secondInput += input;
        }
    }

    private void displayError(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        new Calculator();
    }

    private void clear() {
        secondInput = "";
        currentOperator = "";
        firstInput = "";
        longResult = 0L;
        expressionLabel.setText("");
        updateDisplay();
    }

    public class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonLabel = e.getActionCommand();
            switch (buttonLabel) {
                case "CE":
                    clear();
                    updateDisplay();
                    break;
                case "DEL":
                    if (!secondInput.isEmpty()) {
                        secondInput = secondInput.substring(0, secondInput.length() - 1);
                        updateDisplay();
                        break;
                    }
                    if (!firstInput.isEmpty()) {
                        firstInput = firstInput.substring(0, firstInput.length() - 1);
                        updateDisplay();
                        break;
                    }
                    break;
                default:
                    if (logicalOperator(buttonLabel)) {
                        if (!firstInput.isEmpty() && !secondInput.isEmpty()) {
                            calculateResult();
                            firstInput = convertResultToCurrentRadix(longResult);
                            secondInput = "";
                        }
                        currentOperator = buttonLabel;
                        updateDisplay();
                    } else if (buttonLabel.matches("[0-9A-F]") || buttonLabel.equals(".")) {
                        appendToSecondInput(buttonLabel);
                        updateDisplay();
                    } else if (buttonLabel.equals("+/-")) {
                        if (secondInput.startsWith("-")) {
                            secondInput = secondInput.substring(1);
                        } else {
                            secondInput = "-" + secondInput;
                        }
                        updateDisplay();
                    } else if (buttonLabel.equals("=")) {
                        calculateResult();
                    } else {
                        if (currentOperator.isEmpty()) {
                            firstInput = secondInput;
                            secondInput = "";
                            currentOperator = buttonLabel;
                        } else {
                            calculateResult();
                            firstInput = convertResultToCurrentRadix(longResult);
                            secondInput = "";
                            currentOperator = buttonLabel;
                        }
                        updateDisplay();
                    }
                    break;
            }
        }

        private boolean logicalOperator(String operator) {
            for (String logicalLabel : logicalLabels) {
                if (operator.equals(logicalLabel)) {
                    return true;
                }
            }
            return false;
        }
    }
}
