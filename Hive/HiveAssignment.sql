drop table Teller;
drop table ptTeller;

Create external table teller(Name string,Bills array<int>)
Row format delimited
Fields terminated by ','
Collection items terminated by ' '
Lines terminated by '\n'
Stored as textfile
LOCATION '/user/training/hive/Teller';


load data inpath '/user/training/hive/bankdata' into table Teller;
Select * from Teller;

Create table ptTeller(Bills array<int>)
Partitioned by(Name String)
Row format delimited
Fields terminated by ','
LOCATION'/user/training/hive/ptTeller';

set hive.exec.dynamic.partition.mode=nonstrict;
set hive.exec.dynamic.partition=true;


INSERT OVERWRITE TABLE ptTeller PARTITION(Name)SELECT Bills,Name FROM Teller;
Select * from ptTeller;


SELECT distinct c.sum,c.count
FROM (Select name, name1 FROM Teller LATERAL VIEW explode(bills) subs as name1)a
JOIN
(SELECT SUM(b.name1) sum,count(b.name1) count
FROM
(Select name, name1 FROM Teller LATERAL VIEW explode(bills) subs as name1)b)c;




select name, sum(name1) as sum,avg(name1) as avg,count(name1) as count
from
(Select name, name1 FROM Teller LATERAL VIEW explode(bills) subs as name1)a
group by name;



select a.name,a.count1 from
(select c.name,count(name3) as count1 FROM Teller c LATERAL VIEW explode(c.bills)subs as name3 group by c.name)a
join
(select d.name,count(name3) as count1 FROM Teller d LATERAL VIEW explode(d.bills)subs as name3 group by d.name)b
on a.count1=b.count1
where
a.name!=b.name;



select a.name,a.count1 from
(select c.name,sum(name3) as count1 FROM Teller c LATERAL VIEW explode(c.bills)subs as name3 group by c.name)a
join
(select d.name,sum(name3) as count1 FROM Teller d LATERAL VIEW explode(d.bills)subs as name3 group by d.name)b
on a.count1=b.count1
where
a.name!=b.name;



select count(distinct Name) from ptTeller;



select name, sum(name1) as sum,avg(name1) as avg,count(name1) as count
from
(Select name, name1 FROM ptTeller LATERAL VIEW explode(bills) subs as name1)a
group by name;



select name, sum(name1) as sum
from
(Select name, name1 FROM ptTeller LATERAL VIEW explode(bills) subs as name1)a
group by name
sort by sum desc limit 1;

