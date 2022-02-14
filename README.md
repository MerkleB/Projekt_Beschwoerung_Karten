# SummonMasters > Projekt_Beschwoerung_Karten

Dieses Git Repository enthält die Spielmechaniken zum Spiel Summon Masters. 
Aktuell sind refactoring Maßnahmen nötig so das die folgenden Konzepte möglicherweise nicht mit dem Code übereinstimmen.

# Worum geht es

Summon Masters ist ein Kartenspiel bei dem sich zwei Spieler (im folgenden Summon Master genannt) gegenüberstehen. 
Das Ziel eines jeden Summon Masters ist es die Lebenpunkte des Gegners auf 0 zu reduzieren. Dies kann durch den Angriff von Beschwörungen oder durch den Einsatz von Zaubern geschehen.
Hat ein Summon Master es geschafft die Lebenspunkte des Gegners auf 0 zu bringen hat er gewonnen. Fallen in einem Zug die Lebenspunkte beider Summon Master auf 0 gewinnt der Summon Master, dessen Lebenspunkte zuletzt auf 0 gefallen sind.

# Grundlegende Spielelemente

Hier werden die Grundlegenden Spielelemente erklärt.

## Kartentypen

In Summon Masters gibt es zwei unterschiedliche Kartentypen: Beschwörungen und Sprüche. Im folgenden sind die beiden Typen genauer beschrieben.

### Beschwörungen

Mit Beschwörungen kann ein Summon Master mächtige Kreaturen herbeirufen. Jede Kreatur gehört sowohl einer Klasse als auch einem Element an. Die Klassen sind Krieger, Magier, Ungeheuer, Magisches Ungeheuer und Maschine. Die Elemente sind Feuer, Wasser, Erde, Luft, Licht und Finsternis.

Eine Beschwörung zeichnet sich durch folgende Attribute aus:

* Name: Der Name der Kreatur
* Beschwörungskosten: Die Anzahl an Manapunkten die benötigt werden um die Kreatur zu beschwören.
* Magischer Angriffswert: Stärke der maigschen Angriffe.
* Physischer Angriffswert: Stärke der pyhsischen Angriffe.
* Magische Verteidigung: Widerstandsfähigkeit gegen magische Angriffe
* Pyhsische Verteidigung: Widerstandsfähigkeit gegne pyhsische Angriffe 

### Sprüche


## Spieler

Spieler stellen den Summon Master im Spiel dar. Spieler sind durch folgende Attribute gekennzeichnet.

### Name

Der Name des Spielers.

### Mana

Mana ist die Energie die ein Summon Master benötigt um Karten zu spielen. Das Beschwören einer Kreatung oder as sprechen eines Zaubers verbraucht eine gewisse Menge an Mana. 
Zu beginn eines jeden Spiels hat jeder Spieler 20 Mana Punkte. Zu beginn jedes Zuges regneriert ein Spieler 4 Punkte. Es gibt Karten, die die Manaregeneration pro Zug erhöhen. Ein Spieler kann maximal 100 Manapunkte ansammenln. 