package net.softloaf.automatchic.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GradingType {

    CREDIT("Зачет"), GRADE("Оценка"), EXAM("Экзамен");

    private final String translation;
}