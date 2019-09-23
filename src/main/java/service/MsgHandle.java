package service;

import element.Environment;
import service.read.Read;
import service.store.Store;
import util.ClassFactory;

import java.io.File;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class  MsgHandle extends Thread{
    private Socket socket;
    private File storefile;
    private Queue<Environment> queue;

    public static boolean book = false;

    public MsgHandle(Socket socket,File storefile){
        this.socket = socket;
        this.storefile = storefile;
        this.queue = new ConcurrentLinkedDeque<>();
    }

    @Override
    public void run() {
        System.out.println(this.getName()+":开始获取客户端上传的数据");
        Read read = ClassFactory.getRead(socket,queue);
        Store store = ClassFactory.getStore(queue,storefile);
        System.out.println(read);
        System.out.println(store);
        new Thread((Runnable) read).start();
        new Thread((Runnable) store).start();
    }
}
