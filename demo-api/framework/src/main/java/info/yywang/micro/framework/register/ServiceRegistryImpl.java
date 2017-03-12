package info.yywang.micro.framework.register;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author shengguo
 * @version 1.0
 * @date 2017-03-06 23:44
 */
@Component
public class ServiceRegistryImpl implements ServiceRegistry, Watcher {

    private static Logger logger = LoggerFactory.getLogger(ServiceRegistryImpl.class);

    private static CountDownLatch latch = new CountDownLatch(1);

    private ZooKeeper zk;

    private final static int TIME_OUT = 5000000;

    private static final String REGISTER_PATH = "/home";

    public ServiceRegistryImpl(String zkServers) {
        try {
            zk = new ZooKeeper(zkServers, TIME_OUT, this);
            latch.await();
            logger.info("connect to zookeeper success.");
        } catch (IOException e) {
            logger.error("create zookeeper client failed.", e);
        } catch (InterruptedException e) {
            logger.error("create zookeeper client failed.", e);
        }
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        String registerPath = REGISTER_PATH;
        try {
            //创建根节点
            if (zk.exists(registerPath, false) == null) {
                zk.create(registerPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.info("create register node:{}", registerPath);
            }
            //创建服务节点
            String servicePath = registerPath + "/" + serviceName;
            if (zk.exists(servicePath, false) == null) {
                zk.create(servicePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.info("create service node:{}", registerPath);
            }
            //创建服务地址节点  临时节点
            String serviceAddressPath = servicePath + "/address-";
            String addressNode = zk.create(serviceAddressPath, serviceAddress.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.info("create address note: {}=>{}", addressNode, serviceAddress);

        } catch (KeeperException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
        }
    }


}
