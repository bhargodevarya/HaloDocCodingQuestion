import com.api.service.DistMapHandler;
import com.api.service.DistMapHandlerImpl;
import com.api.service.LockManager;
import com.ds.api.DistMap;
import com.ds.api.DistMapImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        DistMap distMap = new DistMapImpl();
        LockManager lockManager = new LockManager();

        DistMapHandler distMapHandler = new DistMapHandlerImpl(lockManager, distMap);
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Boolean> map2 = new HashMap<>();
        map1.put("innerK1", 1);
        map2.put("innerK2", false);

        distMapHandler.put("k1", map1);
        distMapHandler.put("k2", map2);

        //update existing, update innerK2 value for main key k2
        createThread("k2", "innerK2", distMapHandler).start();
        Thread.sleep(500);
        //add new main key k3
        createThread("k3", "innerK5", distMapHandler).start();
        Thread.sleep(500);
        //update existing, existing key k1, add another value innerK4
        createThread("k1", "innerK4", distMapHandler).start();
        Thread.sleep(500);
        //update existing, existing key k2, add another value innerK9
        createThread("k2", "innerK9", distMapHandler).start();
        Thread.sleep(500);
        //update existing, existing key k1, add another value innerK6
        createThread("k1", "innerK6", distMapHandler).start();

        Thread.sleep(3000);

        System.out.println("K1 " + distMapHandler.get("k1"));
        System.out.println("K2 " + distMapHandler.get("k2"));
        System.out.println("K3 " + distMapHandler.get("k3"));

        //TODO, test multiple deletes
        distMapHandler.delete("k2");

        System.out.println(distMap);
    }

    private static Thread createThread(String mainKey, String key, DistMapHandler distMapHandler) {
        return new Thread(() -> {
            Map<String, String> map = new HashMap<>();
            LocalDateTime now = LocalDateTime.now();
            //System.out.println("Current thread is " + Thread.currentThread().getName() + " Time is " + now);
            map.put(key, Thread.currentThread().getName() + now);
            distMapHandler.put(mainKey, map);
        });
    }
}
