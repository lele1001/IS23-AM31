# MyShelfie Board Game - Software Engineering Project

MyShelfie Board Game is the final test of **"Software Engineering"** course of **"Computer Science Engineering"** held
at Politecnico di Milano (2022/2023).

NOTA: My Shelfie è un gioco da tavolo sviluppato ed edito da Cranio Creations Srl. I contenuti grafici di questo progetto riconducibili al prodotto editoriale da tavolo sono utilizzati previa approvazione di Cranio Creations Srl a solo scopo didattico. È vietata la distribuzione, la copia o la riproduzione dei contenuti e immagini in qualsiasi forma al di fuori del progetto, così come la redistribuzione e la pubblicazione dei contenuti e immagini a fini diversi da quello sopracitato. È inoltre vietato l'utilizzo commerciale di suddetti contenuti.

**Teacher**: Alessandro Margara

**Final Grade**: 30L

## The Team

* [Niccolò Brembilla](https://github.com/BrembillaNiccolo)
* [Luigi Bruzzese](https://github.com/luigibruzzese)
* [Eleonora Cabai](https://github.com/lele1001)
* [Milagros Adriana Casaperalta Garcia](https://github.com/MilagrosCasaperalta)

## Project specification

The project consists of a Java version of the board game *MyShelfie*, made by Cranio Creations.

## Documentation

The final version includes:

* [Initial UML diagrams](https://github.com/lele1001/IS23-AM31/tree/main/deliveries/UML/initial).
* [Final UML diagrams](https://github.com/lele1001/IS23-AM31/tree/main/deliveries/UML/final).
* Working game implementation, which has to be rules compliant.
* [Peer reviews' documentation](https://github.com/lele1001/IS23-AM31/tree/main/PeerReview).
* Source code of the implementation.
* [Jar files](https://github.com/lele1001/IS23-AM31/tree/main/deliveries/JAR).
* [JavaDoc documentation](https://github.com/lele1001/IS23-AM31/tree/main/deliveries/javadoc).
* Source code of unity tests.

The UML diagrams' folders contain also the sequence diagrams to explain the communication protocol between server and client.
> **NOTE**: the diagrams show the method invocations (in both directions) as happens for RMI-type connections; for socket, the parameters are exactly the same but are encapsulated and sent in json-formatted messages.

## Implemented functionalities

### Main functionalities

| Functionality                    |          Status           |
|:---------------------------------|:-------------------------:|
| Basic rules                      |             ✅             |
| Complete rules                   |             ✅             |
| RMI                              |             ✅             |
| Socket                           |             ✅             |
| TUI _(Textual User Interface)_   |             ✅             |
| GUI _(Graphical User Interface)_ |             ✅             |

### Advanced functionalities

| Functionality                | Status |
|:-----------------------------|:------:|
| Multiple simultaneous games  |   ⛔    |
| Persistence                  |   ✅    |
| Resilience to disconnections |   ✅    |
| Chat                         |   ✅    |

⛔ Not implemented &nbsp;&nbsp;&nbsp;&nbsp; :arrows_counterclockwise: In progress &nbsp;&nbsp;&nbsp;&nbsp; ✅ Implemented

## Execution

In order to correctly implement persistence, each game played has a name, chosen by the first client (together with the number of players) that connects. In this way, the server can use this name to save a json file with the details of the game at the end of each round.
For the first user who connects, the server checks if its nickname is in one or more saved games: if so, asks it to choose whether he wants to resume one or start a new game. Note that, in the latter case, the server sends the client a list with the names of the saved games to prevent it from choosing a name already in use.

Here are the details to start the game correctly.
There are two JAR files, one for the server (one boot for each game) and one for the client (two or more boots).

### Server:
- the jar must be run from the command line: <br><code> java -jar IS23-AM31Server.jar [<socket_port_number>]</code>.<br> In doing so, an integer indicating the access port to the server via socket can be added as a parameter; the port for RMI will be the one specified + 1. In the absence of this parameter or if it is not valid, the server sets 1500 (socket) and 1501 (RMI) as default ports;
- for persistence, the program checks if there is a folder named 'MyShelfieSavedGames' inside the JAR directory: if so, it reads any json files with saved games inside it, otherwise it creates it.

### Client:
- when executing the jar by double-click on it or from the command line without parameters: <br><code> java -jar IS23-AM31Client.jar</code>,<br> the graphical interface (GUI) will automatically start, and then user can choose the connection configuration to the server. Otherwise, to start the text interface, user needs to add 'cli' as the first parameter on the command line and, following, can also specify the type of connection ('rmi' or 'socket') and the port number (otherwise requested by the application at startup): <br><code> java -jar IS23-AM31Client.jar [cli [<rmi/socket> <port_number>]]</code>,<br>
