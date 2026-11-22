ORE LEAKER - Meteor-Addon (Quellcode) fuer Minecraft 1.21.11 / Fabric

WICHTIG
- Das ist Quellcode, keine fertige .jar. Du musst es einmal bauen.
- Ungetestet: Meteors API aendert sich oft. Falls der Build meckert, nimm die offizielle
  Vorlage https://github.com/MeteorDevelopment/meteor-addon-template (gradle-Dateien,
  Versionen) und kopiere nur den Ordner src/main/java/com/oreleaker + fabric.mod.json hinein.
- Das Addon zeigt nur Bloecke, die dein Client wirklich bekommt. Bei Anti-Xray auf dem Server
  musst du dir als Host die Bypass-Berechtigung geben (Paper: paper.antixray.bypass).

BAUEN
1. JDK 21 installieren.
2. In diesem Ordner:  gradlew build   (oder gradle build, wenn Gradle installiert ist)
3. Die Datei build/libs/ore-leaker-1.0.0.jar in den mods-Ordner legen
   (zusammen mit Fabric API, Meteor Client und OPSEC).

NUTZUNG
Meteor-GUI (Rechts-Shift) -> Kategorie "Ore Leaker" -> Modul "ore-leaker" aktivieren.
