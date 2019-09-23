package service.read;

import element.Environment;
import service.MsgHandle;
import util.Log4JFactroy;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.Queue;

/**
 * @program: environmental_detection
 * @package: service.read
 * @filename: ReadImpl.java
 * @create: 2019/09/19 19:55
 * @author: 29314
 * @description: .读取服务端备份文件数据实现类
 **/

public class ReadImpl implements Read,Runnable{
    private Socket socket;
    private Queue<Environment> queue;

    public ReadImpl(Socket socket,Queue<Environment> queue) {
        this.socket = socket;
        this.queue = queue;
    }

    @Override
    public void read(Queue queue) {
        ObjectInputStream ois = null;

        int count = 0;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Object o = ois.readObject();
                if(o!=null&&o instanceof List){
                    List<Environment> temp = (List<Environment>) o;
                    count = count+temp.size();
                    for(Environment environment:temp){
                        queue.offer(environment);
                    }
                }
            }
        } catch (Exception e) {
            Log4JFactroy.getConsoleLog().info(e.getMessage());
            Log4JFactroy.getFileLog().info(e.getMessage());
            e.printStackTrace();
        }
        Log4JFactroy.getConsoleLog().info("接收到客户端"+count+"条数据");
        Log4JFactroy.getFileLog().info("接收到客户端"+count+"条数据");
        System.out.println("接收到客户端"+count+"条数据");
        MsgHandle.book = true;
    }

    @Override
    public void run() {
        read(queue);
    }
}
