package net.softloaf.automatchic.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    STUDENT("Студент");

    private final String translation;
}
