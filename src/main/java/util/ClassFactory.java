package util;

import client.gather.Gather;
import client.send.Send;
import element.Environment;
import service.read.Read;
import service.store.Store;
import util.backup.Backup;
import util.propertiesutil.ClientPro;
import util.propertiesutil.ServicePro;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @program: environmental_detection
 * @package: util
 * @filename: ClassFactory.java
 * @create: 2019/09/23 19:21
 * @author: 29314
 * @description: .通过反射获取类对象的工厂模式
 **/

public class ClassFactory {
    public static Backup getBackup() {
        try {
            Class clazz = Class.forName(ClientPro.getValue("backup"));
            Object o = clazz.newInstance();
            return (Backup) o;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Gather getGather(Queue<Environment> queue, File gatherfile) {
        try {
            Class clazz = Class.forName(ClientPro.getValue("gather"));
            Constructor constructor = clazz.getConstructor(Queue.class, File.class);
            Object o = constructor.newInstance(queue,gatherfile);
            return (Gather) o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Send getSend(Queue<Environment> queue,File sendfile) {
        try {
            Class clazz = Class.forName(ClientPro.getValue("send"));
            Constructor constructor = clazz.getConstructor(Queue.class, File.class);
            Object o = constructor.newInstance(queue,sendfile);
            return (Send) o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Read getRead(Socket socket, Queue<Environment> queue) {
        try {
            Class clazz = Class.forName(ServicePro.getValue("read"));
            Constructor constructor = clazz.getConstructor(Socket.class,Queue.class);
            Object o = constructor.newInstance(socket,queue);
            return (Read) o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Store getStore(Queue<Environment> queue,File storefile) {
        try {
            Class clazz = Class.forName(ServicePro.getValue("store"));
            Constructor constructor = clazz.getConstructor(Queue.class, File.class);
            Object o = constructor.newInstance(queue,storefile);
            return (Store) o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
