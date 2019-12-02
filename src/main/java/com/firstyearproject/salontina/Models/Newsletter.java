package com.firstyearproject.salontina.Models;

public class Newsletter {

    private String text;
    private String testNumber;

    public Newsletter() {
    }

    public Newsletter(String text, String testNumber) {
        this.text = text;
        this.testNumber = testNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(String testNumber) {
        this.testNumber = testNumber;
    }
}
