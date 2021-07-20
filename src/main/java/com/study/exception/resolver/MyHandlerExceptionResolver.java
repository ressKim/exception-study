package com.study.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    /**
     * HandlerExceptionResolver - 컨트롤러 밖으로 예외가 던져진 경우 예외를 해결하고
     * 동작을 새로 정의 할 수 있는 방법을 제공한다. 줄여서 ExceptionResolver 라고 한다.
     * <p>
     * ModelAndView() 를 빈값으로 넘기면 return, return 으로 처리되서 서블릿까지 정상적으로 간다(예외를 먹어버린다고 봐도 무방하다)
     * <p>
     * null 을 반환하게 되면 다음 ExceptionResolver 를 찾아 실행하고, 만약 처리할 수 있는 ExceptionResolver 가 없다면
     * 예외 처리가 안되고, 기존에 발생한 예외를 서블릿 밖으로 던진다.
     *
     * ExceptionResolver 활용
     * 예외 상태 코드 변환
     * - 예외를 'response.sendError(xxx)' 호출로 변경해서 서블릿에서 상태 코드에 따른 오류를 처리하도록 위임 할 때
     * - 이후 was 는 서블릿 오류 페이지를 찾아서 내부 호출, 예를 들어 spring boot 가 기본 설정된 '/error' 호출 등
     * 뷰 템플릿 처리
     * - ModelAndView 에 값을 채워서 예외에 따른 새로운 오류 화면 뷰 렌더링 해서 고객에게 제공
     * API 응답 처리
     * - 'response.getWriter().println("hello");' 처럼 HTTP 응답 바디에 직접 데이터를 넣어 주는것도 가능
     * , JSON 으로 응답하면 API 응답 처리도 할 수 있다
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        log.info("call resolver=", ex);

        try {
            if (ex instanceof IllegalArgumentException) {
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView();
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }

        //
        return null;
    }
}
