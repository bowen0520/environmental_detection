package client.gather;

import client.Client;
import util.ClassFactory;
import util.Log4JFactroy;
import util.backup.Backup;
import util.backup.BackupImpl;
import element.DataType;
import element.Environment;
import util.propertiesutil.ClientPro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Queue;

/**
 * @program: environmental_detection
 * @package: gather
 * @filename: GatherImpl.java
 * @create: 2019/09/17 09:32
 * @author: 29314
 * @description: .Gather接口实现类
 **/

public class GatherImpl implements Gather,Runnable{
    private File gatherfile;
    private File resourcesfile;
    private int line;
    private Backup backup;
    private Queue<Environment> queue;

    public GatherImpl(Queue<Environment> queue,File gatherfile) {
        this.queue = queue;
        this.gatherfile = gatherfile;
        this.backup = ClassFactory.getBackup();
        Object o = backup.load(gatherfile);
        this.line = o==null?0:(Integer) o;
        this.resourcesfile = new File(ClientPro.getValue("resoursefile"));
    }

    @Override
    public void run() {
        System.out.println("开始解析资源文件");
        if(!resourcesfile.exists()){
            Log4JFactroy.getConsoleLog().info("资源文件不存在");
            Log4JFactroy.getFileLog().info("资源文件不存在");
            System.out.println("资源文件不存在");
        }else {
            gather(queue);
        }
        Client.book = true;
        backup.backup(line,gatherfile);
        System.out.println("采集端:"+Client.book);
    }

    @Override
    public void gather(Queue queue) {
        int count = 0;
        int num = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(resourcesfile)));
            String msg = null;
            System.out.println("从第"+line+"行开始解析文件");
            while ((msg = br.readLine()) != null) {
                count++;
                if(count>line) {
                    String[] msgs = msg.split("[|]");
                    if (msgs.length == 9) {
                        if ("16".equals(msgs[3])) {
                            Environment e1 = new Environment();
                            getCommon(e1, msgs);
                            e1.setDataType(DataType.TEMP);
                            int value1 = Integer.parseInt(msgs[6].substring(0, 4), 16);
                            float temp = (float) (value1 * 0.00268127 - 46.85);
                            e1.setData(String.format("%.2f", temp));

                            Environment e2 = new Environment();
                            getCommon(e2, msgs);
                            e2.setDataType(DataType.HUM);
                            int value2 = Integer.parseInt(msgs[6].substring(4, 8), 16);
                            float hum = (float) (value2 * 0.00190735 - 6);
                            e2.setData(String.format("%.2f", hum));

                            queue.offer(e1);
                            queue.offer(e2);
                            num+=2;
                        }

                        if ("256".equals(msgs[3])) {
                            Environment e = new Environment();
                            getCommon(e, msgs);
                            e.setDataType(DataType.ILL);
                            int ill = Integer.parseInt(msgs[6].substring(0, 4), 16);
                            e.setData(String.valueOf(ill));

                            queue.offer(e);
                            num++;
                        }

                        if ("1280".equals(msgs[3])) {
                            Environment e = new Environment();
                            getCommon(e, msgs);
                            e.setDataType(DataType.CO2);
                            int co2 = Integer.parseInt(msgs[6].substring(0, 4), 16);
                            e.setData(String.valueOf(co2));

                            queue.offer(e);
                            num++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            Log4JFactroy.getConsoleLog().error(e.getMessage());
            Log4JFactroy.getFileLog().error(e.getMessage());
            e.printStackTrace();
        }
        System.out.println("本次读取资源文件"+(count-line)+"行");
        System.out.println("从资源文件中获得"+num+"条数据");
        line = count;
        if(br!=null){
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void getCommon(Environment e,String[] msgs){
        e.setSrc_id(msgs[0]);
        e.setDst_id(msgs[1]);
        e.setDev_id(msgs[2]);
        e.setSensor_id(msgs[3]);
        e.setCounter(Integer.parseInt(msgs[4]));
        e.setCmd_type(Integer.parseInt(msgs[5]));
        e.setStatus(Integer.parseInt(msgs[7]));
        e.setGather_time(new Timestamp(Long.parseLong(msgs[8])));
    }
}
