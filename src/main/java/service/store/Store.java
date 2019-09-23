package service.store;

import element.Environment;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

/**
 * @program: environmental_detection
 * @package: store
 * @filename: Store.java
 * @create: 2019/09/17 11:06
 * @author: 29314
 * @description: .入库实现接口
 **/

public interface Store {
    public void store(Queue queue);
}
