package baseconverterproject;

import java.util.LinkedHashMap;
import java.util.Map;

public class BaseConverter {

    private final LinkedHashMap<String, Object> messageMap = new LinkedHashMap<String, Object>();

    private String result;
    private int fromBase;
    private int toBase;
    private String input;

    public BaseConverter() {
    }

    // ########################################################################
    // CALCULATIONS
    // ########################################################################
    //
    public String convertBase(String number, int fromBase, int toBase) {

        if (!isValidBase(number, fromBase)) {
            throw new IllegalArgumentException(
                    "Number is not valid for the given base!\nPlease enter values from 0 to " + (fromBase - 1));
        }
        if (fromBase < 2 || toBase < 2) {
            throw new IllegalArgumentException("Base must be greater than or equal to 2");
        }
        if (number == null || number.isEmpty()) {
            throw new IllegalArgumentException("Number cannot be null or empty");
        }

        this.input = number;
        this.toBase = toBase;
        this.fromBase = fromBase;

        if (number.contains(".")) {
            String[] parts = number.split("\\.");
            String integerPart = parts[0];
            String fractionalPart = "0." + parts[1];
            // messageMap.put("title_0", "INTEGER number: " + integerPart + "\t " + "FRACTIONAL part: " + fractionalPart);

            String integerResult = convertBase(integerPart, fromBase, toBase);
            String fractionalResult = convertFractionalPart(fractionalPart, fromBase, toBase);
            fractionalResult = fractionalResult.contains(".") ? fractionalResult.split("0.")[1] : fractionalResult;
            result = integerResult + "." + fractionalResult;

            while (result.endsWith("0")) {
                result = result.substring(0, result.length() - 1);
            }
            this.input = parts[0] + "." + parts[1];

            return result;
        }
        if (Integer.parseInt(number, fromBase) == 0) {
            return "0";
        }
        int base10Value;
        if (fromBase != 10) {
            messageMap.put("title_1", "Number: " + number + " To Base10");
            base10Value = integerToBase10(number, fromBase);
            messageMap.put("title_2", "Equivalent to Base10: " + base10Value);
        } else {
            base10Value = Integer.parseInt(number);

        }

        if (toBase != 10) {
            messageMap.put("title_3", "Number: " + base10Value + " To Base" + toBase);
            result = integerFromBase10(base10Value, toBase);
            messageMap.put("title_4", "Read from bottom to up: " + result);
        } else {
            result = base10Value + "";
        }

        return result;
    }

    private int integerToBase10(String number, int fromBase) {
        int result = 0;
        int power = 0;

        messageMap.put("theader_3", new String[]{"Digit - Position", "Solution", "Result"});

        for (int i = number.length() - 1; i >= 0; i--) {
            char currentChar = number.charAt(i);

            if (currentChar == '.') {
                continue;
            }

            int digit;
            if (Character.isDigit(currentChar)) {
                digit = Character.getNumericValue(currentChar);
            } else {
                digit = Character.digit(currentChar, fromBase);
            }
            int base10digit = (int) (digit * Math.pow(fromBase, power));
            result += base10digit;

            messageMap.put("trow_3_" + i,
                    new String[]{
                        digit + " - " + power + "",
                        String.format("%d * (%d) ^ %d", digit, fromBase, power),
                        base10digit + ""});

            power++;
        }
        return result;
    }

    private String integerFromBase10(int number, int toBase) {
        StringBuilder result = new StringBuilder();

        messageMap.put("theader_4", new String[]{"Division", "Quotient", "Remainder"});
        int i = 0;
        while (number > 0) {
            int quotient = number / toBase;
            int remainder = number % toBase;
            String toStr = Integer.toString(remainder, toBase).toUpperCase();
            result.insert(0, toStr);

            messageMap.put("trow_4_" + i,
                    new String[]{number + "/" + toBase + "", quotient + "", remainder + ""});

            number /= toBase;
            i++;
        }

        return result.toString();
    }

    private String convertFractionalPart(String number, int fromBase, int toBase) {
        double base10Value;

        if (fromBase != 10) {
            messageMap.put("title_5", "CONVERTING FRACTION: " + number + " TO BASE 10.");
            base10Value = fractionToBase10(number, fromBase);
            messageMap.put("title_6", "FRACTIONAL BASE 10: " + String.format("%.7f", base10Value));
        } else {
            base10Value = Double.parseDouble(number);
        }

        if (toBase != 10) {
            messageMap.put("title_7",
                    "CONVERTING FRACTION BASE 10: " + String.format("%.7f", base10Value) + " TO BASE " + toBase + ".");
            result = fractionFromBase10(base10Value, toBase);
            messageMap.put("title_8", "Read from top to bottom: " + result);
        } else {
            result = base10Value + "";
        }

        return result;
    }

    private double fractionToBase10(String number, int fromBase) {
        double result = 0;
        int power = -1;
        number = number.split("\\.")[1];

        messageMap.put("theader_1", new String[]{"Digit - Position", "Solution", "Product"});

        for (int i = 0; i < number.length(); i++) {
            
            char character = number.charAt(i);
            if (character == '.') {
                continue;
            }
            int digit = Character.digit(character, fromBase);
            double base10digit = digit * Math.pow(fromBase, power);
            result += base10digit;

            messageMap.put("trow_1_" + i,
                    new String[]{
                        character + " - " + Math.abs(power), 
                        String.format("%d * (%d)^(%d) ", digit, fromBase, power),
                        String.format("%.6f", base10digit)
                    });

            power--;
        }
        return result;
    }

    private String fractionFromBase10(double base10value, int toBase) {
        int digitCounts = 6;
        StringBuilder resultBuilder = new StringBuilder();

        messageMap.put("theader_2", new String[]{"Digit * Base", "Product", "Integer Part"});

        for (int i = 0; i < digitCounts; i++) {
            double prev = base10value;
            double product = base10value * toBase;
            base10value = product;

            int integerPart = (int) base10value;
            String toStr = Integer.toString(integerPart, toBase).toUpperCase();
            resultBuilder.append(toStr);
            base10value -= integerPart;

            messageMap.put("trow_2_" + i,
                    new String[]{
                        String.format("%.6f * %d = ", prev, toBase),
                        String.format("%.6f", product) + "",
                        integerPart + ""
                    });
        }

        return resultBuilder.toString();
    }
    //
    // ########################################################################

    // ########################################################################
    // CLASS METHODS
    // ########################################################################
    //
    private boolean isValidBase(String number, int base) {
        for (int i = 0; i < number.length(); i++) {
            char currentChar = number.charAt(i);
            if (currentChar == '.') {
                continue;
            }
            int digit;
            try {
                digit = Integer.parseInt("" + currentChar, base);
            } catch (NumberFormatException e) {
                return false;
            }
            if (digit >= base) {
                return false;
            }
        }
        return true;
    }

    public void printMessage() {
        for (Map.Entry<String, Object> entry : messageMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            System.out.print(key + ": ");

            if (value instanceof String[]) {
                String[] arrayValue = (String[]) value;
                System.out.print("[");
                for (int i = 0; i < arrayValue.length; i++) {
                    System.out.print(arrayValue[i]);
                    if (i < arrayValue.length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println("]");
            } else {
                System.out.println(value);
            }
        }
    }

    public LinkedHashMap<String, Object> getSolutionMap() {
        return messageMap;
    }

    public String getResult() {
        return result;
    }

    public int getToBase() {
        return this.toBase;
    }

    public int getFromBase() {
        return this.fromBase;
    }

    public String getInput() {
        return this.input;
    }

    public void clearMap() {
        messageMap.clear();
    }
    //
    // ########################################################################
}
