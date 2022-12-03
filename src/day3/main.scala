type Path = String

@main
def main: Unit =
  println(s"Part 1: $part1")
  println(s"Part 2: $part2")

def getFileContent(p: Path): Seq[String] = 
  import scala.io.Source
  Source.fromFile(p).getLines.toVector

def getScore(c: String): Int =
  if Character.isUpperCase(c(0)) then (c(0) - 'A').toInt + 27
  else (c(0) - 'a') + 1

def part1: Int =
  val backpacks = getFileContent("./input.txt")
  var res: Int = 0
  for backpack <- backpacks do
    val len = backpack.length / 2
    val first = backpack.split("").take(len)
    val second = backpack.split("").takeRight(len)
    res += getScore(first.intersect(second)(0))

  res

def part2: Int =
  val backpacks = getFileContent("./input.txt")
  var res: Int = 0
  var i: Int = 0;
  while i < backpacks.length do
    var arr = backpacks(i).split("")
    for ii <- 1 until 3 do
      arr = arr.intersect(backpacks(i + ii).split(""))
    
    res += getScore(arr(0))
    i += 3
  res