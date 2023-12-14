# Logical Calculator

This is a simple logical calculator implemented in Java using Swing for the GUI.

## Features

- Basic arithmetic operations: addition, subtraction, multiplication, division, and modulus.
- Logical operations: AND, OR, XOR, NOT, NAND, NOR.
- Radix conversion: Decimal, Binary, Hexadecimal.
- Input and output in the specified radix.

## Code Structure

The calculator is implemented as a Swing GUI application with the following key components:

- **Main Class:** `Calculator`
- **GUI Components:**
  - `JTextField` for displaying the current input/result.
  - `JLabel` for displaying the expression.
  - `JButtons` for numeric input, arithmetic/logical operations, and other functionalities.
  - `JRadioButtons` for selecting the radix (Decimal, Binary, Hexadecimal).
  - `JPanel` for organizing buttons in a grid layout.

## Usage

1. Run the application.
2. Input numeric values and perform arithmetic or logical operations.
3. Select the desired radix for input and output.

## How to Use

1. **Numeric Input:**
   - Use numeric buttons (`0-9`) and alphabetic buttons (`A-F` in hexadecimal mode).
   - Decimal point (`.`) is available for floating-point numbers.

2. **Arithmetic Operations:**
   - Addition (`+`), Subtraction (`-`), Multiplication (`*`), Division (`/`), Modulus (`%`).
   - Change sign (`+/-`).

3. **Logical Operations:**
   - AND (`AND`), OR (`OR`), XOR (`XOR`), NOT (`NOT`), NAND (`NAND`), NOR (`NOR`).

4. **Other Functions:**
   - Clear Entry (`CE`): Clears the current input.
   - Delete (`DEL`): Removes the last character from the input.
   - Equals (`=`): Calculates and displays the result.

5. **Radix Selection:**
   - Use radio buttons to switch between Decimal, Binary, and Hexadecimal input and output.

## Example

```plaintext
Decimal Input:     Binary Output:
1 + 1 = 2           1 + 1 = 10
3 AND 1 = 1         11 AND 01 = 01
```
## Dependencies
- Java Swing
## Author
- [Utku Berki Baysal](https://github.com/gitdevutku/)
