
// Den Code bespreche ich ausführlich im Podcast "Herzbergs Hörsaal" in der Episode
// https://anchor.fm/dominikusherzberg/episodes/PiS-Das-Nim-Spiel-in-Java-programmiert-edks2t
//
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
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

class MoveSim {

    private Move move;
    private int wins;
    private int losses;
    private int result;

    public MoveSim(Move move) {
        this.move = move;
        this.wins = 0;
        this.losses = 0;
        this.result = 0;
    }

    public int calcWin() {
        return result = wins - losses;
    }

    public Move getMove() {
        return move;
    }

    public void inkrementWins() {
        wins++;
    }

    public void inkrementLosses() {
        losses++;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getResult() {
        return result;
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

    public ArrayList possibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        for (int i = 0; i < rows.length; i++) {
            for (int j = 1; j <= rows[i]; j++) {
                moves.add(Move.of(i, j));
            }
        }
        return moves;
    }

    public Move mcMove() {
        ArrayList<Move> moves = new ArrayList<>();
        for (int i = 0; i < rows.length; i++) {
            for (int j = 1; j <= rows[i]; j++) {
                moves.add(Move.of(i, j));
            }
        }
        ArrayList<MoveSim> bestMoves = new ArrayList<>();
        for (Move move : moves) {
            bestMoves.add(new MoveSim(move));
        }

        int simLength = 100;
        for (int i = 0; i < simLength; i++) {
            for (MoveSim simulation : bestMoves) {
                Nim copyNim = new Nim(this.rows);
                // copyNim = copyNim.play(simulation.getMove());
                while (!copyNim.isGameOver()) {
                    copyNim = copyNim.play(copyNim.randomMove());
                }
                if (NimGame.isWinning(copyNim.rows)) {
                    simulation.inkrementWins();
                } else {
                    simulation.inkrementLosses();
                }
            }
        }

        
        bestMoves.sort((a, b) -> b.calcWin() - a.calcWin());

        int highestWinValue = bestMoves.get(0).calcWin();
        List<MoveSim> highestWinMoves = bestMoves.stream()
                .filter(move -> move.calcWin() == highestWinValue)
                .collect(Collectors.toList());

        if (highestWinMoves.size() > 1) {
            Random r = new Random();
            MoveSim randomBestMove = highestWinMoves.get(r.nextInt(highestWinMoves.size()));
            return randomBestMove.getMove();
        }
        return bestMoves.get(0).getMove();
    }

    public String evaluateMcMove() {
        int wins = 0;
        String result = "";
        // min 5 mal spielen
        for (int i = 0; i < 5; i++) {
            //100 durchläufe
            for (int j = 0; j < 100; j++) {
                Nim randomNim = randomSetup();
                randomNim.toString();
                while (!randomNim.isGameOver()) {
                    randomNim = randomNim.play(randomNim.mcMove());
                }
                if (NimGame.isWinning(randomNim.rows)) {
                    wins++;
                }
            }
            
        }
            double totalWinRate = (double) wins / (5 * 100) * 100;
            return totalWinRate + "%\n";
    }

    public Nim randomSetup() {
        Random r = new Random();
        int numRows = r.nextInt(5) + 1;
        int totalSticks;
        do {
            rows = new int[numRows];
            totalSticks = 0;
            for (int i = 0; i < rows.length; i++) {
                rows[i] = r.nextInt(6) + 2; 
                totalSticks += rows[i];
            }
        } while (totalSticks < 5);
        return Nim.of(rows);
    }
}
/*
 * Nim nim = Nim.of(2,3,4);assert
 * nim!=nim.play(Move.of(1,2)):"Return a new Nim instance";
 * 
 * int[] randomSetup(int... maxN) {
 * Random r = new Random();
 * int[] rows = new int[maxN.length];
 * for (int i = 0; i < maxN.length; i++) {
 * rows[i] = r.nextInt(maxN[i]) + 1;
 * }
 * return rows;
 * }
 * 
 * ArrayList<Move> autoplay(NimGame nim) {
 * ArrayList<Move> moves = new ArrayList<>();
 * while (!nim.isGameOver()) {
 * Move m = nim.bestMove();
 * moves.add(m);
 * nim = nim.play(m);
 * }
 * return moves;
 * }
 * 
 * boolean simulateGame(int... maxN) {
 * Nim nim = Nim.of(randomSetup(maxN));
 * // System.out.println(nim);
 * // System.out.println((NimGame.isWinning(nim.rows) ? "first" : "second") +
 * " to win");
 * ArrayList<Move> moves = autoplay(nim);
 * // System.out.println(moves);
 * return (NimGame.isWinning(nim.rows) && (moves.size() % 2) == 1) ||
 * (!NimGame.isWinning(nim.rows) && (moves.size() % 2) == 0);
 * }
 * 
 * assert IntStream.range(0,100).allMatch(i->
 * 
 * simulateGame(3,4,5));
 * assert IntStream.range(0,100).allMatch(i -> simulateGame(3,4,6,8));
 */
/*
 * // Beispielhaftes Spiel über JShell
 * 
 * jshell> Nim n = Nim.of(2,3,4)
 * n ==>
 * I I
 * I I I
 * I I I I
 * 
 * jshell> n = n.play(n.bestMove())
 * n ==>
 * I I
 * I I I
 * I
 * 
 * jshell> n = n.play(Move.of(2,1))
 * n ==>
 * I I
 * I I I
 * 
 * 
 * jshell> n = n.play(n.bestMove())
 * n ==>
 * I I
 * I I
 * 
 * 
 * jshell> n = n.play(Move.of(1,1))
 * n ==>
 * I I
 * I
 * 
 * 
 * jshell> n = n.play(n.bestMove())
 * n ==>
 * I
 * I
 * 
 * 
 * jshell> n = n.play(Move.of(1,1))
 * n ==>
 * I
 * 
 * 
 * 
 * jshell> n = n.play(n.bestMove())
 * n ==>
 * 
 * 
 * 
 * 
 * jshell> n.isGameOver()
 * $25 ==> true
 */