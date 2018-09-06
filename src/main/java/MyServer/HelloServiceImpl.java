package main.java.MyServer;

import java.io.Serializable;

/**
 * Created by GEKL on 2018/8/27.
 */
public class HelloServiceImpl implements HelloService,Serializable{

    public String sayHi(String name) {
        return "hi"+name;
    }
}
