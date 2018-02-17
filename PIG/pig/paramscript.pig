-- Paramscript.pig
REGISTER '/home/training/pig/myudfs.jar';
A = load '$indir/students.txt' using PigStorage('\t') as (id: int, name: chararray, age: int, gpa: float);
B = FOREACH A GENERATE myudfs.UPPER(name);
dump B;

