-- Name: Mariyam Rajeev George
-- Hive Assignment

/*Write a set of Hive scripts that load these files into a staging external table called, Teller.
Create a managed partitioned table, called PtTeller that partitions the rows in the table Teller
dynamically based on the teller’s name.*/

drop table hive1;
drop table hive2;

--Q3: 1)Creating Hive1 Table
Create table hive1(Name array<String>)
Row format delimited
Collection items terminated by ' '
Lines terminated by '\n'
Stored as textfile;


--Loading Data into hive1 Table
load data inpath '/user/training/hive/names.hive' into table hive1;

--Displaying hive1 Table contents
Select * from hive1;

--Q3. 2)Print the names in sorted order and for each name the number of times the name appears in the file
select a.name1,count(name1) as count
from
(Select name1 FROM hive1 LATERAL VIEW explode(Name) subs as name1)a
group by a.name1;


--Q4 Use table hive1 and hive 2 and print the each name and number of times that a name appears  in the 2 tables
--Creating hive2 Table
Create table hive2(Name String,count int)
Row format delimited
Fields terminated by  '\t'
Lines terminated by '\n'
Stored as textfile;

--Displaying Teller Table contents
Select * from hive2;

--Loading Data into hive2 Table
load data local inpath '/home/training/pigres' into table hive2;

select d.names2,SUM(d.counts1+d.counts2) from
(select distinct c.names as names2,c.count as counts1,b.count as counts2
from
 (select name1 as names,count(name1) as count
from
 (Select name1 FROM hive1 LATERAL VIEW explode(Name) subs as name1)a
group by name1)c
full outer join
(select Name,count from hive2)b
on c.names=b.Name)d
group by d.names2;



