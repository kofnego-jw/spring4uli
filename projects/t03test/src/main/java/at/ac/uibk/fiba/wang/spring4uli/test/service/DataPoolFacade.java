package at.ac.uibk.fiba.wang.spring4uli.test.service;

import at.ac.uibk.fiba.wang.spring4uli.di.service.DataPool;
import org.springframework.stereotype.Service;

@Service
public class DataPoolFacade {

    private final DataPool dataPool;

    public DataPoolFacade(DataPool dataPool) {
        this.dataPool = dataPool;
    }

    public boolean isRegistered(String username) {
        return dataPool.getId(username) != null;
    }
}
