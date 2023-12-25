package ru.tanz.pollution.region;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegionManager {
    List<Region> regions;

    public RegionManager() {
        this.regions = new ArrayList<>();
    }

    public void addRegion(JTextField regionTextField) {
        String regionName = regionTextField.getText();
        if (!regionName.isEmpty()) {
            regions.add(new Region(regionName));
//            updateRegionComboBox();
            regionTextField.setText("");
        }
    }
    public Region findRegionByName(String name) {
        for (Region region : regions) {
            if (region.getName().equals(name)) {
                return region;
            }
        }
        return null;
    }
}
