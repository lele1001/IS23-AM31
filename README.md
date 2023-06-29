# MyShelfie Board Game - Software Engineering Project

MyShelfie Board Game is the final test of **"Software Engineering"** course of **"Computer Science Engineering"** held
at Politecnico di Milano (2022/2023).

**Teacher**: Alessandro Margara

## The Team

* [Niccolò Brembilla](https://github.com/BrembillaNiccolo)
* [Luigi Bruzzese](https://github.com/luigibruzzese)
* [Eleonora Cabai](https://github.com/lele1001)
* [Milagros Adriana Casaperalta Garcia](https://github.com/MilagrosCasaperalta)

## Project specification

The project consists of a Java version of the board game *MyShelfie*, made by Cranio Creations.

<!-- Project requirements: [link](https://github.com/nicolozambon/ing-sw-2022-sciarrabba-sironi-zambon/blob/master/resources/requirements.pdf?raw=true). -->

The final version includes:

* [Initial UML diagram](https://github.com/lele1001/IS23-AM31/tree/main/deliveries/UML/initial).
* [Final UML diagram](https://github.com/lele1001/IS23-AM31/tree/main/deliveries/UML/final).
* Working game implementation, which has to be rules compliant.
* Source code of the implementation.
* Source code of unity tests.

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
- the jar must be run from the command line <code> java -jar IS23-AM31Server.jar </code>. In doing so, an integer indicating the access port to the server via socket can be added as a parameter; the port for RMI will be the one specified + 1. In the absence of this parameter or if it is not valid, the server sets 1500 (socket) and 1501 (RMI) as default ports;
- for persistence, the program checks if there is a folder named 'MyShelfieSavedGames' inside the JAR directory: if so, it reads any json files with saved games inside it, otherwise it creates it.

### Client:
- when executing the jar by double clicking on it or from the command line without parameters <code> java -jar IS23-AM31Client.jar </code>, the graphical interface (GUI) will automatically start, and then user can choose the connection configuration to the server. Otherwise, to start the text interface, user needs to add 'cli' as the first parameter on the command line and, following, can also specify the type of connection ('rmi' or 'socket') and the port number (otherwise requested by the application at startup).
