package site.LatteIs.latteIs.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter){ //컨트롤러 메소드의 특정 파라미터를 지원하는 지 판단
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null; //파라미터에 @LoginUser 어노테이션이 붙어있는 지
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType()); //파라미터 클래스 타입이 SessionUser.class인지
        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception{
        //파라미터에 전달할 객체를 생성
        return httpSession.getAttribute("user"); //세션에서 객체를 가져옴
    }

}
