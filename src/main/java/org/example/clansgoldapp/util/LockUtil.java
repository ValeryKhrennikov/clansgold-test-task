package org.example.clansgoldapp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockUtil {

    private static final Object lockMapLock = new Object();
    private static final Map<Long, Lock> lockMap = new HashMap<>();

    public static Lock getLock(Long clanId) {
        synchronized (lockMapLock) {
            return lockMap.computeIfAbsent(clanId, k -> new ReentrantLock());
        }
    }
}
