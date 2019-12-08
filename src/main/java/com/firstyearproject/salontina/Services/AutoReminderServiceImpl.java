package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Repositories.BookingRepoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class AutoReminderServiceImpl implements AutoReminderService {

    //Asbjørn
    //Used in method for scheduling smsReminders. Creates a thread
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SMSServiceImpl smsService;

    @Autowired
    BookingRepoImpl bookingRepo;

    //Asbjørn
    //This method creates a thread and schedules it to repeat every given timeperiod
    @Override
    public ScheduledFuture initiateAutoReminder(){
        log.info("Autoreminder Initiated");
        final Runnable autoReminder = new Runnable() {
            @Override
            public void run() {
                boolean taskResult = bookingRepo.checkSMSReminder();

                log.info("Reminder for tomorrow has been sent? " + taskResult);
                if (!taskResult){
                    log.info("thread task reached");
                    smsService.sendNewsletter("autotest - daily"); //This Method is for testing/demonstration
                    smsService.sendReminder(); //This method is the actual purpose of the autoReminder
                }
            }
        };

        //The first long is the first delay after initialization. The second long is the delay between each execution
        //TimeUnit can be configured to the desired unit of measure. <?> is an unbounded wildcard meaning it can be any type of object
        final ScheduledFuture<?> autoReminderTask = scheduler.scheduleAtFixedRate(autoReminder, 6, 24, TimeUnit.HOURS);

        //The following method is used to limit how long the autoReminder runs but can be manually started from Back-Office
        Runnable autoStop = new Runnable() {
            @Override
            public void run() {autoReminderTask.cancel(true); }
        };
        scheduler.schedule(autoStop, 30, TimeUnit.DAYS);
        return autoReminderTask;
    }

    //Asbjørn
    //Underlying method cancels the thread
    @Override
    public boolean cancelAutoReminder() {
        Runnable cancelRun = new Runnable() {
            @Override
            public void run() {initiateAutoReminder().cancel(true);}
        };
        scheduler.schedule(cancelRun, 1, TimeUnit.SECONDS);
        log.info("Autoreminder cancelled");
    return true;
    }
}