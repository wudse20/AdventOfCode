namespace solution
{
    class Day11 
    {
        private readonly List<Monkey> monkies = new();

        private void PrepData(String path)
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
                                    .Select(s => Int64.Parse(s))
                                    .ToList();

                var opStr = lines[2].Split("=")[1]
                                    .Trim()
                                    .Split(" ");
                                    
                
                var op = opStr[1];
                var t1 = opStr[0];
                var t2 = opStr[2];

                Func<long, long> operation = x => {
                    switch(op)
                    {
                        case "+":
                            return (t1.Equals("old") ? x : Int64.Parse(t1)) + (t2.Equals("old") ? x : Int64.Parse(t2));
                        case "-":
                            return (t1.Equals("old") ? x : Int64.Parse(t1)) - (t2.Equals("old") ? x : Int64.Parse(t2));
                        case "*":
                            return (t1.Equals("old") ? x : Int64.Parse(t1)) * (t2.Equals("old") ? x : Int64.Parse(t2));
                        case "/":
                            return (t1.Equals("old") ? x : Int64.Parse(t1)) / (t2.Equals("old") ? x : Int64.Parse(t2));
                        default:
                            throw new Exception("Illegal operation");
                    }
                };

                var condNum = Int32.Parse(lines[3].Split(" ")[3]);
                Predicate<long> cond = x => x % condNum == 0;

                var trueIndex = Int32.Parse(lines[4].Split(" ")[5]);
                var falseIndex = Int32.Parse(lines[5].Split(" ")[5]);

                monkies.Add(new Monkey(index, falseIndex, trueIndex, condNum, items, cond, operation));
            }
        }

        private long solution(int rounds, Func<long, long> worryUpdate)
        {
            for (int i = 0; i < rounds; i++)
            {
                foreach (var monkey in monkies)
                {
                    foreach (var item in monkey.items.ToList())
                    {
                        monkey.count++;
                        var newLevel = worryUpdate.Invoke(monkey.operation.Invoke(item));
                        var passTo = monkey.condition.Invoke(newLevel) ? monkey.trueIndex : monkey.falseIndex;
                        monkies[passTo].AddItem(newLevel);
                        monkey.RemoveItem(item);
                    }
                }
            }

            var pq = new PriorityQueue<long, long>();
            foreach(var monkey in monkies)
                pq.Enqueue(monkey.count, -monkey.count);


            return pq.Dequeue() * pq.Dequeue();
        }

        private void run()
        {
            PrepData("input.txt");
            Console.WriteLine("Part 1: {0}", solution(20, x => x / 3));

            PrepData("input.txt");
            var mod = monkies.Aggregate(1, (mod, monkey) => mod * monkey.mod);
            Console.WriteLine("Part 2: {0}", solution(10_000, x => x % mod));
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
        public int mod;
        public readonly List<long> items;
        public readonly Predicate<long> condition;
        public readonly Func<long, long> operation;

        public long count = 0;

        public Monkey(
            int index, int falseIndex, int trueIndex, int mod, List<long> items,
            Predicate<long> condition, Func<long, long> operation
        )
        {
            this.index = index;
            this.falseIndex = falseIndex;
            this.trueIndex = trueIndex;
            this.mod = mod;
            this.items = items;
            this.condition = condition;
            this.operation = operation;
        }

        public void AddItem(long item)
        {
            items.Add(item);
        }

        public void RemoveItem(long item)
        {
            items.Remove(item);
        }

        public override string ToString()
        {
            return String.Format("{0}", count);
        }
    }
}