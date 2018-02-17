-- general.pig
--
A = load '/home/training/pig/students.txt' using PigStorage('\t') as ($f1:int, $f2:chararray, age:int, gpa:float);
B = foreach A generate name;
dump B;