package at.ac.uibk.fiba.wang.spring4uli.di.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataPool {

    private final Map<Long, String> registeredUsernames = new ConcurrentHashMap<>();

    public String getUsername(Long id) {
        return registeredUsernames.get(id);
    }

    public Long getId(String username) {
        return registeredUsernames.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(username))
                .findAny()
                .map(entry -> entry.getKey())
                .orElse(null);
    }

    public Long putUsername(String username) throws Exception {
        if (registeredUsernames.containsValue(username))
            throw new Exception("Username already in the DataPool");
        synchronized (this) {
            Long nextId = registeredUsernames.keySet()
                    .stream()
                    .sorted((l1, l2) -> (l1 < l2) ? 1 : -1)
                    .findFirst()
                    .map(l -> l + 1)
                    .orElse(1L);
            registeredUsernames.put(nextId, username);
            return nextId;
        }
    }

}
