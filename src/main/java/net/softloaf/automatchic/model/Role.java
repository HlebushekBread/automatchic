package net.softloaf.automatchic.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    STUDENT("Студент");

    private final String translation;
}
