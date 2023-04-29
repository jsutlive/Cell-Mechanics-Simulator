package Utilities;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public static String splitPascalCase(String pascalString){
        Pattern WORD_FINDER = Pattern.compile("(([A-Z]?[a-z]+)|([A-Z]))");
        Matcher matcher = WORD_FINDER.matcher(pascalString);
        StringBuilder words = new StringBuilder("");
        while (matcher.find()) {
            words.append(matcher.group(0));
            words.append(" ");
        }
        return words.toString();
    }

    public static String[] parseCSV(String commandLine){
        return commandLine.replace("\uFEFF","").split(",");
    }
}
