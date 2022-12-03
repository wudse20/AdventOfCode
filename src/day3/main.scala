type Path = String

@main
def main: Unit =
  println(s"Part 1: ${part1}")

def getFileContent(p: Path): Seq[String] = 
  import scala.io.Source
  Source.fromFile(p).getLines.toVector

def part1: Int =
  def getScore(c: String): Int =
    if Character.isUpperCase(c(0)) then (c(0) - 'A').toInt + 27
    else (c(0) - 'a') + 1

  val backpacks = getFileContent("./input.txt")
  var res: Int = 0
  for backpack <- backpacks do
    val len = backpack.length / 2
    val first = backpack.split("").take(len)
    val second = backpack.split("").takeRight(len)
    res += getScore(first.intersect(second)(0))

  res