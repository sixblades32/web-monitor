package io.enigmasolutions.stuffmanager.services.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UrlExtractor {

    public List<String> extractURL(
            String str) {

        List<String> list
                = new ArrayList<>();

        String regex
                = "\\b((?:https?|ftp|file):"
                + "//[-a-zA-Z0-9+&@#/%?="
                + "~_|!:, .;]*[-a-zA-Z0-9+"
                + "&@#/%=~_|])";

        Pattern p = Pattern.compile(
                regex,
                Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(str);

        while (m.find()) {
            String substring = str.substring(
                    m.start(0), m.end(0));

            list.add(substring);
        }

        return list;
    }
}
