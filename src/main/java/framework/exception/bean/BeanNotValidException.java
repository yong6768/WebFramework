package framework.exception.bean;

public class BeanNotValidException extends BeansException {
    public BeanNotValidException() {
        super();
    }

    public BeanNotValidException(String message) {
        super(message);
    }

    public BeanNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanNotValidException(Throwable cause) {
        super(cause);
    }

    protected BeanNotValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
