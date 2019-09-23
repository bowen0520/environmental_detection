package client;

import client.gather.Gather;
import client.send.Send;
import element.Environment;
import util.ClassFactory;
import util.Log4JFactroy;
import util.propertiesutil.ClientPro;

import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Client {
    private Queue<Environment> queue;
    private File nodefile;
    private File gatherfile;
    private File sendfile;
    public static Boolean book = false;

    public Client(){
        queue = new ConcurrentLinkedDeque<>();
        getFile();
        Gather gather = ClassFactory.getGather(queue,gatherfile);
        new Thread((Runnable) gather,"采集").start();
        Send send = ClassFactory.getSend(queue,sendfile);
        new Thread((Runnable) send,"发送").start();
        System.out.println(gather);
        System.out.println(send);
    }

    public void getFile(){
        nodefile = new File(ClientPro.getValue("nodefile"));
        gatherfile = new File(nodefile,ClientPro.getValue("gatherfile"));
        sendfile = new File(nodefile,ClientPro.getValue("sendfile"));
        try {
            if (!nodefile.exists()) {
                nodefile.mkdirs();
            }
            if(!sendfile.exists()){
                sendfile.createNewFile();
            }
            if(!gatherfile.exists()){
                gatherfile.createNewFile();
            }
        }catch (IOException e){
            Log4JFactroy.getConsoleLog().error(e.getMessage());
            Log4JFactroy.getFileLog().error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
    }
}
