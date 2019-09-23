package client.send;

import java.util.Queue;

/**
 * @program: environmental_detection
 * @package: client.send
 * @filename: Send.java
 * @create: 2019/09/19 14:23
 * @author: 29314
 * @description: .发送消息程序实现接口
 **/

public interface Send {
    public void send(Queue queue);
}
