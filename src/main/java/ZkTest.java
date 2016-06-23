import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

import static java.lang.System.out;

/**
 * Created by cdlvsheng on 2016/5/8.
 */
public class ZkTest {
	public static void main(String[] args) {
		try {
			Watcher watcher = new Watcher() {
				public void process(WatchedEvent event) {
					System.out.println(event.getType() + " is triggered!");
					Event.EventType i = event.getType();
					if (i == Event.EventType.NodeChildrenChanged) {
						out.println(event.getType() + " is triggered!");

					} else if (i == Event.EventType.NodeDeleted) {
						out.println(event.getType() + " is triggered!");

					} else if (i == Event.EventType.NodeCreated) {
						out.println(event.getType() + " is triggered!");

					} else if (i == Event.EventType.None) {
						out.println(event.getType() + " is triggered!");

					} else if (i == Event.EventType.NodeDataChanged) {
						out.println(event.getType() + " is triggered!");

					}
				}
			};
			ZooKeeper    zk       = new ZooKeeper("127.0.0.1:2181", 30, watcher);
			List<String> children = zk.getChildren("/", true);
			out.println(children);
			zk.create("/hly13", "ls".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			zk.register(watcher);
			children = zk.getChildren("/", true);
			out.println(children);
			zk.setData("/hly8", "hly".getBytes(), -1);
			children = zk.getChildren("/", true);
			out.println(children);
			out.println(zk.getState());
			zk.delete("/hly4", -1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		}
	}
}
