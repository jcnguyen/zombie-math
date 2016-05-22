import java.awt.*;
import objectdraw.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.AudioClip;

/**
 * Zombie Math<br>
 * Player answers math problems. 5 points for every correct answer, -1 point for
 * every incorrect answer Player dies if there are 3 incorrect answers. Player
 * wins if player reaches 100 points<br>
 * 
 * Suggested Window Size: 500x500
 * 
 * @author Jennifer Nguyen
 * 
 */
public class ZombieMathController extends WindowController implements
		ActionListener {

	/** Dimensions of start game button */
	private static final int WIDTH = 150, HEIGHT = 50;

	/** The instructions */
	private Text instructions;

	/** Click here text */
	private Text clickHereText;

	/** The score */
	private int score, fail;

	/** Keeps track of the score */
	private JLabel scoreDisplay, failDisplay;

	/** Enter answer here */
	private JTextField answerField;

	/** Start game button */
	private FramedRect startGameButton;

	/** The numbers to add */
	private int numX, numY;

	/** The displayed numbers to add */
	private Text numDisplay;

	/** Randomly generates numbers to add */
	private RandomIntGenerator xInt, yInt;

	/** Human and zombie images */
	private Image humanImage, zombieImage;

	/** The human and zombie */
	private VisibleImage human, zombie;

	public void begin() {
		// load images
		zombieImage = getImage("zombie.gif");
		humanImage = getImage("human.gif");

		// the instructions
		instructions = new Text("Get 100 points", 0, 0, canvas);
		instructions.move((canvas.getWidth() - instructions.getWidth()) / 2, 200);

		// start game button
		startGameButton = new FramedRect((canvas.getWidth() - WIDTH) / 2,
				(canvas.getHeight() - HEIGHT) / 2, WIDTH, HEIGHT, canvas);
		clickHereText = new Text("Click here to begin", 0, 0, canvas);
		clickHereText.move(
				startGameButton.getX()
						+ ((WIDTH - clickHereText.getWidth()) / 2),
				startGameButton.getY()
						+ ((HEIGHT - clickHereText.getHeight()) / 2));
	}

	/**
	 * Invokes when user clicks mouse<br>
	 * When player clicks begin button, game starts
	 * 
	 * @param point - mouse location
	 */
	public void onMouseClick(Location point) {
		if (startGameButton.contains(point)) {
			startGame();
		}
	}

	/**
	 * Required keyboard event listener (for ActionListener interface)<br>
	 * Handles answer guesses
	 * 
	 * @param evt
	 *            - key press event
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == answerField) {

			// checks that the guess is an integer
			if (isInteger(answerField.getText())) {

				// answer is correct
				if (Integer.parseInt(answerField.getText()) == numX + numY) {
					// gets new values to add
					numX = xInt.nextValue();
					numY = yInt.nextValue();
					numDisplay.setText(numX + " + " + numY);

					// sets the answer field text to blank
					answerField.setText("");

					// updates the score
					score += 50;
					scoreDisplay.setText("Score = " + score);
				} else {// answer is not correct
					// scorns the player
					answerField
							.setText("You're about to die and you can't even do simple math. Again.");

					// updates the score
					fail += 1;
					failDisplay.setText("Fail = " + fail);
				}
			} else {// guess is not an integer
				answerField.setText("Numbers only, fool.");
			}

			// you win
			if (score >= 100) {
				human.show();
				Text winText = new Text(
						"CONGRATULATIONS! YOU GET TO SEE ANOTHER DAY AS A HUMAN",
						0, 0, canvas);
				winText.move((canvas.getWidth() - winText.getWidth()) / 2, 0);
				numDisplay.hide();
			}
			
			// you lose
			if (fail == 3) {
				zombie.show();
				Text loseText = new Text("Died", 0, 0, canvas);
				loseText.move((canvas.getWidth() - loseText.getWidth()) / 2, 0);
				numDisplay.hide();
			}
		}
	}

	/**
	 * Creates the interface of the game when game starts
	 */
	private void startGame() {
		// create and hide images
		human = new VisibleImage(humanImage, 0, 0, canvas);
		human.move((canvas.getWidth() - human.getWidth()) / 2,
				(canvas.getHeight() - human.getWidth()) / 2);
		human.hide();

		zombie = new VisibleImage(zombieImage, 0, 0, canvas);
		zombie.move((canvas.getWidth() - zombie.getWidth()) / 2,
				(canvas.getHeight() - zombie.getWidth()) / 2);
		zombie.hide();

		// hide instructions
		startGameButton.removeFromCanvas();
		clickHereText.removeFromCanvas();
		instructions.removeFromCanvas();

		// current score and fail
		score = 0;
		fail = 0;

		// GUI components
		scoreDisplay = new JLabel("Score = " + score);
		failDisplay = new JLabel("Fail = " + fail);
		answerField = new JTextField("Enter answer");
		answerField.addActionListener(this);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1));
		panel.add(answerField);
		panel.add(scoreDisplay);
		panel.add(failDisplay);

		Container contentPane = getContentPane();
		contentPane.add(panel, BorderLayout.SOUTH);
		contentPane.validate();

		// generating the numbers to add and displaying them
		xInt = new RandomIntGenerator(0, 100);
		yInt = new RandomIntGenerator(0, 100);

		numX = xInt.nextValue();
		numY = yInt.nextValue();

		numDisplay = new Text(numX + " + " + numY, 0, 0, canvas);
		numDisplay.move((canvas.getWidth() - numDisplay.getWidth()) / 2,
				(canvas.getHeight() - numDisplay.getHeight()) / 2.5);
	}

	/**
	 * Checks if the guess is an integer
	 * 
	 * @param aString - the guess
	 * @return true if guess is an integer
	 */
	private boolean isInteger(String aString) {
		for (int i = 0; (i < aString.length()); i++) {
			if (aString.charAt(i) < '0' || aString.charAt(i) > '9')
				return false;
		}
		return true;
	}
}