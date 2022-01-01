package com.momo.toys.be.factory;

import java.util.Calendar;
import java.util.function.UnaryOperator;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.momo.toys.be.account.Problem;

@Component
public class CommonUtility{

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public Problem createProblem(String title, int status, String message){

        Problem problem = new Problem();
        problem.setTitle(title);
        problem.setStatus(status);
        problem.setDetail(message);

        return problem;
    }

    public UnaryOperator<String> uniqueFileName = extension -> String.format("%s.%s", Calendar.getInstance().getTime().getTime(), extension);
}
