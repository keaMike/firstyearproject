package com.firstyearproject.salontina.Services;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

//Luca
@Component
public class DatabaseLogger {

    private File logFile = new File("logFile.txt");
    private Date date = new Date();

    public void writeToLogFile(String statement){
        try{
            FileOutputStream fos = new FileOutputStream(logFile, true);
            PrintStream pS = new PrintStream(fos);

            pS.println(date);
            pS.println(statement);
            pS.println();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

}
