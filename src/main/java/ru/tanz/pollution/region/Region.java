package ru.tanz.pollution.region;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.tanz.pollution.element.SoilElement;

import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Region {
    String name;
    List<SoilElement> elements;

    public Region(String name) {
        elements = new ArrayList<>();
        this.name = name;
    }

    public void addElement(SoilElement element) {
        elements.add(element);
    }

    public double calculateTotalPollution() {
        double totalConcentration = 0.0;

        for (SoilElement element : elements) {
            totalConcentration += (element.getConcentration() / element.getOdk());
        }

        int n = elements.size();
        double Zc = totalConcentration - (n - 1);
        return Zc;
    }
}
