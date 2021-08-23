package framework.exception.bean;

public class BeanStoreException extends BeansException {
    public BeanStoreException() {
        super();
    }

    public BeanStoreException(String message) {
        super(message);
    }

    public BeanStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanStoreException(Throwable cause) {
        super(cause);
    }

    protected BeanStoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
