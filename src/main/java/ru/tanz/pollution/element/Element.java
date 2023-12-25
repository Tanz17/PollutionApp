package ru.tanz.pollution.element;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Element {
    Nickel("Никель", 20),
    Copper("Медь", 33),
    Zinc("Цинк", 55),
    Arsenic("Мышьяк", 2),
    Cadmium("Кадмий", 0.5),
    Lead("Свинец", 32);
    String element;
    double odk;

}
