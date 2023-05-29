package cn.kawauso.entity;

/**
 * {@link ErrorMessage}是一个实体类，记录了错误信息，由{@link cn.kawauso.advice.ExceptionControllerAdvice}捕获
 * {@link Exception}后，封装成对应的{@link ErrorMessage}响应给客户端
 *
 * @author RealDragonking
 */
public final class ErrorMessage {

    private final String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    /**
     * @return 异常信息
     */
    public String getMessage() {
        return message;
    }

}
