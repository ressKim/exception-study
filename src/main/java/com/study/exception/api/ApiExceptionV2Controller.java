package com.study.exception.api;

import com.study.exception.exception.UserException;
import com.study.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

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

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {

        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id);
    }


    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }

}
