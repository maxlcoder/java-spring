package pers.maxlcoder.demo.service;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class B {

    private String name;

    public B(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
