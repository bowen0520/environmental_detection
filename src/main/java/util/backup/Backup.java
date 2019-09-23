package util.backup;

import java.io.File;

/**
 * @program: environmental_detection
 * @package: backup
 * @filename: Backup.java
 * @create: 2019/09/19 09:09
 * @author: 29314
 * @description: .备份程序接口
 **/

public interface Backup {
    //将o备份到文件
    public void backup(Object o, File file,boolean append);
    public void backup(Object o, File file);

    //将备份文件内容提取
    public Object load(File file);
}
