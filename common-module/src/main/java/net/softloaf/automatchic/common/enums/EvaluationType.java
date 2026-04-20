package net.softloaf.automatchic.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EvaluationType {

    TOTAL("Сумма"), AVERAGE("Средняя");

    private final String translation;
}
