import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class RacingGame extends JPanel implements ActionListener, MouseMotionListener {
    private Timer timer, countdownTimer, StopStartcountdownTimer, shieldTimer;
    private int shieldRemainingTime, opponentSpeed, score, countdown = 3, StopStartcountdown = 3;
    private boolean isGameOver, isShieldActive, isPaused = false, isCountdownRunning = false;
    private Car playerCar;
    private List<Car> opponentCars, powerUps;
    private Image carImage, backgroundImage, powerUpImage;
    private List<Image> obstacleImages;
    private JLabel scoreLabel, countdownLabel, StopcountdownLabel, shieldLabel, pauseLabel;
    private JButton startButton, pauseButton;
    private final int[] OBSTACLE_X_POSITIONS = { 100, 180, 260, 340 };

    public RacingGame() {
        setupGamePanel();
        loadImages();
        initializeGameObjects();
        setupUIComponents();
        setupEventListeners();
    }

    private void setupGamePanel() {
        setPreferredSize(new Dimension(500, 800));
        setBackground(Color.WHITE);
        setLayout(null);
    }

    private void loadImages() {
        powerUpImage = new ImageIcon("src/pic/PowerUp.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        backgroundImage = new ImageIcon("src/pic/2DBackground.png").getImage();
        obstacleImages = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            obstacleImages.add(new ImageIcon("src/pic/obstacle" + i + ".png").getImage());
        }
    }

    private void initializeGameObjects() {
        playerCar = new Car(225, 700, 60, 115, carImage);
        opponentCars = new ArrayList<>();
        powerUps = new ArrayList<>();
        opponentSpeed = 2;
        score = 0;
        isGameOver = false;
        timer = new Timer(10, this);
    }

    private void setupUIComponents() {
        scoreLabel = createLabel("Score: 0", 150, 40, 200, 30, 30, Color.RED);
        countdownLabel = createLabel("", 150, 300, 200, 100, 100, Color.WHITE);
        StopcountdownLabel = createLabel("", 150, 300, 200, 100, 100, Color.ORANGE);
        shieldLabel = createLabel("Shield Active!", 110, 100, 300, 50, 30, Color.ORANGE);
        shieldLabel.setVisible(false);
        pauseLabel = createLabel("Paused", 150, 350, 200, 100, 50, Color.RED);
        pauseLabel.setVisible(false);
        startButton = createButton("Start Game", 100, 375, 300, 80, 50, e -> startCountdown());
        pauseButton = createButton("Pause", 400, 20, 80, 40, 20, e -> togglePause());
        add(scoreLabel);
        add(countdownLabel);
        add(StopcountdownLabel);
        add(shieldLabel);
        add(pauseLabel);
        add(startButton);
        add(pauseButton);
    }

    private JLabel createLabel(String text, int x, int y, int width, int height, int fontSize, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        label.setBounds(x, y, width, height);
        label.setForeground(color);
        label.setVisible(true);
        return label;
    }

    private JButton createButton(String text, int x, int y, int width, int height, int fontSize,
            ActionListener action) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.addActionListener(action);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        if (fontSize > 0) {
            button.setFont(new Font("Arial", Font.TYPE1_FONT, fontSize));
        }
        return button;
    }

    private void setupEventListeners() {
        playerCar.setMoveDistance(80);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        addMouseMotionListener(this);
        setFocusable(true);
    }

    private void handleKeyPress(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            playerCar.KeymoveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            playerCar.KeymoveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            togglePause();
        }
        repaint();
    }

    public void setPlayerCarImage(String carImagePath) {
        Image carImage = new ImageIcon(carImagePath).getImage().getScaledInstance(60, 115, Image.SCALE_SMOOTH);
        playerCar.setImage(carImage);
    }

    private void startCountdown() {
        countdown = 3;
        countdownLabel.setText(String.valueOf(countdown));
        countdownLabel.setVisible(true);
        startButton.setVisible(false);
        countdownTimer = new Timer(1000, e -> updateCountdown());
        countdownTimer.start();
    }

    private void updateCountdown() {
        countdown--;
        if (countdown > 0) {
            countdownLabel.setText(String.valueOf(countdown));
        } else {
            countdownTimer.stop();
            countdownLabel.setVisible(false);
            startGame();
        }
    }

    private void startGame() {
        resetGame();
        for (int i = 0; i < 3; i++) {
            addNewObstacle();
        }
        timer.start();
    }

    private void resetGame() {
        isGameOver = false;
        isShieldActive = false;
        score = 0;
        opponentSpeed = 2;
        opponentCars.clear();
        powerUps.clear();
        shieldLabel.setVisible(false);
    }

    private void addNewObstacle() {
        int randomX, randomY;
        boolean overlap;
        Image obstacleImage;
        do {
            randomX = OBSTACLE_X_POSITIONS[(int) (Math.random() * OBSTACLE_X_POSITIONS.length)];
            randomY = -500 - (int) (Math.random() * 1600);
            overlap = false;
            obstacleImage = obstacleImages.get((int) (Math.random() * obstacleImages.size()));
            Rectangle newObstacleBounds = new Rectangle(randomX, randomY, 50, 115);
            for (Car car : opponentCars) {
                if (newObstacleBounds
                        .intersects(new Rectangle(car.getX(), car.getY(), car.getWidth(), car.getHeight()))) {
                    overlap = true;
                    break;
                }
            }
        } while (overlap);
        Car newObstacle = new Car(randomX, randomY, 50, 115, obstacleImage);

        // 根據圖片設置不同的高度
        if (obstacleImage == obstacleImages.get(2)) {
            newObstacle.setHeight(200); // 設置較長的高度
        }
        if (obstacleImage == obstacleImages.get(4)) {
            newObstacle.setHeight(170); // 設置較短的高度
        }
        opponentCars.add(newObstacle);
    }

    private void addNewPowerUp() {
        int randomX, randomY;
        boolean overlap;
        do {
            randomX = OBSTACLE_X_POSITIONS[(int) (Math.random() * OBSTACLE_X_POSITIONS.length)];
            randomY = -500 - (int) (Math.random() * 1600);
            overlap = false;
            Rectangle newPowerUpBounds = new Rectangle(randomX, randomY, 50, 115);
            for (Car car : opponentCars) {
                if (newPowerUpBounds
                        .intersects(new Rectangle(car.getX(), car.getY(), car.getWidth(), car.getHeight()))) {
                    overlap = true;
                    break;
                }
            }
            for (Car obstacle : opponentCars) {
                if (newPowerUpBounds
                        .intersects(new Rectangle(obstacle.getX(), obstacle.getY(), obstacle.getWidth(),
                                obstacle.getHeight()))) {
                    overlap = true;
                    break;
                }
            }
        } while (overlap);
        powerUps.add(new Car(randomX, randomY, 50, 150, powerUpImage));
    }

    private void activateShield() {
        isShieldActive = true;
        shieldLabel.setVisible(true);
        shieldRemainingTime += 3;
        if (shieldTimer != null) {
            shieldTimer.stop();
        }
        shieldTimer = new Timer(1000, e -> updateShieldTimer());
        shieldLabel.setText("Shield Active! " + shieldRemainingTime + " s");
        shieldTimer.start();
    }

    private void updateShieldTimer() {
        shieldRemainingTime--;
        if (shieldRemainingTime > 0) {
            shieldLabel.setText("Shield Active! " + shieldRemainingTime + " s");
        } else {
            isShieldActive = false;
            shieldLabel.setVisible(false);
            shieldTimer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            updateOpponentCars();
            updatePowerUps();
            spawnNewPowerUps();
            updateScore();
            repaint();
        }
    }

    private void updateOpponentCars() {
        for (Car car : opponentCars) {
            car.moveDown(opponentSpeed);
            if (car.getY() > 800) {
                resetCarPosition(car);
            }
            if (!isShieldActive && car.intersects(playerCar)) {
                endGame();
            }
        }
    }

    private void resetCarPosition(Car car) {
        car.setY(-100);
        int randomIndex;
        boolean overlap;
        Image obstacleImage;
        do {
            randomIndex = (int) (Math.random() * OBSTACLE_X_POSITIONS.length);
            overlap = false;
            obstacleImage = obstacleImages.get((int) (Math.random() * obstacleImages.size()));
            Rectangle newObstacleBounds = new Rectangle(OBSTACLE_X_POSITIONS[randomIndex], car.getY(), car.getWidth(),
                    car.getHeight());
            for (Car otherCar : opponentCars) {
                if (otherCar != car && newObstacleBounds.intersects(
                        new Rectangle(otherCar.getX(), otherCar.getY(), otherCar.getWidth(), otherCar.getHeight()))) {
                    overlap = true;
                    break;
                }
            }
        } while (overlap);
        car.setX(OBSTACLE_X_POSITIONS[randomIndex]);
        car.setImage(obstacleImage);
    }

    private void endGame() {
        isGameOver = true;
        timer.stop();
        showGameOverDialog();
    }

    private void updatePowerUps() {
        for (Car powerUp : powerUps) {
            powerUp.moveDown(opponentSpeed);
            if (powerUp.getY() > 800) {
                powerUps.remove(powerUp);
                break;
            }
            if (powerUp.intersects(playerCar)) {
                activateShield();
                powerUps.remove(powerUp);
                break;
            }
        }
    }

    private void spawnNewPowerUps() {
        double baseSpawnRate = 0.0005;
        double speedFactor = 0.0001;
        double powerUpSpawnRate = baseSpawnRate + (opponentSpeed * speedFactor);
        if (Math.random() < powerUpSpawnRate) {
            addNewPowerUp();
        }
    }

    private void updateScore() {
        if (!isGameOver) {
            score++;
            scoreLabel.setText("Score: " + score);
            if (score % 600 == 0) {
                opponentSpeed++;
            }
        }
    }

    private void togglePause() {
        if (isCountdownRunning)
            return;
        if (!isPaused) {
            pauseGame();
        } else {
            resumeGame();
        }
        isPaused = !isPaused;
    }

    private void pauseGame() {
        stopTimers();
        pauseLabel.setVisible(true);
    }

    private void stopTimers() {
        if (timer != null && timer.isRunning())
            timer.stop();
        if (shieldTimer != null && shieldTimer.isRunning())
            shieldTimer.stop();
        if (StopStartcountdownTimer != null && StopStartcountdownTimer.isRunning())
            StopStartcountdownTimer.stop();
    }

    private void resumeGame() {
        pauseLabel.setVisible(false);
        startCountdownBeforeResume();
    }

    private void startCountdownBeforeResume() {
        if (isCountdownRunning)
            return;
        isCountdownRunning = true;
        StopStartcountdown = 3;
        StopcountdownLabel.setText(String.valueOf(StopStartcountdown));
        StopcountdownLabel.setVisible(true);
        pauseButton.setVisible(false);
        StopStartcountdownTimer = new Timer(1000, e -> updateResumeCountdown());
        StopStartcountdownTimer.start();
    }

    private void updateResumeCountdown() {
        StopStartcountdown--;
        if (StopStartcountdown > 0) {
            StopcountdownLabel.setText(String.valueOf(StopStartcountdown));
        } else {
            StopStartcountdownTimer.stop();
            isCountdownRunning = false;
            StopcountdownLabel.setVisible(false);
            timer.start();
            if (shieldTimer != null)
                shieldTimer.start();
            pauseButton.setText("Pause");
            pauseButton.setVisible(true);
        }
    }

    private void showGameOverDialog() {
        JDialog gameOverDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Game Over", true);
        gameOverDialog.setLayout(new BorderLayout());
        gameOverDialog.setSize(300, 400);
        gameOverDialog.setLocationRelativeTo(this);
        gameOverDialog.add(createGameOverImageLabel(), BorderLayout.NORTH);
        gameOverDialog.add(createGameOverLabel(), BorderLayout.CENTER);
        gameOverDialog.add(createGameOverButtonPanel(), BorderLayout.SOUTH);
        gameOverDialog.setVisible(true);
    }

    private JLabel createGameOverImageLabel() {
        ImageIcon gameOverImageIcon = new ImageIcon("src/pic/Crash.png");
        gameOverImageIcon = new ImageIcon(gameOverImageIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH));
        return new JLabel(gameOverImageIcon);
    }

    private JLabel createGameOverLabel() {
        JLabel gameOverLabel = new JLabel("<html> GAME OVER ! <br> Your Score is: " + score, SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gameOverLabel.setForeground(Color.DARK_GRAY);
        return gameOverLabel;
    }

    private JPanel createGameOverButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(createRestartButton());
        buttonPanel.add(createQuitButton());
        return buttonPanel;
    }

    private JButton createRestartButton() {
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {
            restartGame();
            Window window = SwingUtilities.getWindowAncestor(restartButton);
            if (window != null) {
                window.dispose();
            }
        });
        return restartButton;
    }

    private JButton createQuitButton() {
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> {
            System.exit(0);
        });
        return quitButton;
    }

    private void restartGame() {
        startCountdown();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        playerCar.draw(g);
        for (Car car : opponentCars) {
            car.draw(g);
        }
        for (Car powerUp : powerUps) {
            powerUp.draw(g);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int newX = e.getX() - playerCar.getWidth() / 2;
        if (newX < 100) {
            newX = 100;
        } else if (newX > 400 - playerCar.getWidth()) {
            newX = 400 - playerCar.getWidth();
        }
        playerCar.setX(newX);
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    public void showStartButton() {
        startButton.setVisible(true);
    }
}
