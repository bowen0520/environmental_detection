package util.propertiesutil;

import java.io.IOException;
import java.util.Properties;

/**
 * @program: environmental_detection
 * @package: propertiesutil
 * @filename: GatherPro.java
 * @create: 2019/09/18 14:26
 * @author: 29314
 * @description: .获取Client端的配置文件信息
 **/

public class ClientPro {
    private static Properties util = new Properties();

    static {
        try {
            util.load(ClientPro.class.getResourceAsStream("/client.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key){
        return util.getProperty(key);
    }

    //修改properties文件中key对应的值
    /*public static void setValue(String key,String value) throws IOException {
        util.setProperty(key,value);
        util.store(new FileOutputStream("src\\main\\resources\\client.properties"),"");
    }*/

    //测试
    /*public static void main(String[] args) throws IOException {
        System.out.println(getValue("line"));
        setValue("line","2");
        System.out.println(getValue("line"));
    }*/
}
