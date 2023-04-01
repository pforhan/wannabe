wannabe
=======

Multiplatform low-resolution voxel 3d engine.  For losers, by losers.

Summary
-------
> [Wikipedia](https://en.wikipedia.org/wiki/Voxel): A voxel represents a value on a regular grid in three-dimensional space.

This project contains simple 3d data structures and code to support a graphics engine, along with some sample voxel structures and demonstration application.

Code
----
Here's a quick overview of how the code represents voxel data

* A `Position` describes a point in 3d Cartesian space, with x, y, and z values.
* A `Voxel` is a `Position` and an integer value.
* A `Grid` is a collection of `Voxels`

Then there's how to display these:

* `Translation` is a mutable form of `Position`.
* A `Camera` keeps track of where we're looking, by translating a grid to its local space.
* A `Projection` converts a Position to a pixel location, given a Camera and a pixel size.  There's `Flat`, `Cabinet`, and `PseudoPerspective` Projections.
* A `UI` keeps track of the Grid, the Camera, and the Projection, and actually renders the result in a platform-specific way. The Swing UI implementation uses a variety of `SwingRenderer` classes, seen below.

Samples
-------
You can run SwingWannabe, a Kotlin/Swing implementation that I'm using as my testbed. Run its main method from the IDE, or try `./gradlew swing_demo:run` on the command line. It has a selection of sample grids, along with projection and render mode selections.

**Rotation Video tests (click to play)**

[![Perspective Block Test](https://img.youtube.com/vi/MfYdNTo8nR8/0.jpg)](https://www.youtube.com/watch?v=MfYdNTo8nR8) [![Perspective Circle Test](https://img.youtube.com/vi/bdmP57BH-_A/0.jpg)](https://www.youtube.com/watch?v=bdmP57BH-_A)[![Cabinet Block Test](https://img.youtube.com/vi/s7FAf_rRXvQ/0.jpg)](https://www.youtube.com/watch?v=s7FAf_rRXvQ) [![Interactive Test](https://img.youtube.com/vi/HWenUpl_C-Q/0.jpg)](https://www.youtube.com/watch?v=HWenUpl_C-Q)

**Projection and Render tests (Click for larger views.)**

[![Projections](http://www.muddyhorse.com/wp-content/uploads/2020/05/wannabe-projections.gif)](http://www.muddyhorse.com/wp-content/uploads/2020/05/wannabe-projections-big.gif)
[![Render Types](http://www.muddyhorse.com/wp-content/uploads/2020/05/wannabe-renders.gif)](http://www.muddyhorse.com/wp-content/uploads/2020/05/wannabe-renders-big.gif)

Heightmap
---------

One of the cool sample Grids is a [heightmap](http://en.wikipedia.org/wiki/Heightmap) where shades of gray are translated to height and color and shown as voxels.

Given this original heightmap:

![Original Heightmap](http://www.muddyhorse.com/wp-content/uploads/2013/11/example-heightmap.png)

The sample applicaiton renders the upper-left 50x50 pixels of the heightmap as voxels. As shown in the animations above, there's a number of render modes available.  Shown here are filled circles, rounded-squares, squares, and "3d" squares.

![fillCircle](http://www.muddyhorse.com/wp-content/uploads/2013/11/fillCircle-292x300.png) ![fillRoundRect](http://www.muddyhorse.com/wp-content/uploads/2013/11/fillRoundRect-292x300.png) ![fillRect](http://www.muddyhorse.com/wp-content/uploads/2013/11/fillRect-292x300.png) ![fill3dRect](http://www.muddyhorse.com/wp-content/uploads/2013/11/fill3dRect-292x300.png)

And there's non-filled versions of the same shapes:

![circle](http://www.muddyhorse.com/wp-content/uploads/2013/11/circle-292x300.png) ![roundRect](http://www.muddyhorse.com/wp-content/uploads/2013/11/roundRect-292x300.png) ![rect](http://www.muddyhorse.com/wp-content/uploads/2013/11/rect-292x300.png) ![3drect](http://www.muddyhorse.com/wp-content/uploads/2013/11/3drect-292x300.png)

Limitations
-----------

As you can see from the samples, this isn't a photorealistic engine.  It's geared more for pixel-art and retro-style visuals, but allows for some nifty things, as the combinations of projections and renderers allow for very different looks.

Grids should be considered the smallest practical unit of animation.  Voxels inside a grid don't move relative to one another.  Grids can support adding and removing voxels, but a voxel should only be in a single Grid.

My expectation is that the camera will always be looking down the z-axis.  The position of the camera may change, but not the direction.

Why?
----
This is a way to learn some little bits about 3d graphics while working in a simple space.  And potentially it can be a good platform from which to develop some small games.  Also helps me think about and [write about](http://www.muddyhorse.com/category/technical/wannabe/) the process from a developer's standpoint.

License
-------
I chose the MIT license at this time.  Please contribute back fixes you come across!
