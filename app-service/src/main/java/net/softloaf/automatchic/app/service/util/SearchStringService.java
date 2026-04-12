package net.softloaf.automatchic.app.service.util;

import net.softloaf.automatchic.app.model.Subject;
import net.softloaf.automatchic.app.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchStringService {
    public final String separator = "|";
    public final List<String> ignoredChars = List.of(".", ",", "-", "!", "(", ")");

    public String getSearchString(Subject subject) {
        String base = subject.getName() + separator +
                getAbbreviation(subject.getName()) + separator +
                subject.getTeacher() + separator +
                getAbbreviation(subject.getTeacher()) + separator +
                subject.getDescription();
        return clean(base);
    }

    public String getSearchString(User user) {
        String base = user.getUsername() + separator +
                user.getFullName() + separator +
                getAbbreviation(user.getFullName()) + separator +
                user.getGroup();
        return clean(base);
    }

    public String clean(String text) {
        if (text == null) return "";
        String result = text.toLowerCase();

        for (String ch : ignoredChars) {
            result = result.replace(ch, "");
        }

        return result.replaceAll("\\s+", "");
    }

    private String getAbbreviation(String text) {
        if (text == null || text.isBlank()) return "";
        return Arrays.stream(text.split("\\s+"))
                .filter(word -> !word.isEmpty())
                .map(word -> String.valueOf(word.charAt(0)))
                .collect(Collectors.joining());
    }
}
