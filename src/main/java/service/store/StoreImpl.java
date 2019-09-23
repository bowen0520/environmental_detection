package service.store;

import element.Environment;
import service.MsgHandle;
import util.ClassFactory;
import util.Log4JFactroy;
import util.backup.Backup;
import util.backup.BackupImpl;
import util.propertiesutil.ServicePro;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @program: environmental_detection
 * @package: store
 * @filename: StoreImpl.java
 * @create: 2019/09/17 11:08
 * @author: 29314
 * @description: .入库实现类
 **/

public class StoreImpl implements Store,Runnable{
    private Backup backup;
    private Queue<Environment> queue;
    private File storefile;

    private String driver;
    private String url;
    private String user;
    private String passwd;

    public StoreImpl(Queue<Environment> queue,File storefile) {
        backup = ClassFactory.getBackup();
        System.out.println(backup);
        this.queue = queue;
        this.storefile = storefile;

        driver = ServicePro.getValue("driver");
        url = ServicePro.getValue("url");
        user = ServicePro.getValue("user");
        passwd = ServicePro.getValue("passwd");
    }


    @Override
    public void store(Queue queue) {
        Connection conn = null;
        List<Environment> environments = new ArrayList<>();
        int count = 0;
        try {
            conn = getConn();
            conn.setAutoCommit(false);
            String sql = "insert envs(src_id,dst_id,dev_id,sensor_id,counter,cmd_type,data_type,data,status,gather_time) value(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            while(!(MsgHandle.book&&queue.isEmpty())) {
                if (!queue.isEmpty()) {
                    Object o = queue.poll();
                    Environment environment = (Environment) o;
                    System.out.println(environment);
                    environments.add(environment);
                    ps.setString(1, environment.getSrc_id());
                    ps.setString(2, environment.getDst_id());
                    ps.setString(3, environment.getDev_id());
                    ps.setString(4, environment.getSensor_id());
                    ps.setInt(5, environment.getCounter());
                    ps.setInt(6, environment.getCmd_type());
                    ps.setString(7, environment.getDataType().toString());
                    ps.setString(8, environment.getData());
                    ps.setInt(9, environment.getStatus());
                    ps.setTimestamp(10, environment.getGather_time());
                    ps.addBatch();
                    count++;
                    if(count%1000==0){
                        ps.executeUpdate();
                    }
                }
            }
            if(count%1000!=0){
                ps.executeUpdate();
            }
            conn.commit();
            Log4JFactroy.getConsoleLog().info("入库成功，本次提交了"+count+"条数据");
            Log4JFactroy.getFileLog().info("入库成功，本次提交了"+count+"条数据");
            System.out.println("入库成功，本次提交了"+count+"条数据");
            environments.clear();
        }catch (Exception e){
            System.out.println("入库失败");
            while(!(MsgHandle.book&&queue.isEmpty())) {
                if (!queue.isEmpty()) {
                    Object o = queue.poll();
                    Environment environment = (Environment) o;
                    environments.add(environment);
                }
            }
            if(conn!=null){
                try {
                    conn.rollback();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            Log4JFactroy.getConsoleLog().error(e.getMessage());
            Log4JFactroy.getFileLog().error(e.getMessage());
            e.printStackTrace();
        }
        synchronized (storefile) {
            Object o = backup.load(storefile);
            List<Environment> temp = o==null?new ArrayList<>():(List<Environment>) o;
            System.out.println("需要备份数据" + environments.size() + "条");
            System.out.println("备份文件中原本备份了" + temp.size() + "条");
            environments.addAll(temp);
            Connection connection = null;
            try {
                connection = getConn();
                connection.setAutoCommit(false);
                String sql = "insert envs(src_id,dst_id,dev_id,sensor_id,counter,cmd_type,data_type,data,status,gather_time) value(?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                int fl = 0;
                for(Environment e:environments) {
                    ps.setString(1, e.getSrc_id());
                    ps.setString(2, e.getDst_id());
                    ps.setString(3, e.getDev_id());
                    ps.setString(4, e.getSensor_id());
                    ps.setInt(5, e.getCounter());
                    ps.setInt(6, e.getCmd_type());
                    ps.setString(7, e.getDataType().toString());
                    ps.setString(8, e.getData());
                    ps.setInt(9, e.getStatus());
                    ps.setTimestamp(10, e.getGather_time());
                    ps.addBatch();
                    fl++;
                    if (fl % 1000 == 0) {
                        ps.executeUpdate();
                    }
                }
                ps.executeUpdate();
                connection.commit();
                Log4JFactroy.getConsoleLog().info("入库成功，本次提交了"+fl+"条数据");
                Log4JFactroy.getFileLog().info("入库成功，本次提交了"+fl+"条数据");
                System.out.println("入库成功，本次提交了" + fl + "条数据");
                environments.clear();
            }catch (Exception e){
                if(connection!=null){
                    try {
                        connection.rollback();
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                Log4JFactroy.getConsoleLog().error(e.getMessage());
                Log4JFactroy.getFileLog().error(e.getMessage());
                e.printStackTrace();
            }
            backup.backup(environments, storefile);
            System.out.println("此次存入服务端备份数据" + environments.size() + "条");
        }
    }

    public Connection getConn() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(url, user, passwd);
    }

    @Override
    public void run() {
        store(queue);
    }
}
