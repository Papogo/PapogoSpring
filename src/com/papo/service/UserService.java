package com.papo.service;

import com.papo.spring.*;

//给bean指定名字
@Component("userService")
@Scope("prototype")
//prototype是多例
public class UserService implements BeanNameAware, InitializingBean {
    @Autowired
    private OrderService orderService;

    private String beanName;



    public void test(){
        System.out.println(orderService);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("初始化init接口实现");

    }
}
