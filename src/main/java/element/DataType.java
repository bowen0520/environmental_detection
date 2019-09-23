package element;

/**
 * @program: environmental_detection
 * @package: element
 * @filename: DataType.java
 * @create: 2019/09/17 09:37
 * @author: 29314
 * @description: .数据类型枚举
 **/

public enum DataType {
    TEMP("温度"),HUM("湿度"),ILL("光照强度"),CO2("二氧化碳浓度");

    private String name;

    DataType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
