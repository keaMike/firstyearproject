package com.firstyearproject.salontina.Tools;

import com.firstyearproject.salontina.Models.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class UserAuthenticator {
    //Jonathan
    public boolean userIsAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            for (String s : user.getUserRoles()) {
                if (s.equals("Admin") || s.equals("Frisør")) {
                    return true;
                }
            }
        }
        return false;
    }

    //Jonathan
    //As no users in the database has the role "Bruger" yet, this method simply checks if they are not null.
    public boolean userIsUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            //for (String s : user.getUserRoles()) {
            //    if (s.equals("Bruger")) {
            //        return true;
            //    }
            //}
            return true;
        }
        return false;

    }

}
