****How to run the game****

**Running the Java Project
Prerequisites**

Java Development Kit (JDK) installed on your system.
Git installed to clone the repository.

**Getting Started**

Clone the repository:

    git clone https://github.com/Khayal-Aghazada/Chess-Game.git

Navigate to the project directory:

    cd Chess-Game/Chess

Locate the lib folder within the project directory. Find the jl1.0.1.jar file inside it. This is the JLayer library required for the project.

**Compiling and Running
On Windows**

Compile the project using the following command from the project root directory:

cmd

    javac -cp "src;lib/jl1.0.1.jar" src/Board.java src/Cell.java src/Game.java src/Piece.java src/PieceType.java src/SoundPlayer.java

Run the project (assuming Game contains the main method) with:

cmd

    java -cp "src;lib/jl1.0.1.jar" Game

**On Unix-based Systems (Linux, macOS)**

Compile the project using the following command from the project root directory:

bash

    javac -cp "src:lib/jl1.0.1.jar" src/Board.java src/Cell.java src/Game.java src/Piece.java src/PieceType.java src/SoundPlayer.java

Run the project (assuming Game contains the main method) with:

bash

    java -cp "src:lib/jl1.0.1.jar" Game
