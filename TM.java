import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

enum Move {
    LEFT, RIGHT
};

class Tape {

    private int position;
    private ArrayList<Character> cell;
    private char vbzeichen;

    public Tape(String input, char vbzeichen,int position) {
        this.vbzeichen = vbzeichen;
        this.position = position;
        cell = new ArrayList<>();
        cell.add(vbzeichen);
        for (int i = 0; i < input.length(); i++) {
            cell.add(input.charAt(i));
        }
        cell.add(vbzeichen);
    }

    public void move(Move direction) {
        if (direction == Move.LEFT) {
            position--;
            if (position < 0) {
                cell.add(0, vbzeichen);
                position = 0;
            }
        } else if (direction == Move.RIGHT) {
            position++;
            if (position >= cell.size()) {
                cell.add(vbzeichen);
            }
        }
    }

    void write(char c) {
        if (position >= cell.size()) {
            cell.add(c);
        } else {
            cell.set(position, c);
        }
    }

    char read() {
        return cell.get(position);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cell.size(); i++) {
            if (i == position) {
                sb.append("[").append(cell.get(i)).append("]");
            } else {
                sb.append(cell.get(i));
            }
        }
        return sb.toString();
    }
}

record Trigger(String fromState, char read) {
}

record Action(char write, Move move, String toState) {
    public char getWrite() {
        return write;
    }

    public Move getMove() {
        return move;
    }

    public String getToState() {
        return toState;
    }
}

class Table {

    private Map<Trigger, Action> table;

    public Table() {
        this.table = new HashMap<>();
    }

    public void addTransition(String fromState, char read, char write, Move move, String toState) {
        Trigger trigger = new Trigger(fromState, read);
        Action action = new Action(write, move, toState);
        table.put(trigger, action);
    }

    public Action getAction(String fromState, char read) {
        return table.get(new Trigger(fromState, read));
    }

    public void loadDecrementBinary() {
        // Diese Methode dient dazu, die Tabelle für das Dekrementieren einer Binärzahl zu laden.
        // Diese Tabelle ist aus der Vorlesung bekannt. Soll auch Zeit sparen.
        addTransition("S", '#', '#', Move.LEFT, "S");
        addTransition("S", '1', '0', Move.RIGHT, "R");
        addTransition("S", '0', '1', Move.LEFT, "L");
        addTransition("R", '0', '0', Move.RIGHT, "R");
        addTransition("R", '1', '1', Move.RIGHT, "R");
        addTransition("R", '#', '#', Move.LEFT, "W");
        addTransition("W", '1', '1', Move.RIGHT, "HALT");
        addTransition("W", '0', '0', Move.RIGHT, "HALT");
        addTransition("W", '#', '#', Move.RIGHT, "HALT");
        addTransition("L", '0', '1', Move.LEFT, "L");
        addTransition("L", '1', '0', Move.RIGHT, "R");
        addTransition("L", '#', '#', Move.RIGHT, "R");
    }

    public void loadMoveOnes(){
        // Diese Methode dient dazu, die Tabelle für die Verschiebung von Einsen zu laden.
        addTransition("S", '1', '1', Move.LEFT, "S");
        addTransition("S", 'S', 'S', Move.RIGHT, "HALT");
        addTransition("S", '0', '0', Move.LEFT, "0");
        addTransition("0", '0', '0', Move.LEFT, "0");
        addTransition("0", '1', '0', Move.RIGHT, "1");
        addTransition("0", 'S', 'S', Move.RIGHT, "HALT");
        addTransition("1", '0', '0', Move.RIGHT, "1");
        addTransition("1", '1', '1', Move.LEFT, "D");
        addTransition("1", 'S', 'S', Move.LEFT, "D");
        addTransition("D", '0', '1', Move.LEFT, "S");
        
    }

    
    public String tableToString(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Trigger, Action> entry : table.entrySet()) {
            sb.append(entry.getKey().fromState()).append(" ").append(entry.getKey().read()).append(" -> ");
            sb.append(entry.getValue().toState()).append(" ").append(entry.getValue().write()).append(" ");
            sb.append(entry.getValue().move()).append("\r\n");
        }
        return sb.toString();
    
    }

}

class TM {
    private Table table;
    private String fromState;
    private Tape tape;

    //Die Turing Maschine lädt standardmäßig die Tabelle für die Dekrementierung einer Binärzahl

    public TM(String input, char vbzeichen, int startposition) {
        this.table = new Table();
        this.table.loadDecrementBinary();
        //this.table.loadMoveOnes();
        this.fromState = "S";
        this.tape = new Tape(input, vbzeichen, startposition);
        }

        public void step() {
        char currentSymbol = tape.read();
        Action action = table.getAction(fromState, currentSymbol);
        tape.write(action.getWrite());
        tape.move(action.getMove());
        fromState = action.getToState();
        }

        public void run(){
            Clerk.markdown("## Dekrementierung einer Binärzahl");
            Clerk.markdown(tape.toString()); // Startzustand
        while(!fromState.equals("HALT")){
            step();
            Clerk.markdown(tape.toString()); //Zwischenstand bzw. Endzustand
        }
        Clerk.markdown("Die Turing Maschine funktiniert korrekt, da _11000_ zu _10111_ dekrementiert wurde.");
    }

    public String getTape(){
        return tape.toString();
    }

    public void getTable(){
        Clerk.markdown("## Tabelle Für Dekrementierung aus der Vorlesung");
        Clerk.markdown(table.tableToString());
    }
}
