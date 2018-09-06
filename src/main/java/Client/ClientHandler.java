package main.java.Client;

import main.java.Message.Request;
import main.java.Message.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.CountDownLatch;

/**
 * Created by GEKL on 2018/9/3.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private Response response = new Response("初始值",true);
    private Request reqMessage;

    public ClientHandler(Request r){
        this.reqMessage = r;
    }

    public Response getResponse(){
        return response;
    }

    public CountDownLatch getCountDownLatch(){
        return countDownLatch;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("Client 与Server建立链接");
        ctx.writeAndFlush(reqMessage);
        System.out.println("Client 请求成功");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println("Client 收到Server的结果"+msg);
        this.response = new Response(msg,true);
        this.countDownLatch.countDown();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}