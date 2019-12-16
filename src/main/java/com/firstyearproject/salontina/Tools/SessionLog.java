package com.firstyearproject.salontina.Tools;

import javax.servlet.http.HttpSession;

/**
 * Luca
 * Used to note which session is active
 */
public class SessionLog {

    public static String NOTLOGGEDIN = "user is not logged in...";
    public static String NOTADMIN = "user is not admin...";

    /**
     * Luca
     */
    public static String sessionId(HttpSession session){
        return "\nsession: " + session.getId() + "...";
    }

}
