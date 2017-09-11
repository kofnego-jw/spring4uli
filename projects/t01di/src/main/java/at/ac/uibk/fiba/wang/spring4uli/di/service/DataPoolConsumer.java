package at.ac.uibk.fiba.wang.spring4uli.di.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataPoolConsumer {

    private final DataPool dataPool;

    @Autowired
    public DataPoolConsumer(DataPool dataPool) throws Exception {
        this.dataPool = dataPool;
        run();
    }

    private void run() throws Exception {
        Long testUserId = dataPool.putUsername("TestUser");
        System.out.println("TestUser has ID of " + testUserId + "! Sie muss 1 sein.");
    }
}
