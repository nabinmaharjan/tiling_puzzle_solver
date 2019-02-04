# tiling_puzzle_solver
The Tiling Puzzle problem is a type of exact cover problem that can be efficiently solved using Knuth’s Dancing Links algorithm (aka AlgorithmX).  The algorithm models the problem as a sparse matrix represented by a circularly four-way linked list.  For distributed-solving, the problem is broken down into sub sparse matrices (sub-problems) which are then dispatched to client/worker nodes for processing.
