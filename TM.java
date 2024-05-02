import java.util.ArrayList;


enum Move {LEFT, RIGHT};

class Tape {

    int position = 0;
    ArrayList<Character> characters = new ArrayList<Character>();


    public void move(Move direction){
       
        switch (direction) {
            case LEFT:
                position++;
                break;
            case RIGHT:
                position--;
                break;
            default:
                throw new IllegalArgumentException("Unbekkante richtung");
        }

    }

    void write(char c){
        characters.set(position, c);
    }

    char read(){
        return characters.get(position);
    }

}

class Table{
    
}

class TM{

}