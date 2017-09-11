package at.ac.uibk.fiba.wang.spring4uli.di2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class RepoDirConsumer {

    private final File repoDir;

    @Autowired
    public RepoDirConsumer(File repoDir) {
        this.repoDir = repoDir;
        System.out.println("Ich bin RepoDirConsumer!");
        System.out.println("RepoDir is: " + this.repoDir.getAbsolutePath());
    }


}
