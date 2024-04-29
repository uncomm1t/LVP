Clerk.clear();
Clerk.markdown(STR.
"""
# Nim Spiel - Monte Carlo Verfahren
        
In diesem Praktikum wird das Nim Spiel um die methode _mcMove()_ erweitert. Diese Methode soll den besten Zug für den aktuellen Spielzustand mit Hilfe des Monte Carlo Verfahrens ermitteln.
Zur Hilfe wurde eine Klasse _MoveSim_ erstellt um die Auswertung der Simulationen zu vereinfachen.
        
MoveSim hat folgende Eigenschaften:
Inhalt | Erklärung
-------|----------
Move move | Der Zug der simuliert wird, wird in einem Move Objekt gespeichert
int wins | Anzahl der Siege
int losses | Anzahl der Niederlagen
int result | Speichert die Differenz aus wins und losses
inkrementWins() | Inkrementiert die Anzahl der Siege, da die Variable private ist
inkrementLosses() | s.o.
calcWin() | Berechnet die Differenz aus wins und losses und speichert sie in result
get set | Getter und Setter für verschiedene Variablen

# Die mcMove() Methode

Die Methode mcMove() soll uns den besten Zug für den aktuellen Spielzustand zurückgeben. 
hierzu erstellenn wir eine _Move_ ArrayList, in der wir alle möglichen Züge speichern.

```java
ArrayList<Move> moves = new ArrayList<>();
for (int i = 0; i < rows.length; i++) {
    for (int j = 1; j <= rows[i]; j++) {
        moves.add(Move.of(i, j));
    }
}
```	
Als nächstes übertraen wir die _Move_ ArrayList in eine _MoveSim_ ArrayList, um gewinne und verluste zu speichern.
```java	
ArrayList<MoveSim> bestMoves = new ArrayList<>();
        for (Move move : moves) {
            bestMoves.add(new MoveSim(move));
        }
```

Nun simulieren wir 100 mal das Spiel und speichern die Ergebnisse in _bestMoves_.

_simLength_ können wir uns beliebig aussuchen, in diesem Fall 100.

Für alle Züge gehen nur wie folgt vor:

In der for-each Schleife wird ein Kopie unseres Spiels erstellt und ein zufälliger Zug ausgeführt, bis das Spiel beendet ist.
Je nachdem ob das Spiel gewonnen oder verloren wurde, wird die entsprechende Variable in _MoveSim_ inkrementiert. 
```java
int simLength = 100;
        for (int i = 0; i < simLength; i++) {
            for (MoveSim simulation : bestMoves) {
                Nim copyNim = new Nim(this.rows);
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
```
Nachdem wir die besten Züge ermittelt haben, müssen wir dafür sorgen, dass Züge mit gleichem Wert zufällig ausgewählt werden.

Dafür sortieren wir unsere liste mit den besten Zügen, falls es keine doppelten Werte gibt, wird der erste Zug zurückgegeben.

Ansonsten müssen wir in einer neuen Liste alle gleichen Züge speichern und schauen, ob diese größer als 1 ist, falls ja wird ein zufälliger Zug ausgewählt.

```java	
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
```

# Die evaluateMcMove() Methode

Die Methode _evaluateMcMove()_ benutzt _randomSet()_ um Zufällige Spiele zu erstellen und simulieren mit _mcMove()_.

```java
public Nim randomSetup() {
    Random r = new Random();
    int numRows = r.nextInt(5) + 1;
    int[] rows;
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
```


In der Methode _evaluateMcMove()_ wird 5 mal ein Spiel mit 100 Simulationen gespielt und die Gewinnrate ausgegeben.



```java	
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
```


""");

