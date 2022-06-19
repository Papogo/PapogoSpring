package com.papo.spring;

public class BeanDefinition {

    private Class type;
    private  String scope;

    public Class getType() {
        return type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
