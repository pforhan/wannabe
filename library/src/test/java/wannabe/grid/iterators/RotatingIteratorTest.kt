package wannabe.grid.iterators

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import wannabe.Position
import wannabe.Voxel
import wannabe.grid.SimpleGrid

class RotatingIteratorTest {

  @Test
  fun `no rotation`() {
    val grid = SimpleGrid("test")
    grid.put(Voxel(1, 2, 3, 10))
    grid.put(Voxel(4, 5, 6, 20))

    val iterator = RotatingIterator(grid.iterator(), RotationDegrees())

    assertTrue(iterator.hasNext())
    assertEquals(Voxel(1, 2, 3, 10), iterator.next())
    assertTrue(iterator.hasNext())
    assertEquals(Voxel(4, 5, 6, 20), iterator.next())
    assertFalse(iterator.hasNext())
  }

  @Test
  fun `rotate around X-axis by 90 degrees`() {
    val grid = SimpleGrid("test")
    grid.put(Voxel(1, 0, 0, 10))
    grid.put(Voxel(1, 1, 1, 20))

    val iterator = RotatingIterator(grid.iterator(), RotationDegrees(x = 90))

    assertTrue(iterator.hasNext())
    assertEquals(Voxel(1, 0, 0, 10), iterator.next()) // (1, 0, 0) -> (1, 0, 0)
    assertTrue(iterator.hasNext())
    assertEquals(Voxel(1, -1, 1, 20), iterator.next()) // (1, 1, 1) -> (1, -1, 1)
    assertFalse(iterator.hasNext())
  }

  @Test
  fun `rotate around Y-axis by 90 degrees`() {
    val grid = SimpleGrid("test")
    grid.put(Voxel(0, 1, 0, 10))
    grid.put(Voxel(1, 1, 1, 20))

    val iterator = RotatingIterator(grid.iterator(), RotationDegrees(y = 90))

    assertTrue(iterator.hasNext())
    assertEquals(Voxel(0, 1, 0, 10), iterator.next()) // (0, 1, 0) -> (0, 0, -1)
    assertTrue(iterator.hasNext())
    assertEquals(Voxel(1, 1, -1, 20), iterator.next()) // (1, 1, 1) -> (1, 1, -1)
    assertFalse(iterator.hasNext())
  }

  @Test
  fun `rotate around Z-axis by 90 degrees`() {
    val grid = SimpleGrid("test")
    grid.put(Voxel(1, 0, 0, 10))
    grid.put(Voxel(1, 1, 1, 20))

    val iterator = RotatingIterator(grid.iterator(), RotationDegrees(z = 90))

    assertTrue(iterator.hasNext())
    assertEquals(Voxel(0, 1, 0, 10), iterator.next()) // (1, 0, 0) -> (0, 1, 0)
    assertTrue(iterator.hasNext())
    assertEquals(Voxel(-1, 1, 1, 20), iterator.next()) // (1, 1, 1) -> (-1, 1, 1)
    assertFalse(iterator.hasNext())
  }

  @Test
  fun `rotate around multiple axes by 90 degrees`() {
    val grid = SimpleGrid("test")
    grid.put(Voxel(1, 0, 0, 10))

    // Order of operations is Z, then Y, then X.
    // (1, 0, 0) -> (0, 1, 0) -> (0, 0, -1) -> (0, 0, -1)
    val iterator = RotatingIterator(grid.iterator(), RotationDegrees(x = 90, y = 90, z = 90))

    assertTrue(iterator.hasNext())
    assertEquals(Voxel(0, 0, -1, 10), iterator.next())
    assertFalse(iterator.hasNext())
  }

  @Test
  fun `rotate around custom point by 90 degrees`() {
    val grid = SimpleGrid("test")
    grid.put(Voxel(2, 1, 0, 10))
    grid.put(Voxel(2, 2, 1, 20))

    // Rotate around the point (1, 1, 0)
    // (2, 1, 0) -> (1, 2, 0)
    // (2,2,1) -> (0,2,1)
    val iterator = RotatingIterator(grid.iterator(), RotationDegrees(z = 90), Position(1, 1, 0))

    assertTrue(iterator.hasNext())
    assertEquals(Voxel(1, 2, 0, 10), iterator.next())
    assertTrue(iterator.hasNext())
    assertEquals(Voxel(0, 2, 1, 20), iterator.next())
    assertFalse(iterator.hasNext())
  }

  @Test
  fun `rotate multiple voxels around z by 90 degrees`() {
    val grid = SimpleGrid("test")
    grid.put(Voxel(1, 0, 0, 10))
    grid.put(Voxel(0, 1, 0, 20))

    val iterator = RotatingIterator(grid.iterator(), RotationDegrees(z = 90))

    // Note that internally simplegrid re-sorts the voxels we put in:
    assertTrue(iterator.hasNext())
    assertEquals(Voxel(-1, 0, 0, 20), iterator.next()) // (0, 1, 0) -> (-1, 0, 0)
    assertTrue(iterator.hasNext())
    assertEquals(Voxel(0, 1, 0, 10), iterator.next()) // (1, 0, 0) -> (0, 1, 0)
    assertFalse(iterator.hasNext())
  }

  @Test
  fun `rotate 180 degrees around x`() {
    val grid = SimpleGrid("test")
    grid.put(Voxel(1, 0, 0, 10))
    grid.put(Voxel(1, 1, 1, 20))

    val iterator = RotatingIterator(grid.iterator(), RotationDegrees(x = 180))

    assertTrue(iterator.hasNext())
    assertEquals(Voxel(1, 0, 0, 10), iterator.next()) // (1, 0, 0) -> (1, 0, 0)
    assertTrue(iterator.hasNext())
    assertEquals(Voxel(1, -1, -1, 20), iterator.next()) // (1, 1, 1) -> (1, -1, -1)
    assertFalse(iterator.hasNext())
  }

  @Test
  fun `rotate 180 degrees around y`() {
    val grid = SimpleGrid("test")
    grid.put(Voxel(1, 0, 1, 10))

    val iterator = RotatingIterator(grid.iterator(), RotationDegrees(y = 180))

    assertTrue(iterator.hasNext())
    assertEquals(Voxel(-1, 0, -1, 10), iterator.next()) // (1, 0, 1) -> (-1, 0, -1)
    assertFalse(iterator.hasNext())
  }

  @Test
  fun `rotate 180 degrees around z`() {
    val grid = SimpleGrid("test")
    grid.put(Voxel(1, 0, 0, 10))

    val iterator = RotatingIterator(grid.iterator(), RotationDegrees(z = 180))

    assertTrue(iterator.hasNext())
    assertEquals(Voxel(-1, 0, 0, 10), iterator.next()) // (1, 0, 0) -> (-1, 0, 0)
    assertFalse(iterator.hasNext())
  }

  @Test
  fun `translate around 0,1,0`() {
    val grid = SimpleGrid("test")
    grid.put(Voxel(1, 1, 1, 10))

    val iterator = RotatingIterator(grid.iterator(), RotationDegrees(z = 0), Position(0, 1, 0))

    assertTrue(iterator.hasNext())
    assertEquals(Voxel(1, 1, 1, 10), iterator.next())
    assertFalse(iterator.hasNext())
  }
}
