package net.softloaf.automatchic.app.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Publicity {

    PRIVATE("Приватный"), PUBLIC("Публичный");

    private final String translation;
}
