rmf $indir/BankPigAll
register myudfs/pigUDF_long.jar
a = load '$indir/bankdata/Day*.txt' using PigStorage('\t') as (line:chararray);
lima = limit a 10;
dump lima
d = foreach a generate flatten(myudfs.BankTeller(line));
limd = limit d 10;
dump limd
describe d;
e = group d by $0;
describe e;
dump e
tot = foreach e generate group,SUM(d.null::bill);
dump tot
