package com.example.project162.Activity;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Utils {
    public static String removeDiacritics(String text) {
        if (text == null) {
            return null;
        }
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }
}
