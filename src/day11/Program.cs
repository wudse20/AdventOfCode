namespace solution
{
    class Day11 
    {
        private readonly List<Monkey> monkies = new();

        private void prepData(String path)
        {
            monkies.Clear();
            var input = File.ReadAllText(path)
                            .Replace("\r", "")
                            .Split("\n\n");

            foreach (var monkey in input)
            {
                var lines = monkey.Split("\n")
                                  .Select(s => s.Trim())
                                  .ToList();

                var index = Int32.Parse(lines[0].Replace("Monkey ", "").Replace(":", ""));

                var items = lines[1].Split(":")[1]
                                    .Split(",")
                                    .Select(s => s.Trim())
                                    .Select(s => Int32.Parse(s))
                                    .ToList();

                var opStr = lines[2].Split("=")[1]
                                    .Trim()
                                    .Split(" ");
                                    
                
                var op = opStr[1];
                var t1 = opStr[0];
                var t2 = opStr[2];

                Func<int, int> operation = x => {
                    switch(op)
                    {
                        case "+":
                            return (t1.Equals("old") ? x : Int32.Parse(t1)) + (t2.Equals("old") ? x : Int32.Parse(t2));
                        case "-":
                            return (t1.Equals("old") ? x : Int32.Parse(t1)) - (t2.Equals("old") ? x : Int32.Parse(t2));
                        case "*":
                            return (t1.Equals("old") ? x : Int32.Parse(t1)) * (t2.Equals("old") ? x : Int32.Parse(t2));
                        case "/":
                            return (t1.Equals("old") ? x : Int32.Parse(t1)) / (t2.Equals("old") ? x : Int32.Parse(t2));
                        default:
                            throw new Exception("Illegal operation");
                    }
                };

                var condNum = Int32.Parse(lines[3].Split(" ")[3]);
                Predicate<int> cond = x => x % condNum == 0;

                var trueIndex = Int32.Parse(lines[4].Split(" ")[5]);
                var falseIndex = Int32.Parse(lines[5].Split(" ")[5]);

                monkies.Add(new Monkey(index, falseIndex, trueIndex, items, cond, operation));
            }
        }

        private int Part1()
        {
            for (int i = 0; i < 20; i++)
            {
                foreach (var monkey in monkies)
                {
                    foreach (var item in monkey.items.ToList())
                    {
                        monkey.count++;
                        var newLevel = monkey.operation.Invoke(item) / 3;
                        var passTo = monkey.condition.Invoke(newLevel) ? monkey.trueIndex : monkey.falseIndex;
                        monkies[passTo].AddItem(newLevel);
                        monkey.RemoveItem(item);
                    }
                }
            }

            var pq = new PriorityQueue<int, int>();
            foreach(var monkey in monkies)
            {
                Console.WriteLine("Monkey {0} inspected items {1} times", monkey.index, monkey.count);
                pq.Enqueue(monkey.count, -monkey.count);
            }

            return pq.Dequeue() * pq.Dequeue();
        }

        private void run()
        {
            prepData("input.txt");
            Console.WriteLine("Part 1: {0}", Part1());
        }

        public static void Main(String[] args) 
        {
            new Day11().run();
        }
    }

    class Monkey
    {
        public readonly int index;
        public readonly int falseIndex;
        public readonly int trueIndex;
        public readonly List<int> items;
        public readonly Predicate<int> condition;
        public readonly Func<int, int> operation;

        public int count = 0;

        public Monkey(int index, int falseIndex, int trueIndex, List<int> items, Predicate<int> condition, Func<int, int> operation)
        {
            this.index = index;
            this.falseIndex = falseIndex;
            this.trueIndex = trueIndex;
            this.items = items;
            this.condition = condition;
            this.operation = operation;
        }

        public void AddItem(int item)
        {
            items.Add(item);
        }

        public void RemoveItem(int item)
        {
            items.Remove(item);
        }

        public override string ToString()
        {
            return String.Format("{0}", count);
        }
    }
}