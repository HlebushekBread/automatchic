package net.softloaf.automatchic.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TaskType {

    HOMEWORK("ДЗ"), LABWORK("ЛР"), TEST("КР"),
    PROJECT("Проект"), PRESENTATION("Доклад"), EXAM("Экзамен"),
    PRACTICE("Практика"), PARTICIPATION("Активность"), ATTENDANCE("Посещаемость"),
    OTHER("Другое");

    private final String translation;
}