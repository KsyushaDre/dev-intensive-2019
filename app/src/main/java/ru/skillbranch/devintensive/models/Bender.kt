package ru.skillbranch.devintensive.models

class Bender(
    var status: Status = Status.NORMAL,
    var question: Question = Question.NAME,
    var count: Int = 0
) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        if (validationAnswer(answer, question)) {
            return if (question.answers.contains(answer)) {
                question = question.nextQuestion()
                "Отлично - ты справился\n${question.question}" to status.color
            } else {
                count++
                if (count <= 3) {
                    status = status.nextStatus()
                    "Это неправильный ответ\n${question.question}" to status.color
                } else {
                    status = Status.NORMAL
                    question = Question.NAME
                    count = 0
                    "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                }
            }
        } else {
            return when (question) {
                Question.NAME -> "Имя должно начинаться с заглавной буквы\n${question.question}" to status.color
                Question.PROFESSION -> "Профессия должна начинаться со строчной буквы\n${question.question}" to status.color
                Question.MATERIAL -> "Материал не должен содержать цифр\n${question.question}" to status.color
                Question.BDAY -> "Год моего рождения должен содержать только цифры\n${question.question}" to status.color
                Question.SERIAL -> "Серийный номер содержит только цифры, и их 7\n${question.question}" to status.color
                Question.IDLE -> question.question to status.color
            }
        }
    }

    private fun validationAnswer(answer: String, question: Question): Boolean {
        val regex = Regex("\\d")
        val regex2 = Regex("[a-zA-zа-яА-яёЁ]")
        val regex3 = Regex("\\s")
        when (question) {
            Question.NAME -> {
                return !answer[0].isLowerCase()
            }
            Question.PROFESSION -> {
                return !answer[0].isUpperCase()
            }
            Question.MATERIAL -> {
                return !regex.containsMatchIn(answer)
            }
            Question.BDAY -> {
                return !(regex2.containsMatchIn(answer) || regex3.containsMatchIn(answer))
            }
            Question.SERIAL -> {
                return !((regex2.containsMatchIn(answer) || regex3.containsMatchIn(answer)) || (answer.length != 7))
            }
            Question.IDLE -> return false
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("Бендер", "Bender")) {
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question
    }
}