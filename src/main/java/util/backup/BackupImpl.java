package util.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @program: environmental_detection
 * @package: backup
 * @filename: BackupImpl.java
 * @create: 2019/09/19 09:13
 * @author: 29314
 * @description: .备份程序实现类
 **/

public class BackupImpl implements Backup{

    @Override
    public void backup(Object o, File file, boolean append) {
        try {
            FileOutputStream fos = new FileOutputStream(file,append);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) {
            System.out.println("备份文件写入错误："+e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void backup(Object o, File file) {
        backup(o,file,false);
    }

    @Override
    public Object load(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            ois.close();
            fis.close();
            return o;
        } catch (Exception e) {
            System.err.println("备份文件"+file.getName()+"读取错误："+e.getMessage());
            System.err.println("可能是第一次读取文件中没有内容,无法找到引用类型对象,因为写入的不管是null还是\"\"在文件内都应该有内容");
            return null;
        }
    }
}
