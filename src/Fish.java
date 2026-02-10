public class Fish {

        Fish[] pond = new Fish[50];
        int size = 10;
        public static void main(String[] args) {


            Fish[] pond = new Fish[50];
            for (int x = 0; x < 50; x = x + 1) {
                Fish fish = new Fish();
                pond [x] = fish;
            }
        }
        public void moveThings(){
            for(int x = 0; x < size; x = x +1) {
                pond[x] = new Fish();
            }
        }
    }


