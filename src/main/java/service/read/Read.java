package service.read;

import java.io.File;
import java.sql.Connection;
import java.util.Queue;

/**
 * @program: environmental_detection
 * @package: service.read
 * @filename: read.java
 * @create: 2019/09/19 19:51
 * @author: 29314
 * @description: .读取服务端备份文件程序接口
 **/

public interface Read {
    public void read(Queue queue);
}
