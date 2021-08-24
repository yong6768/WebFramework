package framework.exception.bean;

public class CircularReferenceBeanException extends BeansException {
    public CircularReferenceBeanException() {
        super();
    }

    public CircularReferenceBeanException(String message) {
        super(message);
    }

    public CircularReferenceBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public CircularReferenceBeanException(Throwable cause) {
        super(cause);
    }

    protected CircularReferenceBeanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
