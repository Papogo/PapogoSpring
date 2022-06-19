package com.papo.service;

import com.papo.spring.Component;
import com.papo.spring.Scope;

//给bean指定名字
@Component("userService")
@Scope("prototype")
//prototype是多例
public class UserService {
}
