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
    private double doubleResult = 0.0; // Use double for floating-point operations
    private long longResult = 0L; // Use long for logical operations
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
        super("Calculator");
        setBackground(Color.gray);
        buttonPanel.setForeground(Color.black);
        buttonPanel.setBackground(Color.gray);
        logicalPanel.setForeground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setSize(400, 500);

        setLocationRelativeTo(null);
        expressionLabel.setVisible(true);
        expressionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        expressionLabel.setBackground(Color.ORANGE);
        expressionLabel.setOpaque(true);
        expressionLabel.setForeground(Color.BLACK);
        add(expressionLabel, BorderLayout.NORTH);

        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setPreferredSize(new Dimension(200, 30));
        textField.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
        textField.setFont(new Font("Arial", Font.BOLD, 24));
        textField.setEditable(false);
        textField.setBackground(Color.ORANGE);
        add(textField, BorderLayout.CENTER);

        initializeButtons();
        initializeRadixButtons();
        initializeLogicButtons();
        updateButtonsState();
        setVisible(true);
    }

    private void initializeButtons() {
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setBackground(Color.darkGray);
            button.setForeground(Color.white);
            button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
    }

    private void initializeLogicButtons() {
        for (String label : logicalLabels) {
            JButton button = new JButton(label);
            button.setBackground(Color.darkGray);
            button.setForeground(Color.white);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
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
        decimalButton.setBackground(Color.darkGray);
        binaryButton.setBackground(Color.darkGray);
        hexButton.setBackground(Color.darkGray);
        decimalButton.setForeground(Color.white);
        binaryButton.setForeground(Color.white);
        hexButton.setForeground(Color.white);
        add(radixPanel, BorderLayout.EAST);
        ButtonGroup radixGroup = new ButtonGroup();
        radixGroup.add(decimalButton);
        radixGroup.add(binaryButton);
        radixGroup.add(hexButton);
        decimalButton.setSelected(true);
        decimalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                radix = "Decimal";
                updateButtonsState();
                updateResultRadix();
                updateDisplay();
                pack();
            }
        });
        binaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                radix = "Binary";
                updateButtonsState();
                updateResultRadix();
                updateDisplay();
                pack();
            }
        });
        hexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                radix = "Hexadecimal";
                updateButtonsState();
                updateResultRadix();
                updateDisplay();
                pack();
            }
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
        String result = "0";

        if (!currentOperator.isEmpty() && !secondInput.isEmpty()) {
            expression += (currentOperator.isEmpty() ? " " + firstInput : " " + currentOperator + " " + secondInput);
            result = convertResultToCurrentRadix(radix.equals("Decimal") ? longResult : doubleResult);
        } else {
            result = convertResultToCurrentRadix(radix.equals("Decimal") ? longResult : doubleResult);
            expression += "=" + firstInput;
        }
        expressionLabel.setText(expression);
        textField.setText(result);
    }



    private void calculateResult() {
        if (currentOperator.isEmpty()) {
            textField.setText("Error: Incomplete expression");
            return;
        }

        if (firstInput.isEmpty() && secondInput.isEmpty()) {
            textField.setText("Error: Incomplete expression");
            return;
        }

        if (radix.equals("Binary") || radix.equals("Hexadecimal")) {
            // Convert inputs to decimal for calculation
            firstInput = convertToDecimal(firstInput, radix);
            secondInput = convertToDecimal(secondInput, radix);
        }

        double firstDoubleValue = Double.parseDouble(firstInput);
        long firstLongValue = (long) firstDoubleValue;

        double secondDoubleValue = secondInput.isEmpty() ? 0.0 : Double.parseDouble(secondInput);
        long secondLongValue = (long) secondDoubleValue;

        String expression = firstInput + " " + currentOperator + " " + secondInput;

        switch (currentOperator) {
            case "+":
                doubleResult = firstDoubleValue + secondDoubleValue;
                longResult = firstLongValue + secondLongValue;
                break;
            case "-":
                doubleResult = firstDoubleValue - secondDoubleValue;
                longResult = firstLongValue - secondLongValue;
                break;
            case "*":
                doubleResult = firstDoubleValue * secondDoubleValue;
                longResult = firstLongValue * secondLongValue;
                break;
            case "/":
                if (secondDoubleValue != 0.0) {
                    doubleResult = firstDoubleValue / secondDoubleValue;
                    longResult = firstLongValue / secondLongValue;
                } else {
                    textField.setText("Error: Division by zero");
                    return;
                }
                break;
            case "%":
                doubleResult = firstDoubleValue / 100 * secondDoubleValue;
                longResult = firstLongValue / 100 * secondLongValue;
                break;
            case "+/-":
                firstDoubleValue *= -1;
                secondDoubleValue *= -1;
                firstLongValue = (long) firstDoubleValue;
                secondLongValue = (long) secondDoubleValue;
                break;
            case "<<":
                longResult = firstLongValue << secondLongValue;
                break;
            case ">>":
                longResult = firstLongValue >> secondLongValue;
                break;
            case "AND":
                longResult = firstLongValue & secondLongValue;
                break;
            case "OR":
                longResult = firstLongValue | secondLongValue;
                break;
            case "XOR":
                longResult = firstLongValue ^ secondLongValue;
                break;
            case "NOT":
                longResult = ~secondLongValue;
                break;
            case "NAND":
                longResult = ~(firstLongValue & secondLongValue);
                break;
            case "NOR":
                longResult = ~(firstLongValue | secondLongValue);
                break;
            default:
                return;
        }

        firstInput = convertResultToCurrentRadix(radix.equals("Decimal") ? longResult : doubleResult);
        secondInput = "";
        currentOperator = "";
        expressionLabel.setText(expression);
    }

    private String convertToDecimal(String input, String sourceRadix) {
        if (input.isEmpty()) {
            return "0";
        }

        // Remove spaces from the input
        input = input.replaceAll("\\s", "");

        if (sourceRadix.equals("Binary")) {
            input = input.replaceFirst("^0+(?!$)", "");
            if (input.isEmpty()) {
                return "0";
            }

            return String.valueOf(Long.parseLong(input, 2));
        } else if (sourceRadix.equals("Hexadecimal")) {
            return String.valueOf(Long.parseLong(input, 16));
        }

        return input;
    }



    private String convertResultToCurrentRadix(Number result) {
        if (resultRadix.equals("Binary")) {
            int bitWidth = 4;
            String binaryString = Long.toBinaryString(result.longValue());


            boolean isNegative = binaryString.startsWith("-");


            if (isNegative) {
                binaryString = calculateOnesComplement(binaryString.substring(1));
            }


            while (binaryString.length() % bitWidth != 0) {
                binaryString = "0" + binaryString;
            }


            String formattedBinary = "";
            for (int i = 0; i < binaryString.length(); i++) {
                if (i > 0 && i % bitWidth == 0) {
                    formattedBinary += " ";
                }
                formattedBinary += binaryString.charAt(i);
            }


            if (isNegative) {
                formattedBinary = " " + formattedBinary;
            }

            return formattedBinary;
        } else if (resultRadix.equals("Hexadecimal")) {
            return Long.toHexString(result.longValue()).toUpperCase();
        } else {
            return String.valueOf(result);
        }
    }

    private String calculateOnesComplement(String binaryString) {
        String onesComplement = "";

        for (char bit : binaryString.toCharArray()) {
            onesComplement += (bit == '0') ? '1' : '0';
        }

        return onesComplement;
    }








    private void appendToSecondInput(String input) {
        if (secondInput.equals("0") && !input.equals(".")) {
            secondInput = "";
        }

        if (input.matches("[0-9A-F.]")) {
            secondInput += input;
        } else if (input.equals("-") && secondInput.isEmpty()) {
            secondInput = "0" + input;
        } else if (input.equals("+/-")) {
            if (secondInput.startsWith("-")) {
                secondInput = secondInput.substring(1);
            } else {
                secondInput = "-" + secondInput;
            }
        } else {
            secondInput += " " + input + " ";
        }
        textField.setText(secondInput);
    }
    private void clear() {
        secondInput = "";
        currentOperator = "";
        firstInput = "";
        doubleResult = 0.0;
        longResult = 0L;
        radix = "Decimal";  // Reset radix
        resultRadix = "Decimal";  // Reset result radix
        expressionLabel.setText("");
        updateButtonsState();
        updateDisplay();
    }

    public class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonLabel = e.getActionCommand();
            JButton clickedButton = (JButton) e.getSource();

            clickedButton.setForeground(Color.orange);

            Timer timer = new Timer(200, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    clickedButton.setForeground(Color.white);
                }
            });
            timer.setRepeats(false);
            timer.start();

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
                case "=":
                    if (!currentOperator.isEmpty() && !secondInput.isEmpty()) {
                        calculateResult();
                        firstInput = convertResultToCurrentRadix(radix.equals("Decimal") ? longResult : doubleResult);
                        secondInput = "";
                        currentOperator = buttonLabel;
                        updateDisplay();
                        break;
                    }
                    break;
                case "AND":
                    if (!currentOperator.isEmpty() && !secondInput.isEmpty()) {
                    calculateResult();
                    firstInput = convertResultToCurrentRadix(radix.equals("Decimal") ? longResult : doubleResult);
                    secondInput = "";
                    currentOperator = buttonLabel;
                    updateDisplay();
                    break;
                }
                case "OR":
                    if (!currentOperator.isEmpty() && !secondInput.isEmpty()) {
                        calculateResult();
                        firstInput = convertResultToCurrentRadix(radix.equals("Decimal") ? longResult : doubleResult);
                        secondInput = "";
                        currentOperator = buttonLabel;
                        updateDisplay();
                        break;
                    }
                case "XOR":
                    if (!currentOperator.isEmpty() && !secondInput.isEmpty()) {
                        calculateResult();
                        firstInput = convertResultToCurrentRadix(radix.equals("Decimal") ? longResult : doubleResult);
                        secondInput = "";
                        currentOperator = buttonLabel;
                        updateDisplay();
                        break;
                    }
                case "NOT":
                    if (!currentOperator.isEmpty() && !secondInput.isEmpty()) {
                        calculateResult();
                        firstInput = convertResultToCurrentRadix(radix.equals("Decimal") ? longResult : doubleResult);
                        secondInput = "";
                        currentOperator = buttonLabel;
                        updateDisplay();
                        break;
                    }
                case "NAND":
                    if (!currentOperator.isEmpty() && !secondInput.isEmpty()) {
                        calculateResult();
                        firstInput = convertResultToCurrentRadix(radix.equals("Decimal") ? longResult : doubleResult);
                        secondInput = "";
                        currentOperator = buttonLabel;
                        updateDisplay();
                        break;
                    }
                case "NOR":
                    if (!firstInput.isEmpty() && !secondInput.isEmpty()) {
                        calculateResult();
                        firstInput = convertResultToCurrentRadix(radix.equals("Decimal") ? longResult : doubleResult);
                        secondInput = "";
                        currentOperator = buttonLabel;
                        updateDisplay();
                        break;
                    }
                default:
                    if (buttonLabel.matches("[0-9A-F]") || buttonLabel.equals(".")) {
                        appendToSecondInput(buttonLabel);
                    } else if (buttonLabel.equals("+/-")) {
                        if (secondInput.startsWith("-")) {
                            secondInput = secondInput.substring(1);
                        } else {
                            secondInput = "-" + secondInput;
                        }
                    } else {
                        if (currentOperator.isEmpty()) {
                            firstInput = secondInput;
                            secondInput = "";
                            currentOperator = buttonLabel;
                        } else {
                            calculateResult();
                            firstInput = convertResultToCurrentRadix(radix.equals("Decimal") ? longResult : doubleResult);
                            secondInput = "";
                            currentOperator = buttonLabel;
                        }
                    }
                    break;
            }
        }
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
