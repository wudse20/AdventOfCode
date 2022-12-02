module Main where
    main :: IO ()
    main = do
        file <- readFile "input.txt"
        putStrLn . show . part1 . prepareData $ file -- Part 1 answer
    
    prepareData :: String -> [[String]]
    prepareData = map words . lines

    part1 :: [[String]] -> Int
    part1 [] = 0
    part1 (xs:xss) = line xs + part1 xss
        where
            line :: [String] -> Int
            line xs
                | win xs = 6 + score xs
                | draw xs = 3 + score xs
                | otherwise = score xs
                    where
                        win :: [String] -> Bool
                        win xs = 
                            ((xs !! 0) == "A" && (xs !! 1) == "Y") || 
                            ((xs !! 0) == "B" && (xs !! 1) == "Z") || 
                            ((xs !! 0) == "C" && (xs !! 1) == "X")
                        draw :: [String] -> Bool
                        draw xs =
                            ((xs !! 0) == "A" && (xs !! 1) == "X") || 
                            ((xs !! 0) == "B" && (xs !! 1) == "Y") || 
                            ((xs !! 0) == "C" && (xs !! 1) == "Z")
                        score :: [String] -> Int
                        score xs
                            | xs !! 1 == "X" = 1
                            | xs !! 1 == "Y" = 2
                            | xs !! 1 == "Z" = 3
                            | otherwise = 0
