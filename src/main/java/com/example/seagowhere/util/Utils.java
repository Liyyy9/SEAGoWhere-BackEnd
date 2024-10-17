package com.example.seagowhere.util;

// As a helper/utility class
// Utils is declared public and final, and have a private constructor to prevent instantiation.
// The final keyword prevents the class form being extended (as a subclass) - thus improving runtime-efficiency.
// JavaDoc comments/documentation is used here to provide context on what the methods accept as arguments and what it returns
// The class methods should be static and not be declared abstract (as that would imply the class is not concrete and has to be implemented in some way).

public final class Utils {

    /**
     * This helper method capitalises a string.
     * @param value is the string to be capitalised
     * @return the converted string
     */
    public static String capitalise(String value) {
       return value.substring(0,1).toUpperCase() + value.substring(1);
    }

    /**
     * This helper method extracts a substring after the last delimiter
     * @param value is the string to be extracted from
     * @param delimiter is the delimiter where the string after it is extracted
     * @return converted the substring value
     */
    public static String substringAtLastDelimiter(String value, String delimiter){
        return value.substring(value.lastIndexOf(".") + 1);
    }

}
