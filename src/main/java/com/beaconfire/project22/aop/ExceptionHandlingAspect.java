package com.beaconfire.project22.aop;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlingAspect {

    // Pointcut to target all service methods
    @Pointcut("execution(* com.beaconfire.project22.Service.*.*(..))")
    public void serviceMethods() {}

    // Advice to handle exceptions thrown by any service method
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public ResponseEntity<String> handleException(Exception ex) {
        // Log the exception (for debugging purposes)
        System.err.println("Exception caught by AOP: " + ex.getMessage());

        // Return a user-friendly message as the response
        return ResponseEntity.status(500).body("An error occurred: " + ex.getMessage());
    }
}
