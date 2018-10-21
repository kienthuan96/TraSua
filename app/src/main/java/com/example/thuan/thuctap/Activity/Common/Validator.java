package com.example.thuan.thuctap.Activity.Common;

public class Validator {

    /**
     * check blank of value
     * @param value
     * @return true or false
     */
    public boolean isBlank(String value) {
        if (value.length() == 0) {
            return true;
        }
        return false;
    }
}
