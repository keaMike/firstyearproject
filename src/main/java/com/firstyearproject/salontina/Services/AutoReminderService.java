package com.firstyearproject.salontina.Services;

import java.util.concurrent.ScheduledFuture;

public interface AutoReminderService {
    ScheduledFuture initiateAutoReminder();
    boolean cancelAutoReminder();
}
