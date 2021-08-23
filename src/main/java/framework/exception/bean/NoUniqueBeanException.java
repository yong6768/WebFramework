package framework.exception.bean;

public class NoUniqueBeanException extends BeansException {
    public NoUniqueBeanException() {
        super();
    }

    public NoUniqueBeanException(String message) {
        super(message);
    }

    public NoUniqueBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoUniqueBeanException(Throwable cause) {
        super(cause);
    }

    protected NoUniqueBeanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
