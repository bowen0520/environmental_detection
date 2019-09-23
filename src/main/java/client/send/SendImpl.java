package client.send;

import client.Client;
import util.ClassFactory;
import util.Log4JFactroy;
import util.backup.Backup;
import util.backup.BackupImpl;
import element.Environment;
import util.propertiesutil.ClientPro;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @program: environmental_detection
 * @package: client.send
 * @filename: SendImpl.java
 * @create: 2019/09/19 15:15
 * @author: 29314
 * @description: .发送消息的实现类
 **/

public class SendImpl implements Send,Runnable{
    private File sendfile;
    private List<Environment> environments;
    private Backup backup;
    private Queue<Environment> queue;

    private int frequency;
    private int port;
    private String ip;

    public SendImpl(Queue<Environment> queue,File sendfile) {
        this.queue = queue;
        this.sendfile = sendfile;
        this.backup = ClassFactory.getBackup();
        this.frequency = Integer.parseInt(ClientPro.getValue("frequency"));
        port = Integer.parseInt(ClientPro.getValue("serviceport"));
        ip = ClientPro.getValue("serviceip");

        Object o = backup.load(sendfile);
        this.environments = o==null?new ArrayList<>():(List<Environment>) o;
        System.out.println("获取备份数据"+environments.size()+"条");
    }

    @Override
    public void run() {
        send(queue);
        System.out.println("备份数据"+environments.size()+"条");
        backup.backup(environments,sendfile);
    }

    @Override
    public void send(Queue queue) {
        Socket socket = null;
        ObjectOutputStream oos = null;
        int count = 0;
        try {
            socket = new Socket(ip,port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            if(environments.size()!=0) {
                oos.writeObject(environments);
                oos.flush();
                count = count + environments.size();
                environments.clear();
            }
            while (!(Client.book&&queue.isEmpty())) {
                if (!queue.isEmpty()) {
                    Object o = queue.poll();
                    System.out.println(o);
                    environments.add((Environment) o);
                }
                if (environments.size() >= frequency) {
                    oos.writeObject(environments);
                    oos.flush();
                    count = count + environments.size();
                    environments.clear();
                }
            }
            if(environments.size()!=0) {
                oos.writeObject(environments);
                oos.flush();
                count = count + environments.size();
                environments.clear();
            }
        }catch (IOException e){
            while(!(Client.book&&queue.isEmpty())){
                if(!queue.isEmpty()){
                    Object o = queue.poll();
                    environments.add((Environment) o);
                }
            }
            Log4JFactroy.getConsoleLog().error(e.getMessage());
            Log4JFactroy.getFileLog().error(e.getMessage());
            e.printStackTrace();
        }
        Log4JFactroy.getConsoleLog().info("本次发送了"+count+"条数据");
        Log4JFactroy.getFileLog().info("本次发送了"+count+"条数据");
        System.out.println("本次发送了"+count+"条数据");
    }
}
