# Guide to contributing to the Cell Mechanics Simulator

## Folder organization:

- component: All behaviors that can be attached to physics objects.

- framework: the base engine and base object classes of the simulation including states, entities/physics objects, and data saving

- input: classes responsible for handling user input and object selection

- General utilities.physics: specific components for basic physics, such as gravity.

- Morphogenesis: specific components to morphogenesis, such as cell osmosis and cell stiffness.

- renderer: classes responsible for graphics

- utilities: Additional functions including custom math libraries, string utilities, and other miscellaneous functions for making code readability easier.


## Adding behaviors/ new objects

Please use [this tutorial](https://github.com/jsutlive/Cell-Mechanics-Simulator/wiki/Example:-Writing-a-New-Component) to learn how to add new components to the system. If you would like to make it possible to add your new component from the GUI, please make sure to update the [user components spreadsheet](https://github.com/jsutlive/Cell-Mechanics-Simulator/blob/master/assets/UserComponents.csv). Additionally, please only add components in the base directory of the component package. If either the sheet is not updated or the component is in the wrong folder, the component _will not_ be available in the GUI but may be added via code. 

## Making System Level Changes

If making a system-level change that drastically alters any of the following:
    
- The way the system is rendered
- The way the objects in the scene are called/ updated
- The way data is stored, managed, and saved
- How the update loop is managed/ how the engine perceives time

please create a new branch and make all changes to a new branch before merging with the master branch.
