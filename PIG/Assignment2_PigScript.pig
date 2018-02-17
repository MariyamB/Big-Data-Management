register myudfs.jar
a= load 'names2.pig' as(line:chararray);
dump a
b= foreach a generate myUDFs.TestPig(line) as(name:chararray,value:int);
dump b
c= foreach a generate flatten(myUDFs.TestPig(line)) as(name:chararray,value:int);
e = group c by $0;
dump e;
e= foreach e generate group,SUM(c.value);
dump e;
