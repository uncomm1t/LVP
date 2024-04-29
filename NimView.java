
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

class NimView extends Nim {

    public static NimView of(int... rows) {
        
        return new NimView(rows);

    }

    private NimView(int... rows) {
        super(rows);
    }

   

    public void show(Turtle t) {
        t.reset();
        int distance = 0;
        for (int i = 0; i < rows.length; i++) {
             for (int j = 0; j < rows[i]; j++) {
                t.right(90).forward(10).penUp().backward(10).left(90).forward(10).penDown();
                distance += 10;
                
            }
            t.penUp().backward(distance).right(90).forward(20).penDown().left(90);
            distance = 0;
        }
    }
}
Clerk.clear();
Clerk.markdown(STR.
"""
# NimView: Visualisierung einer Nim-Spielinstanz

Die `NimView`-Klasse ermöglicht die visuelle Darstellung eines Nim-Spiels durch die Verwendung der Turtle-Programmierung mit Clerk. Sie erweitert die `Nim`-Klasse und bietet Methoden zum Spielen von Zügen und zur Anzeige des aktuellen Spielzustands.
Die Erklärung des Nim-Spiels finden Sie [auf Wikipedia](https://de.wikipedia.org/wiki/Nim-Spiel).


## Methoden

`NimView` bietet die folgenden Methoden:

Befehl | Bedeutung
-------|----------
`NimView.of(int... rows)` | Erstellt eine neue `NimView`-Instanz mit der angegebenen Anzahl an Reihen und Stäbchen
`Move of(int row, int number)` | Erstellt einen neuen Zug mit der angegebenen Reihe und Anzahl an Stäbchen
`NimView play(Move... moves)` | Spielt die angegebenen Züge im Nim-Spiel
`bestMove()` | Gibt den besten Zug für den aktuellen Spielzustand zurück
`randomMove()` | Gibt einen zufälligen Zug für den aktuellen Spielzustand zurück
`toString()` | Gibt eine textuelle Repräsentation des aktuellen Spielzustands zurück
`isGameOver()` | Überprüft, ob das Spiel beendet ist
`hashCode()` | Gibt den Hashcode des aktuellen Spielzustands zurück
`equals(Object obj)` | Überprüft, ob das aktuelle Objekt gleich einem anderen Objekt ist
`show()` | Zeigt den aktuellen Spielzustand visuell mithilfe der Clerks in LiveViewProgramming an


## Beispielverwendung

Wir erstellen ein Nim-Spiel mit 3 Reihen und 2, 3 und 4 Stäbchen in den Reihen. Danach benutzen wir die Turtle um uns das Spiel zu zeichnen.

```java	
 \{Text.cutOut("./NimView.java", "// NimviewExample")}
```
Das Spielfeld sieht dann so aus:
   
"""); 

// NimviewExample
Turtle t = new Turtle(300, 300);
 NimView m = NimView.of(2, 3, 4);
 m.show(t);
// NimviewExample

Clerk.markdown(STR.
"""
    Wir können uns den `hashCode` der Instanz anzeigen lassen:
    
    ```java	
    \{Text.cutOut("./NimView.java", "// NimviewHashCode")}
    ```
    Der Hashcode der Instanz ist: `\{m.hashCode()}`

    """);

// NimviewHashCode  
    NimView m = NimView.of(2, 3, 4);
    m.hashCode();
// NimviewHashCode

Clerk.markdown(STR.
"""
    Wir könen auch zwei Instanzen mit equals()` vergleichen:
    
    ```java
    \{Text.cutOut("./NimView.java", "// NimviewEquals")}
    ```
    Nun kriegen wir _true_ oder _false_ zurück: `\{m.equals(x)}`

    """);

// NimviewEquals
    NimView x = NimView.of(2, 3, 4);
    m.equals(x);
// NimviewEquals