package com.api.service.flush;

import com.api.service.file.FileWriter;
import com.ds.api.DistMap;

//TODO, not tested
/*runs periodically, configurable, and flushes the in memory
structure to disc*/
public class FlushToDisk implements Flush<DistMap> {

    private DistMap distMap;
    private FileWriter fileWriter;

    public FlushToDisk(DistMap distMap, FileWriter fileWriter) {
        this.distMap = distMap;
        this.fileWriter = fileWriter;
    }

    public DistMap getDistMap() {
        return distMap;
    }

    public void start() {
        Thread th = new Thread(
                () -> {
                    while (true) {
                        flush(getDistMap());
                        try {
                            //should be some configurable number
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        th.start();
    }

    //TODO write to disk
    @Override
    public void flush(DistMap distMap) {
        fileWriter.write(distMap);
    }
}
