package wannabe.grid.iterators

import wannabe.Bounds
import wannabe.grid.Grid

fun <T> Iterator<T>.filter(predicate: (T) -> Boolean) = FilteringIterator(this, predicate)

fun Grid.hiddenRemoval() = iterator().filter { !neighbors(it).isSurrounded }

fun Grid.inBounds(bounds: Bounds) = iterator().filter { bounds.contains(it.position) }
