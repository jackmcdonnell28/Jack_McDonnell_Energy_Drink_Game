//Basic Game Application
// Basic Object, Image, Movement
// Threaded

//*******************************************************************************
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.event.KeyListener;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

//*******************************************************************************

public class BasicGameApp implements Runnable, KeyListener{

    //Variable Definition Section
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;
    public boolean pressingKey;
    public SoundFile canOpening;
    public boolean gameOver = false;
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    public boolean collision;
    public SoundFile death;

    public BufferStrategy bufferStrategy;
    Whitemonster whiteMonster;
    Image whiteMonsterImage;
    Rosamonster RosaMonster2;
    Image RosaMonsterImage2;
    Ghost[] ghostWaterfall;
    Image ghostImage;

    // *** BACKGROUND IMAGE (ADDED) ***
    Image backgroundImage;

    public Boolean firstCrash;

    // Main method definition
    public static void main(String[] args) {
        BasicGameApp ex = new BasicGameApp();
        new Thread(ex).start();
    }

    // Constructor
    public BasicGameApp() {

        setUpGraphics();
        firstCrash = true;
        collision = true;

        whiteMonster = new Whitemonster(
                "Whitemonster.png",
                (int)(Math.random() * WIDTH -25),
                (int)(Math.random() * HEIGHT -75),
                75
        );
// it wouldn't work regularly but when i put each thing on a new line it worked somehow
        RosaMonster2 = new Rosamonster(
                "Rosa monster.png",
                (int)(Math.random() * WIDTH),
                (int)(Math.random() * HEIGHT),
                25
        );

        ghostWaterfall = new Ghost[6];
        whiteMonsterImage = new ImageIcon("Whitemonster.png").getImage();
        RosaMonsterImage2 = new ImageIcon("Rosa Monster.png").getImage();
        ghostImage = new ImageIcon("Murica Ghost.png").getImage();
        backgroundImage = new ImageIcon("7-11.jpeg").getImage();

        ghostImage = Toolkit.getDefaultToolkit().getImage("Murica Ghost.png");

        //  LOAD BACKGROUND IMAGE (THIS TOOK SO LONG!!!!)
        backgroundImage = Toolkit.getDefaultToolkit().getImage("7-11.jpeg");
        while(collision == true) {
            collision = false;
            for (int x = 0; x < ghostWaterfall.length; x++) {
                ghostWaterfall[x] = new Ghost(
                        "ghostDrink " + x,
                        (int) (Math.random() * WIDTH),
                        (int) (Math.random() * HEIGHT),
                        25
                );
                for (int y = 0; y < x; y ++){
                    if(ghostWaterfall[y].rect.intersects(ghostWaterfall[x].rect)){
                        System.out.println("hi");
                        collision = true;
                    }
                }
            }
            System.out.println(collision);
        }
        canOpening = new SoundFile("canopening.wav");
        death = new SoundFile()
    }



    public void run() {
        while (true) {

            if(!gameOver){
                moveThings();
            }

            render();
            pause(35);
        }
    }

    public void moveThings() {
        whiteMonster.wrap();
        RosaMonster2.wrap();
        for (int x = 0; x < ghostWaterfall.length; x++) {
            ghostWaterfall[x].bounce();  // FIX

        }
        checkCrash();
        checkCrashes();
        for(int x = 0; x < ghostWaterfall.length; x++){
            ghostWaterfall[x].move();
        }
        whiteMonster.dx = 0;
        whiteMonster.dy = 0;


        if(up) whiteMonster.dy = -10;
        if(down) whiteMonster.dy = 10;
        if(left) whiteMonster.dx = -10;
        if(right) whiteMonster.dx = 10;

        whiteMonster.rect = new Rectangle(
                whiteMonster.xpos,
                whiteMonster.ypos,
                whiteMonster.width,
                whiteMonster.height
        );

        RosaMonster2.rect = new Rectangle(
                RosaMonster2.xpos,
                RosaMonster2.ypos,
                RosaMonster2.width,
                RosaMonster2.height
        );
/*
        for(int i = 0; i < ghostWaterfall.length; i++){
            ghostWaterfall[i].rect = new Rectangle(
                    ghostWaterfall[i].xpos,
                    ghostWaterfall[i].ypos,
                    ghostWaterfall[i].width,
                    ghostWaterfall[i].height
            );
        }

 */
    }

    public void checkCrash() {

        if (whiteMonster.rect.intersects(RosaMonster2.rect) && firstCrash) {

            // reverse both directions
            whiteMonster.dx = -whiteMonster.dx;
            whiteMonster.dy = -whiteMonster.dy;

            RosaMonster2.dx = -RosaMonster2.dx;
            RosaMonster2.dy = -RosaMonster2.dy;

            canOpening.play();

            firstCrash = false; // makes it so they stop flipping the key inputs when they touch
        }

        // this conditional over here makes it so they can't take up the same space after bouncing once
        if (!whiteMonster.rect.intersects(RosaMonster2.rect)) {
            firstCrash = true;
        }
    }

    public void checkCrashes(){
        for(int x = 0; x < ghostWaterfall.length; x++){
            if (ghostWaterfall[x].rect.intersects(RosaMonster2.rect)){
                gameOver = true;

            }
        }
    }

    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        g.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, null);


        g.drawImage(whiteMonsterImage, whiteMonster.xpos, whiteMonster.ypos,
                whiteMonster.width, whiteMonster.height, null);


        g.drawImage(RosaMonsterImage2, RosaMonster2.xpos, RosaMonster2.ypos,
                RosaMonster2.width, RosaMonster2.height, null);

        for(int i = 0; i < ghostWaterfall.length; i++) {
            g.fillRect(ghostWaterfall[i].rect.x,
                    ghostWaterfall[i].rect.y,
                    ghostWaterfall[i].rect.width,
                    ghostWaterfall[i].rect.height);

        }

        for(int x = 0; x < ghostWaterfall.length; x++){
            g.drawImage(ghostImage,
                    ghostWaterfall[x].xpos,
                    ghostWaterfall[x].ypos,
                    ghostWaterfall[x].width,
                    ghostWaterfall[x].height,
                    null);
        }

        g.dispose();
        bufferStrategy.show();



        if(gameOver){
            g.setFont(new Font("Arial", Font.BOLD, 120));
            g.setBackground(Color.RED);
            g.drawString("GAME OVER", 150, 350 );
        }
    }

    public void pause(int time ) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {}
    }
    @Override
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode() == 38) up = true;     // up arrow
        if(e.getKeyCode() == 40) down = true;   // down arrow
        if(e.getKeyCode() == 37) left = true;   // left arrow
        if(e.getKeyCode() == 39) right = true;  // right arrow

        // RosaMonster movement
        if (e.getKeyCode() == 87) { // W
            RosaMonster2.dy = -10;
            RosaMonster2.dx = 0;
        }
        if (e.getKeyCode() == 83) { // S
            RosaMonster2.dy = 10;
            RosaMonster2.dx = 0;
        }
        if (e.getKeyCode() == 65) { // A
            RosaMonster2.dy = 0;
            RosaMonster2.dx = -10;
        }
        if (e.getKeyCode() == 68) { // D
            RosaMonster2.dy = 0;
            RosaMonster2.dx = 10;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        // required but unused
    }

    @Override
    public void keyReleased (KeyEvent e){
        System.out.println();
        pressingKey = true;
        if (e.getKeyCode() == 38) { //38 is up arrow
            whiteMonster.dy = 0;
            whiteMonster.dx = 0;
        }
        if (e.getKeyCode() == 40) { //40 is down arrow
            whiteMonster.dy = 0;
            whiteMonster.dx = 0;
        }
        if (e.getKeyCode() == 37) { //37 is left arrow
            whiteMonster.dy = 0;
            whiteMonster.dx = 0;
        }
        if (e.getKeyCode() == 39) { //39 is right arrow
            whiteMonster.dy = 0;
            whiteMonster.dx = 0;
            if(e.getKeyCode() == 38) up = false;
            if(e.getKeyCode() == 40) down = false;
            if(e.getKeyCode() == 37) left = false;
            if(e.getKeyCode() == 39) right = false;
        }

    }
    private void setUpGraphics() {
        frame = new JFrame("White Monster Bounce");

        panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        canvas.addKeyListener(this);
        System.out.println("DONE graphic setup");


    }
}