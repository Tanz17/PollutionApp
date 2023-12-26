package ru.tanz;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import ru.tanz.pollution.element.Element;
import ru.tanz.pollution.element.ElementManager;
import ru.tanz.pollution.notification.Notification;
import ru.tanz.pollution.recommendation.RecommendationManager;
import ru.tanz.pollution.region.Region;
import ru.tanz.pollution.region.RegionManager;
import ru.tanz.pollution.element.SoilElement;
import ru.tanz.theme.ThemeManager;
import ru.tanz.util.NumberUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

//каллллл
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MainFrame extends JPanel {

    JFrame frame;
    ThemeManager themeManager;
    RegionManager regionManager;
    RecommendationManager recommendationManager;
    ElementManager elementManager;
    JTextField regionTextField;
    JTextField concentrationField;
    JComboBox<String> regionComboBox;
    JComboBox<String> elementsComboBox;
    JSlider concentrationSlider;
    JTextArea resultTextArea;
    JButton addRegionButton;
    JRadioButton jRadioButton;
    JButton addElementButton;
    JButton calculateButton;
    JButton showRecommendation;
    ChartPanel chartPanel;
    @NonFinal
    DefaultCategoryDataset dataset;


    public MainFrame(JFrame frame) {
        this.frame = frame;
        themeManager = new ThemeManager(frame);

        regionManager = new RegionManager();
        recommendationManager = new RecommendationManager();
        elementManager = new ElementManager();
        themeManager.setTheme("Dark");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 6, 5);

        regionTextField = new JTextField(15);
        addComponent(new JLabel("Регион:"), gbc, 0, 0, 1, 1);
        addComponent(regionTextField, gbc, 1, 0, 1, 1);

        addRegionButton = new JButton("Добавить регион");
        addComponent(addRegionButton, gbc, 2, 0, 1, 1);
        addRegionButton.addActionListener(e -> {
            if(regionManager.getRegions().stream().anyMatch(region ->
                    region.getName().equalsIgnoreCase(regionTextField.getText()))){
                val notify = new Notification(frame,
                        Notification.Type.WARNING,
                        Notification.Location.BOTTOM_CENTER,
                        "Данный регион уже добавлен!");
                notify.showNotification();
                return;
            }
            regionManager.addRegion(regionTextField);
            updateRegionComboBox();
        });

        regionComboBox = new JComboBox<>();
        addComponent(new JLabel("Выбрать регион:"), gbc, 0, 1, 1, 1);
        addComponent(regionComboBox, gbc, 1, 1, 1, 1);

        elementsComboBox = new JComboBox<>();
        addComponent(new JLabel("Элемент:"), gbc, 0, 2, 1, 1);
        addComponent(elementsComboBox, gbc, 1, 2, 1, 1);

        for (Element el : Element.values()) {
            elementsComboBox.addItem(el.getElement());
        }

        addElementButton = new JButton("Добавить элемент");
        addComponent(addElementButton, gbc, 2, 2, 1, 1);
        addElementButton.addActionListener(e -> addElement());

        concentrationField = new JTextField(16);
        addComponent(concentrationField, gbc, 2, 3, 1, 1);

        concentrationSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 250);
        addComponent(new JLabel("Установить концентрацию:"), gbc, 0, 3, 1, 1);
        addComponent(concentrationSlider, gbc, 1, 3, 1, 1);

        calculateButton = new JButton("Считать");
        addComponent(calculateButton, gbc, 0, 4, 1, 1);
        calculateButton.addActionListener(e -> calculatePollution());

        jRadioButton = new JRadioButton("Диаграмма регионов по степени загрязнения.");
        jRadioButton.addActionListener(e -> chartChanger());
        addComponent(jRadioButton, gbc, 1, 4, 1, 1);


        showRecommendation = new JButton("Рекомендации");
        addComponent(showRecommendation, gbc, 2, 4, 1, 1);
        showRecommendation.addActionListener(e -> showRecommendationPane());

        chartPanel = new ChartPanel(null);
        chartPanel.setPreferredSize(new Dimension(350, 300));
        addComponent(chartPanel, gbc, 0, 7, 3, 1);

        resultTextArea = new JTextArea(10, 35);
        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        addComponent(scrollPane, gbc, 0, 6, 3, 1);

        concentrationSlider.addChangeListener(e -> updateConcentrationTextField());

        concentrationField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSliderFromTextField();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSliderFromTextField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSliderFromTextField();
            }
        });
    }

    private void chartChanger() {
        if (regionComboBox.getSelectedItem() == null) {
            val notify = new Notification(frame,
                    Notification.Type.WARNING,
                    Notification.Location.BOTTOM_CENTER,
                    "Выберите регион, прежде чем отображать график!");
            notify.showNotification();
            return;
        }
        if(jRadioButton.isSelected()){
            jRadioButton.setText("Диаграмма содержания элементов в регионе.");
            showChartOfElementInRegion();
        } else {

            jRadioButton.setText("Диаграмма регионов по степени загрязнения.");
            showPollutionChartByRegion();
        }

    }

    private void addComponent(Component component, GridBagConstraints gbc,
                              int gridx, int gridy, int gridwidth, int gridheight) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        add(component, gbc);
    }

    //обновить поле концентрации
    private void updateConcentrationTextField() {
        try {
            concentrationField.setText(String.valueOf(concentrationSlider.getValue()));
        } catch (IllegalStateException ex){
            System.out.println(concentrationSlider.getValue() + "   |   " + ex.getMessage());
        }
    }

    //обновить ползунок
    private void updateSliderFromTextField() {
        String text = concentrationField.getText();
        if (NumberUtil.isDouble(text)) {
            double value = Double.parseDouble(text);
            if (value > 500 || value < 0) return;
            concentrationSlider.setValue((int) value);
        }
    }


    //добавить элемент в список региона
    private void addElement() {
        String selectedRegionName = (String) regionComboBox.getSelectedItem();
        String elementName = (String) elementsComboBox.getSelectedItem();
        double concentrationValue = concentrationSlider.getValue();

        Region selectedRegion = regionManager.findRegionByName(selectedRegionName);
        if (selectedRegion != null && !elementName.isEmpty()) {
            SoilElement soilElement = new SoilElement(elementName, concentrationValue);
            if (elementManager.containElement(selectedRegion, elementName)) {
                val element = elementManager.getElementByName(selectedRegion, elementName);
                selectedRegion.getElements().remove(element);
            }
            if (elementManager.elementExist(elementName)) {
                selectedRegion.addElement(soilElement);
                val notify = new Notification(frame,
                        Notification.Type.SUCCESS,
                        Notification.Location.BOTTOM_CENTER,
                        "Вы установили элемент: " + elementName + " с концентрацией: " + concentrationValue);
                notify.showNotification();
            }

        }
    }

    //считать загрязнение и вывести в резы
    private void calculatePollution() {
        String selectedRegionName = (String) regionComboBox.getSelectedItem();
        Region selectedRegion = regionManager.findRegionByName(selectedRegionName);

        if (selectedRegion != null) {
            StringBuilder result = new StringBuilder("Результат вычисления:\n");

            for (SoilElement element : selectedRegion.getElements()) {
                double pollution = element.calculatePollution();
                result.append("Элемент: ").append(element.getName())
                        .append(", Концентрация: ").append(element.getConcentration())
                        .append(", Загрязнение: ").append(pollution)
                        .append(", Превышает ОДК: ").append(pollution > element.getOdk() ? "Да" : "Нет")
                        .append("\n");
            }
            result.append("\n");
            result.append("Zc = ").append(selectedRegion.calculateTotalPollution());

            resultTextArea.setText(result.toString());
        }
    }

    //выводим рекоммендации
    private void showRecommendationPane(){
        val regions = regionManager.getRegions();
        if (regions.isEmpty()){
            val notify = new Notification(frame,
                    Notification.Type.WARNING,
                    Notification.Location.BOTTOM_CENTER,
                    "Сначала добавьте регионы!");
            notify.showNotification();
            return;
        }
        String selectedRegionName = (String) regionComboBox.getSelectedItem();
        Region selectedRegion = regionManager.findRegionByName(selectedRegionName);
        if (selectedRegion.getElements().isEmpty()){
            val notify = new Notification(frame,
                    Notification.Type.WARNING,
                    Notification.Location.BOTTOM_CENTER,
                    "Нету элементов в регионе!");
            notify.showNotification();
            return;
        }
        val pair = recommendationManager.parseRecommendationForZc(selectedRegion.calculateTotalPollution());
        if (pair == null) return;
        JOptionPane.showMessageDialog(frame, pair.getSecond(), pair.getFirst(), JOptionPane.PLAIN_MESSAGE);

    }


    //выводим график загрязнений по регионам
    private void showPollutionChartByRegion() {
        dataset = new DefaultCategoryDataset();
        String selectedRegionName = (String) regionComboBox.getSelectedItem();
        Region selectedRegion = regionManager.findRegionByName(selectedRegionName);

        for (SoilElement element : selectedRegion.getElements()){
            dataset.addValue(element.getConcentration(), element.getName(), element.getName());
        }
        JFreeChart chart = ChartFactory.createBarChart("Содержание элементов в регионе",
                "Элемент", "Общее загрязнение", dataset);
        chartPanel.setChart(chart);
    }

    private void showChartOfElementInRegion(){
        dataset = new DefaultCategoryDataset();
        for (Region region : regionManager.getRegions()) {
            dataset.addValue(region.calculateTotalPollution(), "Загрязнение", region.getName());
        }
        JFreeChart chart = ChartFactory.createBarChart("Уровни загрязнения по регионам",
                "Регион", "Общее загрязнение", dataset);
        chartPanel.setChart(chart);
    }

    //обновить список регионов в выборке
    private void updateRegionComboBox() {
        regionComboBox.removeAllItems();
        for (Region region : regionManager.getRegions()) {
            regionComboBox.addItem(region.getName());
        }
    }

}
