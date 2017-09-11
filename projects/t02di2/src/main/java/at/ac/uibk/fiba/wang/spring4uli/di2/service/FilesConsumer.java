package at.ac.uibk.fiba.wang.spring4uli.di2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FilesConsumer {

    private final File tempDir;

    private final File repoDir;

    @Autowired
    public FilesConsumer(File tempDir, File repoDir) {
        this.tempDir = tempDir;
        this.repoDir = repoDir;
        System.out.println("Ich bin FilesConsumer!");
        System.out.println("RepoDir is: " + this.repoDir.getAbsolutePath());
        System.out.println("TempDir is: " + this.tempDir.getAbsolutePath());
    }


}
