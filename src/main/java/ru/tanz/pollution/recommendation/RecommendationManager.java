package ru.tanz.pollution.recommendation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.tanz.util.Pair;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecommendationManager {
    Pair<String, String> CATEGORY_ONE = new Pair<>("Категория I",
            "Снижение уровня воздействия источников загрязнения почв. Осуществление мероприятий по снижению доступности токсикантов для растений (известкование, внесение органических удобрений)");
    Pair<String, String> CATEGORY_TWO = new Pair<>("Категория II",
            "Снижение уровня воздействия источников загрязнения почв. Осуществление мероприятий по снижению доступности токсикантов для растений (известкование, внесение органических удобрений)");
    Pair<String, String> CATEGORY_THREE = new Pair<>("Категория III",
            "Кроме мероприятий, указанных для категории I, обязательный контроль за содержанием токсикантов в растениях, используемых в качестве продуктов питания и кормов");
    Pair<String, String> CATEGORY_FOUR = new Pair<>("Категория IV",
            "Мероприятия по снижению уровня загрязнения и связыванию токсикантов в почвах. Контроль за содержанием токсикантов в зоне дыхания рабочих, в поверхностных и подземных водах");

    public Pair<String, String> parseRecommendationForZc(double zc){
        if (zc < 16){
            return CATEGORY_ONE;
        } else if (zc >= 16 && zc <= 32) {
            return CATEGORY_TWO;
        } else if (zc >= 32 && zc <= 128) {
            return CATEGORY_THREE;
        } else {
            return CATEGORY_FOUR;
        }
    }
}
