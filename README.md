# tiling_puzzle_solver
The Tiling Puzzle problem is a type of exact cover problem that can be efficiently solved using Knuth’s Dancing Links algorithm (aka AlgorithmX).  The algorithm models the problem as a sparse matrix represented by a circularly four-way linked list.  For distributed-solving, the problem is broken down into sub sparse matrices (sub-problems) which are then dispatched to client/worker nodes for processing.

--------------------------------------
** How to build the program**
---------------------------------------
1. Make sure that apache ant and jdk 1.7 are installed in the system. 
2. cd to project home folder say FinalProject.
3. Run ant_build.sh script to build the source code as:
	./ant_build.sh source_code.
	The TilingSolver.jar file will be created under dist folder inside source_code folder. 
4. In case of errors, set up the environment as:
	export ANT_HOME=<path to where ant is installed> such as /usr/local/ant
	export JAVA_HOME=<path to where jdk 1.7 is installed> such as /usr/local/jdk1.7.0_51
	export PATH=${PATH}:${ANT_HOME}/bin
5. Repeat steps 2 and 3.

--------------------------------------
** How to run the program in the cluster**
--------------------------------------
1. cd to FinalProject/bin
2. change runParallelClients.sh to update the path to that of FinalProject/bin folder.
3. execute runServer.sh script to start the server as:
	./runServer.sh <puzzleFileName> <distributedMode> <orientationMode> [exploreLevel] [numOfClientInstance] [portNo]
	e.g. ./runServer.sh puzzles/pentominoes3x20_shift true all 2 100 3000

4. execute runParallelClients.sh to run the clients in cluster in parallel
	./runParallelClients.sh
5. execute killJavaInstances.sh to clean up any running java processes
	./killJavaInstances.sh


--------------------------------------
**Report
--------------------------------------
Read "TilingPuzzleSolver.pdf" for project implementation details.

--------------------------------------
**bin folder
--------------------------------------
-contains jar executable and associated scripts


--------------------------------------
**source_code folder
--------------------------------------
-contains source code for the project


--------------------------------------
**standardPuzzles folder
--------------------------------------
-contains standard puzzles provided for testing application


--------------------------------------
**customPuzzles folder
--------------------------------------
-contains 5 custom created puzzles


--------------------------------------
**standardPuzzlesOutput folder
--------------------------------------
- contains outputs for puzzles in standardPuzzles folder


--------------------------------------
**customPuzzlesOutput folder
--------------------------------------
- contains outputs for puzzles in customPuzzles folder

--------------------------------------
**ant_build.sh
--------------------------------------
script for building the project.
