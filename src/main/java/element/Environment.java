package element;

import java.io.Serializable;
import java.sql.Timestamp;

public class Environment implements Serializable {
    private String src_id; //发送端id
    private String dst_id; //树莓派id
    private String dev_id; //实验箱区域模块（1-8）
    private String sensor_id; //模块上传感器地址
    private Integer counter; //传感器个数
    private Integer cmd_type; //指令标号（3表示需要接收数据，16表示要发送数据）
    private DataType dataType; //数据类型
    private String data; //数据
    private Integer status; //状态标识（默认1表示成功）
    private Timestamp gather_time; //采集时间

    public Environment() {
    }

    public Environment(String src_id, String dst_id, String dev_id, String sensor_id, Integer counter, Integer cmd_type, DataType dataType, String data, Integer status, Timestamp gather_time) {
        this.src_id = src_id;
        this.dst_id = dst_id;
        this.dev_id = dev_id;
        this.sensor_id = sensor_id;
        this.counter = counter;
        this.cmd_type = cmd_type;
        this.dataType = dataType;
        this.data = data;
        this.status = status;
        this.gather_time = gather_time;
    }

    public String getSrc_id() {
        return src_id;
    }

    public void setSrc_id(String src_id) {
        this.src_id = src_id;
    }

    public String getDst_id() {
        return dst_id;
    }

    public void setDst_id(String dst_id) {
        this.dst_id = dst_id;
    }

    public String getDev_id() {
        return dev_id;
    }

    public void setDev_id(String dev_id) {
        this.dev_id = dev_id;
    }

    public String getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(String sensor_id) {
        this.sensor_id = sensor_id;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Integer getCmd_type() {
        return cmd_type;
    }

    public void setCmd_type(Integer cmd_type) {
        this.cmd_type = cmd_type;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getGather_time() {
        return gather_time;
    }

    public void setGather_time(Timestamp gather_time) {
        this.gather_time = gather_time;
    }

    @Override
    public String toString() {
        return "Environment{" +
                "src_id='" + src_id + '\'' +
                ", dst_id='" + dst_id + '\'' +
                ", dev_id='" + dev_id + '\'' +
                ", sensor_id='" + sensor_id + '\'' +
                ", counter=" + counter +
                ", cmd_type=" + cmd_type +
                ", dataType=" + dataType.getName() +
                ", data=" + data +
                ", status=" + status +
                ", gather_time=" + gather_time +
                '}';
    }
}
