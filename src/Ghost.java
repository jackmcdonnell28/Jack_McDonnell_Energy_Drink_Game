import java.awt.*;

public class Ghost {
        public int xpos;
        public int ypos;
        public int width = 120;
        public int height = 120;
        public int dx = 5;
        public int dy = 4;
        public String pic;
        public Rectangle rect;
        public Boolean isAlive;
        public double successRate;


        public Ghost(String p, int x, int y, double psuccess) {
                pic = p;
                xpos = x;
                ypos = y;
                dx = (int)(Math.random()*11) -5;
                dy = (int)(Math.random()*11) -5;
                rect= new Rectangle(xpos, ypos, width, height);
                isAlive = true;
                successRate = psuccess;

        }
        public void move(){
          xpos=xpos + dx;
          ypos = ypos + dy;
          rect= new Rectangle(xpos, ypos, width, height);

        }
        public void bounce() {
                xpos = xpos + dx;
                ypos = ypos + dy;
                rect= new Rectangle(xpos, ypos, width, height);

                if (xpos <= 0) {
                   dx=-dx;

                } else if (xpos >=1000) {
                      dx=-dx;
                }

                if (ypos <= 0) {
                        dy = -dy;
                } else if (ypos >= 700) {
                      dy=-dy;
                }
        }




        public void wrap(){
        xpos = xpos + dx;
        ypos = ypos + dy;
        rect= new Rectangle(xpos, ypos, width, height);

        if(xpos <0) {
        xpos = 1000;
        } else if(xpos > 1000){
                xpos= 0;
        }
                if(ypos < 0){
                        ypos = 700;
                } else if (ypos > 700) {
                 ypos = 0;
                }
      }
}




