package domclick;


import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class LockService {

    private Map<Long, Lock> locks = new ConcurrentHashMap<>();

    public void lockForId(long id) {
        Lock lock = locks.computeIfAbsent(id, (a) -> new ReentrantLock());
        lock.lock();
    }

    public void unlockForId(long id) {
        locks.get(id).unlock();
    }
}
