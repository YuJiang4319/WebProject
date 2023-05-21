package cn.kawauso.advice;

import cn.kawauso.entity.ErrorMessage;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * {@link ExceptionControllerAdvice}是一个异常处理类，标记了{@link ResponseControllerAdvice}注解
 *
 * @author RealDragonking
 */
@RestControllerAdvice
public final class ExceptionControllerAdvice {

    @ExceptionHandler(value = Exception.class)
    public ErrorMessage catchException(Exception exception) {
        if (exception.getMessage() == null) {
            return new ErrorMessage("");
        } else {
            return new ErrorMessage(exception.getMessage());
        }
    }

}
