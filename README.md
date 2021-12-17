# MorphogenesisSimulationV2
Morphogenesis Simulator Updated

Note: The readme/ documentation currently describes the DevStable branch of the project.
To have the most up-to-date, stable code, please refer to the DevStable branch.

Morphogenesis Simulator:
An animated physics simulator used to model folding of tissue during morphogenesis.

Simulation framework:
The simulation operates by a given number of "states" of which only one is currently implemented.
These "states" perform the following operations:
      -Create(): makes a "monobehavior" type object. These objects use update() and render() functions and are the active components of the simulation
      -awake(): any functions to perform on creation of the state object are done here
      -Tick(): update the physics on all monobehavior objects in cached in the state
      -Render(): render all rendereable objects cached in the state
    
This simulation uses an object-oriented approach to break structures into cells, edges, and nodes.
For the particular case we're studying here (gastrulation in Drosophila), each cell is made up of 10 nodes.
An edge describes the connection between two nodes. A cell is composed of a list of nodes, edges, and internal edges (edges used to described internal stiffness of the cells).
There is only one type of node at this time. There are multiple types of edges and cells that are constructed for the simulation. This allows higher-level functions to access
any type of edge or cell, while allowing for different actions to occur when those functions are called. For instance, an ApicalConstrictingCell tells any ApicalEdges to actively constrict during
its update() method, while other cells would not do this when update() is called.

Nodes are the discrete units which the physics acts upon. For ease of understanding, physics calculations are often abstracted to act on edges and cells rather than just nodes themselves.
For instance, a force acting on an edge describes that force acting upon two of its nodes. Likewise, a force acting on a cell is acting on all of the nodes.

In this simulation all of the "monobehavior" objects described earlier are cells and a single "model" object. During each update, the cell performs the actions listed in its update method.
The model object currently acts as a bit of a wrapper, performing tissue level tasks such as collision detection between cells.

The following physics functions are currently implemented: (F = force and k = constant in all cases)
  -Active and passive springs: using Hooke's law F = k * (l - l0) where l/l0 = length/ initial length
  -Volume conservation: using a 2D interpretation of the spring function F = k * (a - a0), where a/a0 = area/ initial area
