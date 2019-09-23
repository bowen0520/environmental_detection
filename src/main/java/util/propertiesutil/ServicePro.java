package util.propertiesutil;

import java.io.IOException;
import java.util.Properties;

/**
 * @program: environmental_detection
 * @package: util.propertiesutil
 * @filename: ServicePro.java
 * @create: 2019/09/19 18:16
 * @author: 29314
 * @description: .获取Service端的配置文件信息
 **/

public class ServicePro {
    private static Properties util = new Properties();

    static {
        try {
            util.load(ClientPro.class.getResourceAsStream("/service.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key){
        return util.getProperty(key);
    }
}
