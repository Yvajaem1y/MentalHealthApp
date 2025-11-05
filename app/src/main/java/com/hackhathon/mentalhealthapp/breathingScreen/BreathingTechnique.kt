package com.hackhathon.mentalhealthapp.breathingScreen

import androidx.compose.ui.graphics.Color

data class BreathingTechnique(
    val id: Int,
    val name: String,
    val description: String,
    val tuter : String,
    var recommendedCycles : Int,
    val pattern: BreathingPattern,
    val colors : List<Color>
)

data class BreathingPattern(
    val inhale: Int,
    val hold: Int,
    val exhale: Int,
    val holdAfterExhale: Int
)

object BreathingTechniques {
    val techniques = listOf(
        BreathingTechnique(
            id = 1,
            name = "Дыхание 4–7–8",
            description = "Популярная техника для быстрого расслабления и снижения тревоги",
            tuter = "Вдыхай на 4 счета, задержи дыхание на счет 7, выдохни на счет 8. \n" +
                    "Самый важный компонент техники заключается в том, что выдох примерно в два раза длиннее вдоха.",
            pattern = BreathingPattern(inhale = 4, hold = 7, exhale = 8, holdAfterExhale = 0),
            recommendedCycles = 8,
            colors = listOf(Color(191, 125, 249),Color(160, 140, 230),Color(163, 138, 231))
        ),
        BreathingTechnique(
            id = 2,
            name = "Квадратное дыхание",
            description = "Помогает снять стресс и тревожность, успокоиться и сосредоточиться на текущем моменте.",
            tuter = "Вдохни на 4 счёта, задержи дыхание на 4, выдохни на 4, снова задержи дыхание на 4. Повтори несколько циклов, удерживая равный ритм.",
            pattern = BreathingPattern(inhale = 4, hold = 4, exhale = 4, holdAfterExhale = 4),
            recommendedCycles = 8,
            colors = listOf(Color(127, 187, 114),Color(74, 167, 134),Color(127, 187, 114))
        ),
        BreathingTechnique(
            id = 3,
            name = "Удлинённый выдох",
            description = "Помогает быстро снять внутреннее напряжение и снизить частоту сердечных сокращений.",
            tuter = "Сделай спокойный вдох на 3–4 счёта и длинный выдох на 6–8. Почувствуй, как тело становится тяжелее и спокойнее с каждым циклом.",
            pattern = BreathingPattern(inhale = 4, hold = 2, exhale = 8, holdAfterExhale = 0),
            recommendedCycles = 8,
            colors = listOf(Color(234, 154, 135),Color(231, 140, 145),Color(202, 128, 179))
        ),
        BreathingTechnique(
            id = 4,
            name = "Треугольное дыхание",
            description = "Для восстановления равновесия и концентрации.",
            tuter = "Вдохни на 4 счёта, задержи дыхание на 4, выдохни на 4. Представляй, как с каждым циклом ты рисуешь ровный треугольник дыханием.",
            pattern = BreathingPattern(inhale = 4, hold = 2, exhale = 4, holdAfterExhale = 0),
            recommendedCycles = 8,
            colors = listOf(Color(191, 125, 249),Color(160, 140, 230),Color(163, 138, 231))
        ),

        BreathingTechnique(
            id = 5,
            name = "Дыхание по кругу",
            description = "Помогает вернуть фокус и осознанность через ритм.",
            tuter = "Вдохни на 4 счёта, задержи дыхание на 2, выдохни на 4, снова задержи на 2. Повторяй ритм, представляя, как дыхание течёт по замкнутому кругу.",
            pattern = BreathingPattern(inhale = 4, hold = 2, exhale = 4, holdAfterExhale = 2),
            recommendedCycles = 8,
            colors = listOf(Color(127, 187, 114),Color(74, 167, 134),Color(127, 187, 114))
        ),
        BreathingTechnique(
            id = 5,
            name = "Дыхание с сопротивлением",
            description = "Техника для мягкого успокоения и снижения внутреннего напряжения.",
            tuter = "Сделай плавный вдох через нос на 4 счёта. Затем слегка сожми губы — как будто собираешься свистнуть — и выдыхай через них на 6 счётов. Лёгкое сопротивление на выдохе помогает замедлить дыхание и активировать нервную систему расслабления.",
            pattern = BreathingPattern(inhale = 4, hold = 2, exhale = 6, holdAfterExhale = 0),
            recommendedCycles = 8,
            colors = listOf(Color(234, 154, 135),Color(231, 140, 145),Color(202, 128, 179))
        ),
    )
}