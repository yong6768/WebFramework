package framework.exception.handler;

public class NoUniqueHandlerException extends HandlerException {
    public NoUniqueHandlerException() {
    }

    public NoUniqueHandlerException(String message) {
        super(message);
    }

    public NoUniqueHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoUniqueHandlerException(Throwable cause) {
        super(cause);
    }

    public NoUniqueHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
