package com.firstyearproject.salontina.Tools;

import javax.servlet.http.HttpSession;

public class SessionLog {

    public static String NOTLOGGEDIN = "user is not logged in...";
    public static String NOTADMIN = "user is not admin...";

    public static String sessionId(HttpSession session){
        return "\nsession: " + session.getId() + "...";
    }

}
