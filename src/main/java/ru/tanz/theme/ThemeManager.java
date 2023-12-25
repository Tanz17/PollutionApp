package ru.tanz.theme;


import com.formdev.flatlaf.FlatLaf;
import com.jthemedetecor.OsThemeDetector;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tanz.theme.custom.Dark;
import ru.tanz.theme.custom.Light;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.NoSuchElementException;

@Getter
public class ThemeManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThemeManager.class);

    private boolean isDark;
    private final Component window;

    public ThemeManager(Component window) {
        this.window = window;

        // свои темки
        // https://www.formdev.com/flatlaf/theme-editor/
        Light.installLafInfo();
        Dark.installLafInfo();

        // другие темки так ставятся
        // FlatGitHubDarkContrastIJTheme.installLafInfo();

        final OsThemeDetector detector = OsThemeDetector.getDetector();
        var selectedTheme = detector.isDark() ? "Dark" : "Light";
        setTheme(selectedTheme);
    }

    public void setTheme(String theme) {
        try {
            var themeInfo = Arrays.stream(UIManager.getInstalledLookAndFeels())
                    .filter(x -> x.getName().equals(theme))
                    .findFirst()
                    .orElseThrow();

            var clazz = (Class<FlatLaf>) Class.forName(themeInfo.getClassName());
            var instance = clazz.getDeclaredConstructor().newInstance();

            isDark = instance.isDark();

            FlatLaf.setup(instance);
            FlatLaf.updateUI();
        } catch (NoSuchElementException | NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                 ClassNotFoundException | InstantiationException e) {
            LOGGER.error("Unable to set FlatLaf theme", e);
            setSystemLookAndFeel();
        }
    }

    private void setSystemLookAndFeel() {
        try {
            JFrame.setDefaultLookAndFeelDecorated(false);
            JDialog.setDefaultLookAndFeelDecorated(false);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(window);
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            LOGGER.error("Unable to set system look and feel", e);
        }
    }
}
