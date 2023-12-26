package ru.tanz.pollution.element;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SoilElement {
    private String name;
    private Element element;
    private double concentration;
    private double odk;
    public SoilElement(String name, double concentration) {
        this.name = name;
        this.concentration = concentration;
        Element elementName = Element.Nickel;
        double odk = 0;
        for (Element el : Element.values()){
            if (el.getElement().equalsIgnoreCase(name)){
                odk = el.getOdk();
                elementName = el;
            }
        }
        this.odk = odk;
        this.element = elementName;
    }

    public double calculatePollution() {
        return concentration;
    }
}
