import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class QuizApp extends JFrame {
    private ArrayList<Question> questions;
    private int currentQuestionIndex;
    private int score;
    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup buttonGroup;
    private JButton submitButton;
    private JLabel resultLabel;
    private Timer questionTimer;
    private int timeLeft;

    public QuizApp(ArrayList<Question> questions) {
        this.questions = questions;
        this.currentQuestionIndex = 0;
        this.score = 0;

        setTitle("Quiz App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        initComponents();
        loadQuestion();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        questionLabel = new JLabel();
        panel.add(questionLabel);

        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            buttonGroup.add(optionButtons[i]);
            panel.add(optionButtons[i]);
        }

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitAnswer();
            }
        });
        panel.add(submitButton);

        resultLabel = new JLabel();
        panel.add(resultLabel);

        add(panel);
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionLabel.setText(currentQuestion.getQuestion());
            String[] options = currentQuestion.getOptions();
            for (int i = 0; i < options.length; i++) {
                optionButtons[i].setText(options[i]);
            }
            startTimer();
        } else {
            showResult();
        }
    }

    private void startTimer() {
        timeLeft = 10; // 10 seconds per question
        questionTimer = new Timer();
        questionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timeLeft > 0) {
                    setTitle("Time Left: " + timeLeft + " seconds");
                    timeLeft--;
                } else {
                    questionTimer.cancel();
                    submitAnswer();
                }
            }
        }, 0, 1000); // Update every second
    }

    private void submitAnswer() {
        questionTimer.cancel(); // Cancel the timer when submitting an answer
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                if (questions.get(currentQuestionIndex).isCorrect(optionButtons[i].getText())) {
                    score++;
                }
                currentQuestionIndex++;
                loadQuestion();
                break;
            }
        }
    }

    private void showResult() {
        setTitle("Quiz App");
        questionLabel.setVisible(false);
        for (JRadioButton button : optionButtons) {
            button.setVisible(false);
        }
        submitButton.setVisible(false);
        resultLabel.setText("Final Score: " + score + "/" + questions.size());
    }

    public static void main(String[] args) {
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question("What is the capital of France?", new String[]{"London", "Paris", "Berlin", "Madrid"}, "Paris"));
        questions.add(new Question("Which planet is known as the Red Planet?", new String[]{"Mars", "Jupiter", "Venus", "Mercury"}, "Mars"));
        questions.add(new Question("Who wrote 'Romeo and Juliet'?", new String[]{"William Shakespeare", "Jane Austen", "Charles Dickens", "Mark Twain"}, "William Shakespeare"));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuizApp(questions).setVisible(true);
            }
        });
    }
}
