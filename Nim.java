
// Den Code bespreche ich ausführlich im Podcast "Herzbergs Hörsaal" in der Episode
// https://anchor.fm/dominikusherzberg/episodes/PiS-Das-Nim-Spiel-in-Java-programmiert-edks2t
//
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

class Move {
    final int row, number;

    static Move of(int row, int number) {
        return new Move(row, number);
    }

    private Move(int row, int number) {
        if (row < 0 || number < 1)
            throw new IllegalArgumentException();
        this.row = row;
        this.number = number;
    }

    public String toString() {
        return "(" + row + ", " + number + ")";
    }
}

interface NimGame {
    static boolean isWinning(int... numbers) {
        return Arrays.stream(numbers).reduce(0, (i, j) -> i ^ j) != 0;
        // klassische Variante:
        // int res = 0;
        // for(int number : numbers) res ^= number;
        // return res != 0;
    }

    NimGame play(Move... moves);

    Move randomMove();

    Move bestMove();

    boolean isGameOver();

    String toString();
}


 class MoveSim {

    private Move platzhalter;
    private int  wins;
    private int  losses;

    public  MoveSim(Move move, int wins, int losses) {
        this.platzhalter = platzhalter;
        this.wins = wins;
        this.losses = losses;
    }
    
    public void possibleMoves(Nim nim){
        ArrayList<Move> moves = new ArrayList<>();
        for (int row = 0; row < nim.rows.length; row++) {
            for (int number = 1; number <= nim.rows[row]; number++) {
                moves.add(Move.of(row, number));
            }
        }

        ArrayList<MoveSim> MoveScoreList = new ArrayList<>();
        for (Move move : moves) {
            MoveScoreList.add(new MoveSim(move,0,0));
        }

        int simLength = 20;
        for (int i = 0; i < simLength; i++) {
            for (MoveSim moveSim : MoveScoreList) {
                nim
            }
        }
    } 



}

class Nim implements NimGame {
    private Random r = new Random();
    int[] rows;

    public static Nim of(int... rows) {
    
        return new Nim(rows);
    }

    protected Nim(int... rows) {
        if (rows.length > 5)
            throw new IllegalArgumentException("Es sind max. 5 Reihen erlaubt");
        for (int i : rows) {
            if (i > 7) {
                throw new IllegalArgumentException("Es sind max. 7 Stäbchen pro Reihe erlaubt");
            }
        }
        assert rows.length >= 1;
        assert Arrays.stream(rows).allMatch(n -> n >= 0);
        this.rows = Arrays.copyOf(rows, rows.length);
    }

    protected Nim play(Move m) {
        assert !isGameOver();
        assert m.row < rows.length && m.number <= rows[m.row];
        Nim nim = Nim.of(rows);
        nim.rows[m.row] -= m.number;
        return nim;
    }

    public Nim play(Move... moves) {
        Nim nim = this;
        for (Move m : moves)
            nim = nim.play(m);
        return nim;
    }

    public Move randomMove() {
        assert !isGameOver();
        int row;
        do {
            row = r.nextInt(rows.length);
        } while (rows[row] == 0);
        int number = r.nextInt(rows[row]) + 1;
        return Move.of(row, number);
    }

    public Move bestMove() {
        assert !isGameOver();
        if (!NimGame.isWinning(rows))
            return randomMove();
        Move m;
        do {
            m = randomMove();
        } while (NimGame.isWinning(play(m).rows));
        return m;
    }

    public boolean isGameOver() {
        return Arrays.stream(rows).allMatch(n -> n == 0);
    }

    public String toString() {
        String s = "";
        for (int n : rows)
            s += "\n" + "I ".repeat(n);
        return s;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null)
            return false;
        if (object == this)
            return true;
        if (object.getClass() != this.getClass())
            return false;
        Nim other = (Nim) object;
        return Arrays.equals(this.rows, other.rows);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(rows);
        return result;
    }


}


/* 
Nim nim = Nim.of(2,3,4);assert nim!=nim.play(Move.of(1,2)):"Return a new Nim instance";

    int[] randomSetup(int... maxN) {
        Random r = new Random();
        int[] rows = new int[maxN.length];
        for (int i = 0; i < maxN.length; i++) {
            rows[i] = r.nextInt(maxN[i]) + 1;
        }
        return rows;
    }

    ArrayList<Move> autoplay(NimGame nim) {
        ArrayList<Move> moves = new ArrayList<>();
        while (!nim.isGameOver()) {
            Move m = nim.bestMove();
            moves.add(m);
            nim = nim.play(m);
        }
        return moves;
    }

boolean simulateGame(int... maxN) {
    Nim nim = Nim.of(randomSetup(maxN));
    // System.out.println(nim);
    // System.out.println((NimGame.isWinning(nim.rows) ? "first" : "second") + " to win"); 
    ArrayList<Move> moves = autoplay(nim);
    // System.out.println(moves);
    return (NimGame.isWinning(nim.rows) && (moves.size() % 2) == 1) ||
           (!NimGame.isWinning(nim.rows) && (moves.size() % 2) == 0); 
}

assert IntStream.range(0,100).allMatch(i->

simulateGame(3,4,5));
assert IntStream.range(0,100).allMatch(i -> simulateGame(3,4,6,8));
*/
/* // Beispielhaftes Spiel über JShell

jshell> Nim n = Nim.of(2,3,4)
n ==>
I I
I I I
I I I I

jshell> n = n.play(n.bestMove())
n ==>
I I
I I I
I

jshell> n = n.play(Move.of(2,1))
n ==>
I I
I I I


jshell> n = n.play(n.bestMove())
n ==>
I I
I I


jshell> n = n.play(Move.of(1,1))
n ==>
I I
I


jshell> n = n.play(n.bestMove())
n ==>
I
I


jshell> n = n.play(Move.of(1,1))
n ==>
I



jshell> n = n.play(n.bestMove())
n ==>




jshell> n.isGameOver()
$25 ==> true
*/