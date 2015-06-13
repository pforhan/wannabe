wannabe
=======

Multiplatform low-resolution 3d engine.  For losers, by losers.

Code
----
Right now, this is simply a collection of little classes.

* A `Position` holds x, y, and z locations.
* A `Voxel` is a Position and a color.
* A `Grid` is a collection of Voxels

Then there's how to display these:

* A `Camera` keeps track of where we're looking.
* A `Projection` converts a Position to a pixel location, given a Camera and a pixel size.  There's `Flat`, `Isometric`, and (still-in-progress) `PseudoPerspective` Projections.
* A `UI` keeps track of the Grid, the Camera, and the Projection, and actually renders the result.

Samples
-------
You can run SwingWannabe, a java-AWT and Swing implementation that I'm using as my testbed.  It loads a [heightmap](http://en.wikipedia.org/wiki/Heightmap) and displays the resulting Grid.  I went ahead and implemented 8 different render modes (shown here with the Isometric projection).

Original heightmap:

![Original Heightmap](http://www.muddyhorse.com/wp-content/uploads/2013/11/example-heightmap.png)

wannabe rendering the upper-left 50x50 pixels or so.  Render modes: circles, rounded-squares, squares, and "3d" squares.

![fillCircle](http://www.muddyhorse.com/wp-content/uploads/2013/11/fillCircle-292x300.png) ![fillRoundRect](http://www.muddyhorse.com/wp-content/uploads/2013/11/fillRoundRect-292x300.png) ![fillRect](http://www.muddyhorse.com/wp-content/uploads/2013/11/fillRect-292x300.png) ![fill3dRect](http://www.muddyhorse.com/wp-content/uploads/2013/11/fill3dRect-292x300.png)

And there's non-filled versions of the same shapes:

![circle](http://www.muddyhorse.com/wp-content/uploads/2013/11/circle-292x300.png) ![roundRect](http://www.muddyhorse.com/wp-content/uploads/2013/11/roundRect-292x300.png) ![rect](http://www.muddyhorse.com/wp-content/uploads/2013/11/rect-292x300.png) ![3drect](http://www.muddyhorse.com/wp-content/uploads/2013/11/3drect-292x300.png)

Limitations
-----------

As you can see from the samples, this isn't really meant to be a photorealistic engine.  It's geared more for pixel-art and retro-style games, but allows for some nifty things.

My expectation is that the camera will always be looking down the z-axis.  The position of the camera may change, but not the direction.

Why?
----
I don't have a better answer here than what I've said.  This is a way to learn some little bits about 3d graphics while working in a simple space.  And potentially it can be a good platform from which to develop some small games.

License
-------
I chose the MIT license at this time.  Please contribute back fixes you come across!
