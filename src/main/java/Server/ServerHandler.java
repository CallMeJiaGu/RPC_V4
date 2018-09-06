package main.java.Server;

import main.java.Message.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by GEKL on 2018/9/3.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private HashMap<String,Class> mapClass = new HashMap<String, Class>();

    public void Register(String name,Class c){
        if(!mapClass.containsKey(name)) {
            mapClass.put(name,c);
        }
    }

    //当有信息进入时
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Server 接受到Client的请求");
        Object result = HandlerRequest((Request) msg);
        ctx.write(result);
        System.out.println("Server 成功响应Client请求结果");
    }

    //通知处理器最后的channelread()是当前批处理中的最后一条消息时调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Server 执行ReadComplete..");
        ctx.flush();
    }

    //处理请求
    public Object HandlerRequest(Request reqMessage) throws Exception{
        Class c = mapClass.get(reqMessage.getClassName());
        Method method = c.getMethod(reqMessage.getMethodName(),reqMessage.getTypeParameters());
        Object result = method.invoke(c.newInstance(),reqMessage.getParametersVal());
        return result;
    }

    //读操作时捕获到异常时调用
    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}