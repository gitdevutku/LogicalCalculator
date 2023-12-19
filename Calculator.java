import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

public class Calculator extends JFrame {

    //Declaring radix constants for the calculator
    private static final String DECIMAL_RADIX = "Decimal";
    private static final String BINARY_RADIX = "Binary";
    private static final String HEX_RADIX = "Hexadecimal";

    //Declaring components for the calculator
    private JTextField textField = new JTextField("0", 20);
    private JLabel expressionLabel = new JLabel("", SwingConstants.RIGHT);
    private String firstInput = "";
    private String secondInput = "";
    private String currentOperator = "";
    private String radix = DECIMAL_RADIX;
    private String resultRadix = DECIMAL_RADIX;
    private double doubleResult = 0.0; // Use double for floating-point operations
    private long longResult = 0; // Use long for logical operations
    private JRadioButton decimalButton = new JRadioButton(DECIMAL_RADIX);
    private JRadioButton binaryButton = new JRadioButton(BINARY_RADIX);
    private JRadioButton hexButton = new JRadioButton(HEX_RADIX);

    //Declaring button labels for the calculator
    private final String[] buttonLabels = {
            "A", "<<", ">>", "CE", "DEL",
            "B", "(", ")", "%", "/",
            "C", "7", "8", "9", "*",
            "D", "4", "5", "6", "-",
            "E", "1", "2", "3", "+",
            "F", "+/-", "0", ".", "="
    };

    //Declaring logical labels for the calculator
    private final String[] logicalLabels = {
            "AND", "OR", "XOR", "NOT", "NAND", "NOR"
    };
    //Declaring the button panel and the logical panel for the calculator
    private JPanel buttonPanel = new JPanel(new GridLayout(6, 6));
    private JPanel logicalPanel = new JPanel(new GridLayout(6, 1));

    //Constructor for the calculator
    Calculator() {
        super("Calculator");
        initalizeUI();
        initializeButtons();
        initializeRadixButtons();
        initializeLogicButtons();
        pack();
        updateButtonsState();
        updateDisplay();
        setVisible(true);
    }
    //A method that initializes the UI of the calculator layout and sets the background color, also add some font styles, and set the size of the calculator
    private void initalizeUI(){
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
        textField.setBorder(BorderFactory.createLineBorder(Color.orange, 1));
        textField.setFont(new Font("Arial", Font.BOLD, 24));
        textField.setEditable(false);
        textField.setBackground(Color.ORANGE);
        add(textField, BorderLayout.CENTER);
    }
//A method that initializes the buttons of the calculator and sets the background color, also add some font styles
    private void initializeButtons() {
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setBackground(Color.darkGray);
            button.setForeground(Color.white);
            button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            button.addActionListener(new ButtonClickHandler());
            buttonPanel.add(button);
        }
        add(buttonPanel, BorderLayout.SOUTH);
    }
//A method that initializes the logical buttons of the calculator and sets the background color, also add some font styles
    private void initializeLogicButtons() {
        for (String label : logicalLabels) {
            JButton button = new JButton(label);
            button.setBackground(Color.darkGray);
            button.setForeground(Color.white);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.addActionListener(new ButtonClickHandler());
            logicalPanel.add(button);
        }
        logicalPanel.setBackground(Color.darkGray);
        add(logicalPanel, BorderLayout.WEST);
    }
//A method that initializes the radix buttons of the calculator and make a group for them to select exactly one of them each time and sets the background color, also add some font styles
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
        radixPanel.setBackground(Color.darkGray);
        // Add listeners to the radix buttons
        decimalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                radix = DECIMAL_RADIX;
                updateButtonsState();
                updateResultRadix();
                updateDisplay();
            }
        });
        binaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                radix = BINARY_RADIX;
                updateButtonsState();
                updateResultRadix();
                updateDisplay();
            }
        });
        hexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                radix = HEX_RADIX;
                updateButtonsState();
                updateResultRadix();
                updateDisplay();
            }
        });
    }
//A method that updates the result radix of the calculator
    private void updateResultRadix() {
        resultRadix = radix;
    }
//A method that updates the state of the buttons of the calculator
    private void updateButtonsState() {
        // Check if the radix is binary or hexadecimal
        boolean isBinary = radix.equals(BINARY_RADIX);
        boolean isHexadecimal = radix.equals(HEX_RADIX);
    //Check if the radix is binary or hexadecimal and disable the buttons that are not allowed in binary or hexadecimal
        for (Component component : buttonPanel.getComponents()) { // Loop through the components of the button panel
            // Check if the component is a button
            if (component instanceof JButton) {
                // Cast the component to a button
                JButton button = (JButton) component;
                // Get the label of the button
                String label = button.getText();
                // Check if the label is Hexadecimal
                if (label.matches("[A-F]") && !isHexadecimal) {// Disable the button if the radix is not hexadecimal
                    button.setEnabled(false);
                } else if (label.matches("[2-9]") && isBinary) { // Check if the label is 2-9
                    // Disable the button if the radix is binary
                    button.setEnabled(false);
                } else if (label.equals("+/-") && isBinary) { // Check if the label is +/- and the radix is binary
                    // Disable the button if the radix is binary
                    button.setEnabled(false);
                } else if (label.equals("+/-") && isHexadecimal) { // Check if the label is +/- and the radix is hexadecimal
                    button.setEnabled(false); // Disable the button if the radix is hexadecimal
                } else { // Enable the button for other cases
                    button.setEnabled(true);
                }
            }
        }
    }
//A method that updates the display of the calculator
    private void updateDisplay() {
        String expression = expressionLabel.getText();
        String result = "0";
    //Check if the current operator is empty
        if (!currentOperator.isEmpty() && !secondInput.isEmpty()) {
            // Build the expression based on whether the current operator is empty or not
            expression += (currentOperator.isEmpty() ? " " + firstInput : " " + currentOperator + " " + secondInput);
            // Check the radix and convert the result accordingly
            if (radix.equals(DECIMAL_RADIX)) {
                result = convertResultToCurrentRadix(longResult);
            } else {
                result = convertResultToCurrentRadix(doubleResult);
            }
        } else {
            //Check the radix and convert the result accordingly
            if (radix.equals(DECIMAL_RADIX)) {
                result = convertResultToCurrentRadix(longResult);
            } else {
                result = convertResultToCurrentRadix(doubleResult);
            }
            // Complete the expression with the equals sign and the first input
            expression += "=" + firstInput;
        }
        // Update the expression and the textfield
        expressionLabel.setText(expression);
        textField.setText(result);
    }

/*A method that calculates the result of the calculator and handles the special cases of the calculator in case of logical operations or
 binary or hexadecimal operations this calculator do this by converting the input to decimal and then convert the result to the current radix */

    private void calculateResult() {
        //Check if the current operator is empty
        if (currentOperator.isEmpty()) {
            textField.setText("Error: Incomplete expression");
            return;
        }
//Check if the first input and the second input are empty
        if (firstInput.isEmpty() && secondInput.isEmpty()) {
            textField.setText("Error: Incomplete expression");
            return;
        }
//Check if the radix is binary or hexadecimal and convert the input to decimal
        if (radix.equals(BINARY_RADIX) || radix.equals(HEX_RADIX)) {
            firstInput = convertToDecimalFromAnyRadix(firstInput, radix);
            secondInput = convertToDecimalFromAnyRadix(secondInput, radix);
        }
//First convert the input to double and long and then if special cases are true then perform the logical operation else perform the regular calculations
        double firstDoubleValue = Double.parseDouble(firstInput);
        long firstLongValue = Long.parseLong(firstInput);

        double secondDoubleValue = secondInput.isEmpty() ? 0.0 : Double.parseDouble(secondInput);
        long secondLongValue = Long.parseLong(secondInput);

        String expression = firstInput + " " + currentOperator + " " + secondInput;

        if (("AND".equals(currentOperator) || "OR".equals(currentOperator) || "XOR".equals(currentOperator)
                || "NOT".equals(currentOperator) || "NAND".equals(currentOperator) || "NOR".equals(currentOperator))
                && firstInput.equals(secondInput)) {
            // Perform logical operations for special cases
            longResult = performLogicalOperation(firstLongValue, secondLongValue, currentOperator);
        } else {
            // Perform regular calculations for non-special cases
            switch (currentOperator) {
                //Arithmetic operations
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
                    //Logical operations
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
        }
//Check if the radix is binary or hexadecimal and convert the result to the current radix
        firstInput = convertResultToCurrentRadix(radix.equals(DECIMAL_RADIX) ? longResult : doubleResult);
        secondInput = "";
        currentOperator = "";
        expressionLabel.setText(expression);
        updateDisplay();
    }

//A method that removes the leading zeros of the input by converting it to BigInteger and then convert it back to String
    private String removeLeadingZeros(String input) {
        BigInteger bigInteger = new BigInteger(input);
        return bigInteger.toString();
    }
//A method that converts the input to decimal from any radix
    private String convertToDecimalFromAnyRadix(String input, String sourceRadix) {
        // Ensure input is not empty
        if (input.isEmpty()) {
            return "0";
        }

        // Remove spaces from the input
        input = input.replaceAll("\\s", "");
        if (sourceRadix.equals(BINARY_RADIX)) {
            // Ensure proper binary format and remove leading zeros
            input = removeLeadingZeros(input);
            if (input.isEmpty()) {
                return "0";
            }
            // Convert binary to decimal
            return String.valueOf(Long.parseLong(input, 2));
        } else if (sourceRadix.equals(HEX_RADIX)) {
            // Convert hexadecimal to decimal
            return String.valueOf(Long.parseLong(input, 16));
        }

        return input;
    }

    private String convertResultToCurrentRadix(Number result) {
        // Ensure result radix is BINARY_RADIX
        if (resultRadix.equals(BINARY_RADIX)) {
            // Convert the result to binary
            String binaryString = Long.toBinaryString(result.longValue());

            // Ensure binary result is not empty
            if (binaryString.isEmpty()) {
                return "0";
            }

            // Calculate the number of leading zeros needed for grouping 4 bits
            int leadingZeros = (4 - binaryString.length() % 4) % 4;

            // Add leading zeros to ensure groups of 4 bits
            binaryString = "0".repeat(leadingZeros) + binaryString;

            // Adjust length to the nearest multiple of 4
            int length = (binaryString.length() + 3) / 4 * 4;

            // Insert a space every 4 bits
            for (int i = 4; i < length; i += 5) {
                binaryString = binaryString.substring(0, i) + " " + binaryString.substring(i);
            }

            return binaryString;
            // Ensure result radix is HEX_RADIX
        } else if (resultRadix.equals(HEX_RADIX)) {
            // Convert decimal to hexadecimal without formatting
            return Long.toHexString(result.longValue()).toUpperCase();
        } else {
            // Convert decimal to decimal by converting the result to String
            return String.valueOf(result);
        }
    }

//A method that calculates the ones complement of the input
    private String calculateOnesComplement(String binaryString) {
        String onesComplement = "";
        for (char bit : binaryString.toCharArray()) {
            // Flip each bit
            if (bit == '0') {
                // Append 1 if bit is 0
                onesComplement += '1';
            } else {
                // Append 0 if bit is 1
                onesComplement += '0';
            }
        }
        return onesComplement;
    }
    private long performLogicalOperation(long first, long second, String operation) {
        // Perform logical operations for special cases
        switch (operation) {
            case "AND":
                return first & second;
            case "OR":
                return first | second;
            case "XOR":
                return first ^ second;
            case "NOT":
                return ~second;
            case "NAND":
                return ~(first & second);
            case "NOR":
                return ~(first | second);
            default:
                throw new IllegalArgumentException("Invalid logical operation: " + operation);
        }
    }

//A method that appends the input to the second input of the calculator
private void appendToSecondInput(String input) {
    // Ensure input is not empty
    if (secondInput.equals("0") && !input.equals(".")) {
        secondInput = "";
    }
    // Check if the radix is binary , hexadecimal or decimal and append the input to the second input
    if (input.matches("[0-9A-F.]")) {
        secondInput += input;
    } else if (input.equals("-") && secondInput.isEmpty()) {
        // If the input is '-' and secondInput is empty, append "0" before "-"
        secondInput = "0" + input;
    } else if (input.equals("+/-")) {
        // Toggle the sign of the second input
        if (secondInput.startsWith("-")) {// Check if the second input starts with "-"
            secondInput = secondInput.substring(1);// Remove the leading "-"
        } else {
            secondInput = "-" + secondInput; // Add a leading "-"
        }
    } else {
        // For other operators, append the input with spaces around it
        secondInput += " " + input + " ";
    }

    // Set the updated second input to the text field
    textField.setText(secondInput);
}
//A method that clears the calculator
    private void clear() {
        secondInput = "";
        currentOperator = "";
        firstInput = "";
        doubleResult = 0.0;
        longResult = 0;
        radix = DECIMAL_RADIX;  // Reset radix
        resultRadix = DECIMAL_RADIX;  // Reset result radix
        decimalButton.setSelected(true);
        updateButtonsState();// Update buttons state
        updateDisplay();// Update display
        expressionLabel.setText(" ");  // Reset expression label text to empty but put a space to keep the label visible
    }

//A class that handles the button clicks of the calculator
    public class ButtonClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the label of the clicked button
            String buttonLabel = e.getActionCommand();
            // Get the clicked button
            JButton clickedButton = (JButton) e.getSource();
            //Change the color of the clicked button to orange and then change it back to white after 200 milliseconds to give a click effect
            clickedButton.setForeground(Color.orange);
            //A timer that changes the color of the clicked button to white after 200 milliseconds

            Timer timer = new Timer(200, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    clickedButton.setForeground(Color.white);
                }
            });
            timer.setRepeats(false);
            timer.start();
            //Check the label of the clicked button and perform the appropriate action
            switch (buttonLabel) {
                case "CE":
                    clear();
                    break;
                case "DEL":
                    // Check if the second input is empty
                    if (!secondInput.isEmpty()) {
                        // Remove the last character from the second input
                        secondInput = secondInput.substring(0, secondInput.length() - 1);
                        updateDisplay();// Update display
                        break;
                    }
                    // Check if the first input is empty
                    if (!firstInput.isEmpty()) {
                        // Remove the last character from the first input
                        firstInput = firstInput.substring(0, firstInput.length() - 1);
                        updateDisplay(); // Update display
                        break;
                    }
                    break;
                case "=":
                    // Check if the current operator is empty and the second input is empty
                    if (!currentOperator.isEmpty() && !secondInput.isEmpty()) {
                        // Calculate the result
                        calculateResult();
                        // Check the radix and convert the result accordingly
                        if (radix.equals(DECIMAL_RADIX)) {
                            // Convert the result to decimal
                            firstInput = convertResultToCurrentRadix(doubleResult);
                        } else {
                            // for binary and hexadecimal convert the result to decimal and then convert it to the current radix
                            firstInput = convertResultToCurrentRadix(longResult);
                        }
                        secondInput = ""; // Reset second input
                        currentOperator = buttonLabel; // Set the current operator to "="
                        updateDisplay(); // Update display
                        break;
                    }
                    break;
               //Check if the label of the clicked button is a logical operation and perform the appropriate action
                case "AND":
                case "OR":
                case "XOR":
                case "NOT":
                case "NAND":
                case "NOR":
                 // Check if the current operator is empty and the second input is empty
                    if (!firstInput.isEmpty() && !secondInput.isEmpty()) {
                        // Calculate the result
                        calculateResult();
                        // Check the radix and convert the result accordingly
                        if (radix.equals(DECIMAL_RADIX)) {
                            // Convert the result to decimal
                            firstInput = convertResultToCurrentRadix(doubleResult);
                        } else {
                            // for binary and hexadecimal convert the result to decimal and then convert it to the current radix
                            firstInput = convertResultToCurrentRadix(longResult);
                        }
                        secondInput = ""; // Reset second input
                        currentOperator = buttonLabel; // Set the current operator to "="
                        updateDisplay(); // Update display
                        break;
                    }
                default:
                    // Check if the label of the clicked button is a digit or a dot
                    if (buttonLabel.matches("[0-9A-F]") || buttonLabel.equals(".")) {
                        appendToSecondInput(buttonLabel);

                    } else if (buttonLabel.equals("+/-")) {
                        // Toggle the sign of the second input
                        if (secondInput.startsWith("-")) {
                            // Check if the second input starts with "-"
                            secondInput = secondInput.substring(1);
                        } else {
                            // Add a leading "-"
                            secondInput = "-" + secondInput;
                        }
                    } else { // Check if the label of the clicked button is an operator
                        if (currentOperator.isEmpty()) { // Check if the current operator is empty
                            firstInput = secondInput; // Set the first input to the second input
                            secondInput = ""; // Reset the second input
                            currentOperator = buttonLabel; // Set the current operator to the label of the clicked button
                        } else {
                            calculateResult(); // Calculate the result
                            if (radix.equals(DECIMAL_RADIX)) {
                                // Convert the result to decimal
                                firstInput = convertResultToCurrentRadix(doubleResult);
                            } else {
                                // for binary and hexadecimal convert the result to decimal and then convert it to the current radix
                                firstInput = convertResultToCurrentRadix(longResult);
                            }
                            secondInput = ""; // Reset the second input
                            currentOperator = buttonLabel; // Set the current operator to the label of the clicked button
                        }
                    }
                break;
            }
        }
    }
}
