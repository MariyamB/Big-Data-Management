-- ps.pig
REGISTER /home/training/pig/myudfs.jar;
A = LOAD 'students.txt' using PigStorage('\t') AS (id: int, name: chararray, age: int, gpa: float);
B = FOREACH A GENERATE myudfs.UPPER(name);
store B into 'output';

