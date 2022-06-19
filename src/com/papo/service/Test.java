package com.papo.service;

import com.papo.spring.PapoApplicationContext;

public class Test {
    public static void main(String[] args) {
        //new bean
        PapoApplicationContext ApplicationContext = new PapoApplicationContext(AppConfig.class);

//        UserService userService = (UserService) ApplicationContext.getBean("userService");

        System.out.println(ApplicationContext.getBean("userService"));
        System.out.println(ApplicationContext.getBean("userService"));
        System.out.println(ApplicationContext.getBean("userService"));
        System.out.println(ApplicationContext.getBean("userService"));


    }
}
