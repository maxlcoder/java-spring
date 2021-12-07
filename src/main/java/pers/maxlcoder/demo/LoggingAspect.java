package pers.maxlcoder.demo;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Before("execution(public * pers.maxlcoder.demo.UserService.*(..))")
    public void doAccessCheck() {
        System.out.println("[Before] do access check");
    }
}
