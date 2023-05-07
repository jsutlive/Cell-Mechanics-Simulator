# Cell Mechanics Simulator

Requires java installed for the standalone .jar to work! Get Java [here](https://www.java.com/download/ie_manual.jsp).

For in-depth information, please consult the [wiki](https://github.com/jsutlive/CellMechanicsSimulator/wiki)!

Contributors, please check [CONTRIBUTING.md](https://github.com/jsutlive/Cell-Mechanics-Simulator/blob/master/CONTRIBUTING.md) for updates on how to best contribute to the project.
<p align="center">
<img src="https://github.com/jsutlive/Cell-Mechanics-Simulator/blob/master/assets/reference/GUI.png"
     alt="Cell Mechanics Simulator UI"
     width="750"
     style="float: left; margin-right: 10px;" />
</p>
Note: The readme/ documentation currently describes the Master branch of the project.
To have the most up-to-date, stable code, please refer to the Master branch.

The Morphogenesis Simulator is a physics simulator initially constructed to simulate developing organisms as mechanical systems. It is a soft-body dynamics representation and thus physics is typically represented as a spring-mass system. For more information about soft-body dynamics, this Wikipedia [article] https://en.wikipedia.org/wiki/Soft-body_dynamics is an excellent start.

## Features

### Component-based physics system

-Easily change behaviors of physics objects by adding and removing components.

-Easy coding of new components to add new behaviors to system.

### Functional GUI with object inspector

- Change parameters of models without needing to code with the object inspector. 

- Mouse select/multi-select objects

- Group objects

## Folder organization:

- Component: All behaviors that can be attached to physics objects.

- Framework: the base engine and base object classes of the simulation including states, entities/physics objects, and data saving

- Input: classes responsible for handling user input and object selection

- General Physics: specific components for basic physics, such as gravity.

- Morphogenesis: specific components to morphogenesis, such as cell osmosis and cell stiffness.

- Renderer: classes responsible for graphics

- Utilities: Additional functions including custom math libraries, string utilities, and other miscellaneous functions for making code readability easier.

## Cell Mechanics Simulator:

A real-time, 2D soft-body physics simulator used to model cell mechanics. Compatible with PC and Mac!
<p align="center">
<img src="https://github.com/jsutlive/MorphogenesisSimulationV2/blob/master/assets/reference/screenshot_hexmesh.png"
     alt="Markdown Monster icon"
     width="750"
     style="float: left; margin-right: 10px;" />
</p>

## Simulation framework:

The simulation has unique editor and simulation states which you can use to tweak model parameters via the component inspector.
Each state has behaviors it performs every physics update, which drive the simulation.
    
This simulation uses an object-oriented approach to break structures into cells, edges, and nodes. There are two current preset models available, a Drosophila embryo model and a hexagon cell sheet model.

An edge describes the connection between two nodes. There is only one type of node at this time, this will change if 3D representations become supported.

Nodes are the discrete units which the physics acts upon. For ease of understanding, physics calculations are often abstracted to act on edges and cells rather than just nodes themselves.
For instance, a force acting on an edge describes that force acting upon two of its nodes. Likewise, a force acting on a cell is acting on all of the nodes.

In this simulation all of the entities described earlier are cells and a single "model" entity controls group physics. During each update, the entity performs the actions listed in its update method.
The model object currently acts as a bit of a wrapper, performing tissue level tasks such as collision detection between cells.

The following physics functions are currently implemented: (F = force and k = constant in all cases)
  -Active and passive springs: using Hooke's law F = k * (l - l0) where l/l0 = length/ initial length
      Note: this value can be compressed (i.e. after a certain threshold, output equals a set percentage of the input, lowering the overall force output)
  -Volume conservation: using a 2D interpretation of the spring function F = k * (a - a0), where a/a0 = area/ initial area
  -2D Collision: uses the raycast algorithm to prevent meshes from colliding.
