package org.osivia.services.procedure;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PatternTest {

    private static final Pattern objectPattern = Pattern.compile("^ecm:\\/\\/([\\w.]+)\\/([\\w.:]+)");

    public static void main(String[] args) {

        String valueToTest = "ecm://document/dc:title";

        Matcher matcher = objectPattern.matcher(valueToTest);

        if (matcher.matches()) {
            System.out.println("match");
            String objectName = matcher.group(1);
            String objectProperty = matcher.group(2);

            System.out.println("value1:" + objectName);
            System.out.println("value2:" + objectProperty);
        } else {
            System.out.println("no match");
        }
    }

}
