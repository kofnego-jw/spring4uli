package at.ac.uibk.fiba.wang.spring4uli.di2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class TempDirConsumer {

    private final File tempDir;

    @Autowired
    public TempDirConsumer(File tempDir) {
        this.tempDir = tempDir;
        System.out.println("Ich bin TempDirConsumer!");
        System.out.println("TempDir is: " + this.tempDir.getAbsolutePath());
    }


}
