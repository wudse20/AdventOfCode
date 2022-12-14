@main
def run: Unit =
  println(s"Part 1: $part1")

def parseInput(p: String): Seq[Path] =
  import scala.io.Source
  import scala.collection.mutable.ArrayBuffer

  def strToPos(str: String): Pos =
   val split = str.split(",")
   Pos(split(0).toInt, split(1).toInt)

  val lines = Source.fromFile(p).getLines.toVector
  val buf = ArrayBuffer.empty[Path]

  for line <- lines do
    val l = line.split(" -> ")
                .map(strToPos)
                .toVector
    buf += Path(l)

  buf.toVector

def part1: Int =
  def simulateFall(p: Seq[Pos], sand: Pos): Option[Pos] =
    if !(p contains Pos(sand.x, sand.y + 1)) then Some(Pos(sand.x, sand.y + 1))
    else if !(p contains Pos(sand.x - 1, sand.y + 1)) then Some(Pos(sand.x - 1, sand.y + 1))
    else if !(p contains Pos(sand.x + 1, sand.y + 1)) then Some(Pos(sand.x + 1, sand.y + 1))
    else None

  var paths: Vector[Pos] = parseInput("input.txt").map(_.all).flatten.toVector
  val lastRockY = paths.map(_.y).max
  var res = 0
  var current = Pos(500, 0)
  var shouldRun = true
  while shouldRun do
    simulateFall(paths, current) match
      case Some(x) => current = x
      case _ => res += 1; paths = paths.appended(current); current = Pos(500, 0)
    shouldRun = !(current.y > lastRockY)
  res

case class Pos(x: Int, y: Int)
object Pos:
  def inbetween(p1: Pos, p2: Pos): Seq[Pos] =
    if p1.x == p2.x && p1.y < p2.y then for i <- p1.y to p2.y yield Pos(p1.x, i)
    else if p1.x == p2.x && p1.y > p2.y then for i <- p2.y to p1.y yield Pos(p1.x, i)
    else if p1.x < p2.x then for i <- p1.x to p2.x yield Pos(i, p1.y)
    else for i <- p2.x to p1.x yield Pos(i, p1.y)

case class Path(path: Vector[Pos]):
  lazy val all: Seq[Pos] =
    (for i <- 0 until path.length - 1 yield Pos.inbetween(path(i), path(i + 1))).flatten
