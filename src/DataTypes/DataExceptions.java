package DataTypes;

public class DataExceptions extends Exception{
    public DataExceptions() {
        super();
    }

    public DataExceptions(String message) {
        super(message);
    }
}

class IllegalFileFormatException extends DataExceptions {
    IllegalFileFormatException(String message) {
        super(message);
    }
    IllegalFileFormatException() {
        super();
    }
}

class IncorrectGroupException extends DataExceptions {
    IncorrectGroupException(String message) {
        super(message);
    }
}

class IncorrectPriceException extends DataExceptions {
    IncorrectPriceException(String message) {
        super(message);
    }
}

class IncorrectCountException extends DataExceptions {
    IncorrectCountException(String message) {
        super(message);
    }
}