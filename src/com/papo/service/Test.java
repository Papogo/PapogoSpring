package com.papo.service;

import com.papo.spring.PapoApplicationContext;

public class Test {
    public static void main(String[] args) {
        //new bean
        PapoApplicationContext ApplicationContext = new PapoApplicationContext(AppConfig.class);

        UserInterface userService = (UserInterface) ApplicationContext.getBean("userService");
        userService.test();



    }
}
