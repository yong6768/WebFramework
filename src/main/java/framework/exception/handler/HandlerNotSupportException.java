package framework.exception.handler;

public class HandlerNotSupportException extends HandlerException {
    public HandlerNotSupportException() {
    }

    public HandlerNotSupportException(String message) {
        super(message);
    }

    public HandlerNotSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandlerNotSupportException(Throwable cause) {
        super(cause);
    }

    public HandlerNotSupportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
