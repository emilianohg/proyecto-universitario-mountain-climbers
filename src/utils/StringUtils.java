/*
    Author: Emiliano Hernández Guerrero
    No. control: 18170410
    User: emilianohg
*/
package utils;

public class StringUtils {
    public static String[] getUrls (String string, int max) {
        String[] urls = new String[max];
        for (int i=0; i < max; i++) {
            urls[i] = String.format(string, i);
        }
        return urls;
    }
}
