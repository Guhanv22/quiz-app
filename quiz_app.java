import java.util.*;

class User {
    String username;
    int score;
    int level;

    User(String username) {
        this.username = username;
        this.score = 0;
        this.level = 1;
    }

    void updateScore(int points) {
        this.score += points;
        if (this.score >= 50) {
            this.level++;
            this.score = 0;
            System.out.println("Congratulations! You leveled up to " + this.level);
        }
    }
}

class Question {
    String questionText;
    String[] options;
    Option correctAnswer;
    String explanation;

    enum Option {
        OPTION_1, OPTION_2, OPTION_3, OPTION_4;
    }

    Question(String questionText, String[] options, Option correctAnswer, String explanation) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }
}

class Quiz {
    List<Question> questions;

    Quiz() {
        this.questions = new ArrayList<>();
    }

    void addQuestion(Question question) {
        questions.add(question);
    }
}

class Admin {
    Quiz quiz;

    Admin(Quiz quiz) {
        this.quiz = quiz;
    }

    void addQuestion(String questionText, String[] options, Question.Option correctAnswer, String explanation) {
        Question question = new Question(questionText, options, correctAnswer, explanation);
        quiz.addQuestion(question);
    }
}

class Leaderboard {
    Map<String, User> users;

    Leaderboard(Map<String, User> users) {
        this.users = users;
    }

    void displayTopUsers(int topN) {
        users.values().stream()
            .sorted((u1, u2) -> Integer.compare(u2.level, u1.level))
            .limit(topN)
            .forEach(user -> System.out.println(user.username + " - Level: " + user.level + ", Score: " + user.score));
    }
}

public class quiz_app {
    static Scanner scanner = new Scanner(System.in);
    static Map<String, User> users = new HashMap<>();
    static Quiz quiz = new Quiz();
    static Admin admin = new Admin(quiz);
    static Leaderboard leaderboard = new Leaderboard(users);

    public static void main(String[] args) {
        initializeQuestions();
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        User user = users.computeIfAbsent(username, User::new);

        System.out.println("Welcome " + username + "! Let's start the quiz.");
        startQuiz(user);

        System.out.println("\nLeaderboard:");
        leaderboard.displayTopUsers(10);
    }

    static void initializeQuestions() {
        admin.addQuestion("What is the capital of France?",
                new String[]{"Berlin", "Madrid", "Paris", "Rome"},
                Question.Option.OPTION_3,
                "Paris is the capital of France.");
        admin.addQuestion("What is 5 + 7?",
                new String[]{"10", "12", "14", "16"},
                Question.Option.OPTION_2,
                "5 + 7 = 12");
        admin.addQuestion("Who wrote 'Hamlet'?",
                new String[]{"Shakespeare", "Dickens", "Austen", "Twain"},
                Question.Option.OPTION_1,
                "William Shakespeare wrote 'Hamlet'.");
    }

    static void startQuiz(User user) {
        int score = 0;
        for (Question q : quiz.questions) {
            System.out.println(q.questionText);
            for (int i = 0; i < q.options.length; i++) {
                System.out.println((i + 1) + ". " + q.options[i]);
            }
            System.out.print("Your answer (1-4): ");
            int answer = scanner.nextInt();
            Question.Option chosenOption = Question.Option.values()[answer - 1];

            if (chosenOption == q.correctAnswer) {
                System.out.println("Correct!");
                score += 10;
            } else {
                System.out.println("Incorrect. " + q.explanation);
            }
        }
        user.updateScore(score);
        System.out.println("Your total score: " + user.score);
    }
}
