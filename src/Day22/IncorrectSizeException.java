package Day22;

public class IncorrectSizeException extends Exception {
    public IncorrectSizeException(String methodName, int expected, int actual) {
        super("\n| ERROR in " + methodName + "\n| Expected size: " + expected +
                "\n| Actual size: " + actual);
    }
}
