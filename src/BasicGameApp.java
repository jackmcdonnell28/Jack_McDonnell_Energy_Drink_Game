//Basic Game Application
// Basic Object, Image, Movement
// Threaded

//*******************************************************************************
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

//*******************************************************************************

public class BasicGameApp implements Runnable {

    //Variable Definition Section
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    public BufferStrategy bufferStrategy;

    Whitemonster whiteMonster;
    Image whiteMonsterImage;
    Rosamonster RosaMonster2;
    Image RosaMonsterImage2;
    Ghost[] ghostWaterfall;
    Image ghostImage;


    public Boolean firstCrash;

    // Main method definition
    public static void main(String[] args) {
        BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
        new Thread(ex).start();                 //creates a thread & starts up the code in the run( ) method
    }

    // Constructor
    public BasicGameApp() {

        setUpGraphics();
        firstCrash = true;
        whiteMonster = new Whitemonster("Whitemonster.jpeg", 300, 300, 75);
        whiteMonsterImage = Toolkit.getDefaultToolkit().getImage("Whitemonster.jpeg");

        RosaMonster2 = new Rosamonster("Rosa monster.jpeg", 500, 200, 25);
        RosaMonsterImage2 = Toolkit.getDefaultToolkit().getImage("Rosa Monster.jpeg");

        ghostWaterfall = new Ghost[6];
        ghostImage = Toolkit.getDefaultToolkit().getImage("Murica Ghost.jpeg");
        for(int x = 0; x < ghostWaterfall.length; x = x+1){
            ghostWaterfall[x] = new Ghost("ghostDrink " + x, (int)(Math.random()* WIDTH), (int)(Math.random()* HEIGHT), 25);
        }
        run();


    } // end BasicGameApp constructor

//*******************************************************************************
//User Method Section

    // main thread
    public void run() {
        while (true) {
            moveThings();// move all the game objects
            if (RosaMonster2.isAlive ==  false){
                RosaMonster2.width = RosaMonster2.width + 10;
                RosaMonster2.height = RosaMonster2.height + 10;
            }

            render();      // paint the graphics
            pause(15);     // sleep for 15 ms
        }
    }

    public void moveThings() {
        whiteMonster.wrap();
        RosaMonster2.bounce();
        checkCrash();
        for(int x = 0; x < ghostWaterfall.length; x = x+1){
           ghostWaterfall[x].move();
        }


    }
    public void checkCrash(){
        if(whiteMonster.rect.intersects(RosaMonster2.rect)&& firstCrash == true){
           whiteMonster.dx=-whiteMonster.dx;
           RosaMonster2.dx=-RosaMonster2.dx;
            whiteMonster.dy=-whiteMonster.dy;
            RosaMonster2.dy=-RosaMonster2.dy;
            RosaMonster2.height += 1000;
            RosaMonster2.width += 1000;
           double rand1= Math.random();
           double rand2= Math.random();
            RosaMonster2.dx=0;
            RosaMonster2.dy=0;

           firstCrash = false;
           RosaMonster2.isAlive = false;
           //this altered checkCrash method causes the pink monster can to get bigger and bigger until
            //eventually it just gives up on life and disappears

           }

        if (!whiteMonster.rect.intersects(RosaMonster2.rect)) {
            firstCrash = true;
        }
    }
    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setColor(new Color(100, 200, 200));
        g.fillRect(0, 0, WIDTH, HEIGHT);


        // draw the image
        g.drawImage(whiteMonsterImage, whiteMonster.xpos, whiteMonster.ypos, whiteMonster.width, whiteMonster.height, null);
        if(RosaMonster2.width < 1000){g.drawImage(RosaMonsterImage2, RosaMonster2.xpos, RosaMonster2.ypos, RosaMonster2.width, RosaMonster2.height, null);}


        g.dispose();
        bufferStrategy.show();
        for(int x = 0; x < ghostWaterfall.length; x = x+1){
            g.drawImage(ghostImage, ghostWaterfall[x].xpos,ghostWaterfall[x].ypos, ghostWaterfall[x].width, ghostWaterfall[x].height, null);
        }
    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time ) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {}
    }

    //Graphics setup method
    private void setUpGraphics() {
        frame = new JFrame("White Monster Bounce");   //Create the program window or frame.

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
        System.out.println("DONE graphic setup");
    }

}