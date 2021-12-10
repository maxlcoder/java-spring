package pers.maxlcoder.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class A {

    @Autowired
    B b;

    public A(B b) {
        this.b = b;
    }

    public String getNameOfAFromB() {
        return b.getName();
    }
}
