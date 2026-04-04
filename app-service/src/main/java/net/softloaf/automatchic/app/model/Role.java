package net.softloaf.automatchic.app.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    STUDENT("Студент");

    private final String translation;
}
