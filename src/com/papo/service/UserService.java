package com.papo.service;

import com.papo.spring.*;

@Component
//@Scope("prototype")
//public class UserService implements BeanNameAware, InitializingBean{
public class UserService implements UserInterface {
    @Autowired
    private OrderService orderService;

    private String beanName;


    public void test(){
        System.out.println(orderService);
    }

//    @Override
//    public void setBeanName(String beanName) {
//        this.beanName = beanName;
//    }
//
//    @Override
//    public void afterPropertiesSet() {
//        System.out.println("初始化init接口实现");
//
//    }
}
