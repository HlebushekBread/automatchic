package net.breadlab.automatchic.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Publicity {

    PRIVATE("Приватный"), PUBLIC("Публичный");

    private final String translation;
}
