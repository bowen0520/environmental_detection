package service;

import service.read.ReadImpl;
import util.propertiesutil.ServicePro;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Service {
    private ServerSocket serverSocket;
    private int port;
    private File storefile;

    public Service() throws IOException {
        port= Integer.parseInt(ServicePro.getValue("port"));
        this.serverSocket = new ServerSocket(port);
        storefile = new File(ServicePro.getValue("storefile"));
        storefile.createNewFile();
    }

    public void getConnect()  {
        while(true){
            try {
                Socket accept = serverSocket.accept();
                new MsgHandle(accept,storefile).start();
            }catch (IOException e){
                e.getStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Service().getConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
