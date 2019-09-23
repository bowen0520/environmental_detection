package util;

import org.apache.log4j.Logger;

/**
 * @program: environmental_detection
 * @package: util
 * @filename: Log4JFactroy.java
 * @create: 2019/09/23 20:27
 * @author: 29314
 * @description: .获取log4j类
 **/

public class Log4JFactroy {
    public static Logger getConsoleLog(){
        return Logger.getLogger("console");
    }

    public static Logger getFileLog(){
        return Logger.getLogger("file");
    }
}
