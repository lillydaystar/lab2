package DataTypes;

class IllegalFileFormatException extends Exception {
    IllegalFileFormatException(String message) {
        super(message);
    }
    IllegalFileFormatException() {
        super();
    }
}

class IllegalProductFormatException extends Exception {
    IllegalProductFormatException(String message) {
        super(message);
    }
}

class IncorrectGroupException extends Exception {
    IncorrectGroupException(String message) {
        super(message);
    }
}