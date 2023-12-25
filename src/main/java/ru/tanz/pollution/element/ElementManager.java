package ru.tanz.pollution.element;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.tanz.pollution.region.Region;

public class ElementManager {
    public boolean elementExist(String name){
        for (Element el : Element.values()){
            if (el.getElement().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }
    public boolean containElement(Region region, String name){
        for (SoilElement el : region.getElements()){
            if (el.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;

    }
    public SoilElement getElementByName(Region region, String name) {
        return region.getElements()
                .stream()
                .filter(soilElement -> soilElement.getName().equalsIgnoreCase(name))
                .findFirst().get();
    }

}
