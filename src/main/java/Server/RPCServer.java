package main.java.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;


/**
 * Created by GEKL on 2018/9/3.
 */
public class RPCServer {

    private ServerHandler serverHandler;

    public RPCServer(ServerHandler s){
        this.serverHandler = s;
    }

    public void bind(int port) throws Exception {
        //配置服务端的NIO线程组
        //实际上EventLoopGroup就是Reactor线程组
        //两个Reactor一个用于服务端接收客户端的连接，另一个用于进行SocketChannel的网络读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            /**
             * 由于我们使用在 NIO 传输，我们s
             已指定 NioEventLoopGroup接受和处理新连接，指定 NioServerSocketChannel
             为信道类型。在此之后，我们设置本地地址是 InetSocketAddress 与所选择的端口（6）如。
             服务器将绑定到此地址来监听新的连接请求。
             */
            //ServerBootstrap对象是Netty用于启动NIO服务端的辅助启动类，目的是降低服务端开发的复杂度
            ServerBootstrap b = new ServerBootstrap();
            //Set the EventLoopGroup for the parent (acceptor) and the child (client).
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    //绑定I/O事件的处理类ChildChannelHandler,作用类似于Reactor模式中的Handler类
                    //主要用于处理网络I/O事件，例如记录日志，对消息进行编解码等
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ObjectDecoder objectDecoder = new ObjectDecoder(1024 * 1024,
                                    ClassResolvers.weakCachingConcurrentResolver(this
                                            .getClass().getClassLoader()));
                            //因为传输过来是Object，所以需要解码与编码Handler.
                            ch.pipeline().addLast(objectDecoder);
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            //绑定监听端口，调用sync同步阻塞方法等待绑定操作完成，完成后返回ChannelFuture类似于JDK中Future
            System.out.println("Server 服务启动中...");
            ChannelFuture f = b.bind(port).sync();
            System.out.println("Server 等到事件响应...");
            //使用sync方法进行阻塞，等待服务端链路关闭之后Main函数才退出
            f.channel().closeFuture().sync();
        }finally {
            //优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
