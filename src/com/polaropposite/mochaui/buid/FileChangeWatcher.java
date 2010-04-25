package com.polaropposite.mochaui.buid;

import net.contentobjects.jnotify.JNotifyListener;

import java.io.IOException;
import java.util.concurrent.Semaphore;

class FileChangeWatcher implements JNotifyListener {
    Semaphore onlyone = new Semaphore(1);
    String mochaPath;
    boolean fullBuild;
    
    public FileChangeWatcher(String path, boolean force) {
        mochaPath = path;
        fullBuild = force;
    }

    // fires events, and suppresses double messages from OS
    void doChangeNotify(String change, String rootPath, String oldName, String newName) throws InterruptedException {
        if(onlyone.tryAcquire()) {
          (new Thread(new BuildThread(mochaPath,fullBuild))).start();
          onlyone.release();
        }
    }

    public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
        try {
            doChangeNotify("renamed",rootPath,oldName,newName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void fileModified(int wd, String rootPath, String name) {
        try {
            doChangeNotify("modified",rootPath,null,null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void fileDeleted(int wd, String rootPath, String name) {
        try {
            doChangeNotify("deleted",rootPath,null,null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void fileCreated(int wd, String rootPath, String name) {
        try {
            doChangeNotify("created",rootPath,null,null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class BuildThread extends Thread {
    String mochaPath;
    boolean fullBuild;

    public BuildThread(String path, boolean force) {
        mochaPath = path;
        fullBuild = force;
    }

    public void run() {
        try {
            new BuildMochaUI().Build(mochaPath, fullBuild);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

