package com.iucosoft.eavertizare.util;

import com.iucosoft.eavertizare.gui.MainJFrame;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class RunMeJob extends QuartzJobBean {

 //   private RunMeTask runMeTask;
    private MainJFrame runMeTask;

    public void setRunMeTask(MainJFrame runMeTask) {
        this.runMeTask = runMeTask;
    }

    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {

        runMeTask.autoRun();

    }

//    http://www.mkyong.com/tutorials/quartz-scheduler-tutorial/
}
