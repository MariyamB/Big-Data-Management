-- reverse.pig
REGISTER '/usr/lib/pig/piggybank.jar';
stus = LOAD 'pig/students.txt' using PigStorage('\t') AS (id: int, name: chararray, age: int, gpa: float);
names = foreach stus generate name;
reverse = FOREACH names GENERATE org.apache.pig.piggybank.evaluation.string.Reverse(name);
dump names;
dump reverse;
