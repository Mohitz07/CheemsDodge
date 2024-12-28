import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; //use to store the matrix column
import java.util.Random;    //to appear column at random 
import javax.swing.*;
public class CheemsDodge extends JPanel implements ActionListener, KeyListener{    //defining new class with all the functionality of jPanel (inheritance)
    int boardWidth = 360;
    int boardHeight = 640;

    //Image
    Image backgroundImg;
    Image cheemsImg;
    Image topMatrixImg;
    Image bottomMatrixImg;

    //Bird
    int cheemsX = boardWidth/8;
    int cheemsY = boardHeight/2;
    int cheemsWidth = 52;
    int cheemsHeight = 42;

    class Cheems {
        int x = cheemsX;
        int y = cheemsY;
        int width = cheemsWidth;
        int height = cheemsHeight;
        Image img;

        Cheems(Image img) {
            this.img = img;
        }
    }

    //matrix
    int matrixX = boardWidth;
    int matrixY = 0; 
    int matrixWidth = 64;
    int matrixHeight = 512;

    class Matrix {
        int x = matrixX;
        int y = matrixY;
        int width = matrixWidth;
        int height = matrixHeight;
        Image img;
        boolean passed = false;

        Matrix(Image img) {
            this.img = img;
        }
    }


    //game logic
    Cheems cheems;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1; //every frame the cheems is going to slow down 1 pixel

    ArrayList<Matrix> matrixes;
    Random random = new Random();


    Timer gameLoop;
    Timer placeMatrixesTimer;
    boolean gameOver = false;
    double score = 0;


    CheemsDodge() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        //setBackground(Color.blue);

        setFocusable(true);
        addKeyListener(this);

        //load image
        backgroundImg = new ImageIcon(getClass().getResource("./bg.png")).getImage();
        cheemsImg = new ImageIcon(getClass().getResource("./cheems.png")).getImage();
        topMatrixImg = new ImageIcon(getClass().getResource("./topMatrix.png")).getImage();
        bottomMatrixImg = new ImageIcon(getClass().getResource("./bottomMatrix.png")).getImage();

        //cheems
        cheems = new Cheems(cheemsImg);
        matrixes = new ArrayList<Matrix>();

        //place matrixes timer
        placeMatrixesTimer = new Timer(1700, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeMatrixes();
            }
        });
        placeMatrixesTimer.start();

        //game timer
        gameLoop = new Timer(1000/60, this); //1000/60 = 16.6
        gameLoop.start();   //to continuously draw frames for our game
    }

    public void placeMatrixes() {
        int randomMatrixY = (int) (matrixY - matrixHeight/4 - Math.random()*(matrixHeight/2));
        int openingSpace = boardHeight/4;

        Matrix topMatrix = new Matrix(topMatrixImg);
        topMatrix.y = randomMatrixY;
        matrixes.add(topMatrix);
        
        Matrix bottomMatrix = new Matrix(bottomMatrixImg);
        bottomMatrix.y = topMatrix.y + matrixHeight + openingSpace;
        matrixes.add(bottomMatrix); 
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);    //super refers to j class which is the pannel
        draw(g);
    }

    public void draw(Graphics g) {
        //background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        //cheems
        g.drawImage(cheems.img, cheems.x, cheems.y, cheems.width, cheems.height, null);

        //pipes
        for(int i = 0; i < matrixes.size(); i++) {
            Matrix matrix = matrixes.get(i);
            g.drawImage(matrix.img, matrix.x, matrix.y, matrix.width, matrix.height, null);

        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver) {
            g.drawString("Game Over: "+ String.valueOf((int) score), 10, 35);
        }
        else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move() {
        //cheems
        velocityY += gravity;
        cheems.y += velocityY;
        cheems.y = Math.max(cheems.y, 0);   //bird ke position se leke ceiling tak

        //matrix
        for(int i = 0; i < matrixes.size(); i++) {
            Matrix matrix = matrixes.get(i);
            matrix.x += velocityX;

            if(!matrix.passed && cheems.x > matrix.x + matrix.width) {
                matrix.passed = true;
                score += 0.5;   //0.5 because for each matrix we gdo 0.5 + 0.5 which is equal to 1
            }

            if(collision(cheems, matrix)){
                gameOver = true;
            }
        }

        if(cheems.y > boardHeight) {
            gameOver = true;
        }
    }

    public boolean collision(Cheems a, Matrix b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {    //action performed runs 60 times per second
        move(); 
        repaint();
        if(gameOver) {
            placeMatrixesTimer.stop();
            gameLoop.stop();
        }
    }

    
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -10;
            if(gameOver) {
                //restart the game by resetting the conditions
                cheems.y = cheemsY;
                velocityY = 0;
                matrixes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placeMatrixesTimer.start();
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
