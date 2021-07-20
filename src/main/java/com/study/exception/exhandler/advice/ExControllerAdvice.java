package com.study.exception.exhandler.advice;

import com.study.exception.exception.UserException;
import com.study.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {
    /**
     * @ControllerAdvice
     * 는 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler, @InitBinder 기능을 부여해주는 역할을 한다.
     * - 대상을 적용하지 않으면 모든 컨트롤러에 적용된다(글로벌 적용)
     *
     */
    /**
     * // Target all Controllers annotated with @RestController
     * @ControllerAdvice(annotations = RestController.class)
     * public class ExampleAdvice1 {}
     * // Target all Controllers within specific packages
     * @ControllerAdvice("org.example.controllers")
     * public class ExampleAdvice2 {}
     * // Target all Controllers assignable to specific classes
     * @ControllerAdvice(assignableTypes = {ControllerInterface.class,AbstractController.class})
     * public class ExampleAdvice3 {}
     * https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-anncontroller-advice (스프링 공식 문서 참고)
     */
    /**
     * ExceptionHandlerExceptionResolver 은 다양한 파라미터와 응담을 지정할 수 있다.
     * 자세한 파라미터와 응답은 다음 공식 메뉴얼을 참고하자.
     * https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-annexceptionhandler-args
     * <p></p>
     */
    /**
     * 이게 그냥 해서 보내면 정상 작동 코드로 인식이 되서 200 status 를 보내게 된다.
     * 이건 사실 좋은 방법이 아니니깐 ResponseStatus 를 붙여서 알맞은 상태를 넣어 주자.
     * 이렇게 하면 servlet까지가서 다시 다른페이지를 내려주는게 아니라 바로 해결을 해서 끝내버린다.
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    /**
     * 위에 두 개가 잡지 못하는 예외들은 최상위인 여기 Exception 으로 넘어오게 된다.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ErrorResult("EX", "내부 오류");
    }
}
