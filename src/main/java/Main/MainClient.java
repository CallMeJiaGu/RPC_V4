package main.java.Main;

import main.java.MyServer.HelloService;
import main.java.ProxyHandler.NettyInvokeHandler;

import java.lang.reflect.Proxy;



/**
 * Created by GEKL on 2018/9/3.
 */
public class MainClient {

    public static void main(String[] args) throws Exception {
        NettyInvokeHandler invokeHandler = new NettyInvokeHandler(HelloService.class);
        HelloService helloService = (HelloService) Proxy.newProxyInstance(HelloService.class.getClassLoader(),new Class<?>[]{HelloService.class},invokeHandler);
        String result = helloService.sayHi("123") ;
        System.out.println("结果："+result);
    }
}
