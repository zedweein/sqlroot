package com.awn.sqlroot.monitor;
import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;

import com.awn.sqlroot.SQLRoot;

public class MapperAdaptor extends FileAlterationListenerAdaptor {
    private Logger log = Logger.getLogger(MapperAdaptor.class);
    public void onFileCreate(File file) {
		SQLRoot.mapperCache();
        log.info("file " + file.getName() + " created, rebuild the cache");
    }
    public void onFileChange(File file) {
		SQLRoot.mapperCache();
        log.info("file " + file.getName() + " changed, rebuild the cache");
    }
    public void onFileDelete(File file) {
        log.info("file " + file.getName() + " deleted, rebuild the cache");
    }
    public void onDirectoryCreate(File file) {}
    public void onDirectoryChange(File directory) {}
    public void onDirectoryDelete(File directory) {}
    public void onStart(FileAlterationObserver observer) {
        super.onStart(observer);
    }
    public void onStop(FileAlterationObserver observer) {
        super.onStop(observer);
    }
}