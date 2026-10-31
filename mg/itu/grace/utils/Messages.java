package mg.itu.grace.utils;

import mg.itu.grace.exception.DuplicatedUrlException;

import java.util.HashMap;
import java.util.Map;

public class Messages {
    private String[] lang = {"eng", "fr"};
    public static Map<Class<?>, Map<String, String[]>> messages = new HashMap<>();

    public Messages (){
        messages.put(DuplicatedUrlException.class, setDuplicatedUrlMessages());
    }

    private Map<String, String[]> setDuplicatedUrlMessages(){
        Map<String, String[]> map = new HashMap<>();
        String[][] table = new String[lang.length][3];

        table[0][0] = "Duplicate URL mapping found for: ";
        table[0][1] = " in Class ";
        table[0][2] = " method ";

        table[1][0] = "URL mapping duplique pour: ";
        table[1][1] = " dans la Class ";
        table[1][2] = " method ";

        for (int i = 0; i < lang.length; i++) {
            map.put(lang[i], table[i]);
        }

        return map;
    }
}
