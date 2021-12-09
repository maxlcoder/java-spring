package pers.maxlcoder.demo.service;

public class A {

    private B b;

    public A(B b) {
        this.b = b;
    }

    public String getNameOfAFromB() {
        return b.getName();
    }
}
