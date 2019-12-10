package com.firstyearproject.salontina.Tools;

import javax.servlet.http.HttpSession;

public class SessionLog {

    public static String sessionId(HttpSession session){
        return "\nsession: " + session.getId() + "...";
    }

}
