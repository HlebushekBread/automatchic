package net.breadlab.automatchic.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TaskType {

    HOMEWORK("ДЗ"), LABWORK("ЛР"), TEST("КР");

    private final String translation;
}