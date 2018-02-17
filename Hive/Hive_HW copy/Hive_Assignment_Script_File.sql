-- Name: Mariyam Rajeev George
-- Hive Assignment

/*Write a set of Hive scripts that load these files into a staging external table called, Teller.
Create a managed partitioned table, called PtTeller that partitions the rows in the table Teller
dynamically based on the teller’s name.*/

drop table Teller;
drop table ptTeller;

--Creating Teller Table
Create external table teller(Name string,Bills array<int>)
Row format delimited
Fields terminated by ','
Collection items terminated by ' '
Lines terminated by '\n'
Stored as textfile
LOCATION '/user/training/hive/Teller';

--Loading Data into Teller Table
load data inpath '/user/training/hive/bankdata' into table Teller;

--Displaying Teller Table contents
Select * from Teller;

--Creating ptTeller Table
Create table ptTeller(Bills array<int>)
Partitioned by(Name String)
Row format delimited
Fields terminated by ','
LOCATION'/user/training/hive/ptTeller';

--Setting dynamic partition constraints
set hive.exec.dynamic.partition.mode=nonstrict;
set hive.exec.dynamic.partition=true;

--Inserting Data into ptTeller Table
INSERT OVERWRITE TABLE ptTeller PARTITION(Name)SELECT Bills,Name FROM Teller;

--Displaying Teller Table contents
Select * from ptTeller;

--PART A
--Working with table Teller, write a set of Hive scripts that print the answers the following
--questions: 
--Q1: The total amount of money and total number of bills collected by all tellers. 
SELECT distinct c.sum,c.count
FROM (Select name, name1 FROM Teller LATERAL VIEW explode(bills) subs as name1)a
JOIN
(SELECT SUM(b.name1) sum,count(b.name1) count
FROM
(Select name, name1 FROM Teller LATERAL VIEW explode(bills) subs as name1)b)c;

--Q2:The teller name and the total amount of money collected and the number of bills and the
--average money for each teller (as done in the MR job). 
select name, sum(name1) as sum,avg(name1) as avg,count(name1) as count
from
(Select name, name1 FROM Teller LATERAL VIEW explode(bills) subs as name1)a
group by name;

--Q3: Print the teller name and the number of bills for those tellers that have collected the same
--number of bills. 
select a.name,a.count1 from
(select c.name,count(name3) as count1 FROM Teller c LATERAL VIEW explode(c.bills)subs as name3 group by c.name)a
join
(select d.name,count(name3) as count1 FROM Teller d LATERAL VIEW explode(d.bills)subs as name3 group by d.name)b
on a.count1=b.count1
where
a.name!=b.name;

/*PART B
Working with the PtTeller table, write a set of scripts that print the answers to the following
questions: */
--Q1: The number of partitions in this table.
select count(distinct Name) from ptTeller;

--Q2: The teller name and the total amount of money collected and the number of bills and
--the average money for each teller (as done in the MR job).
select name, sum(name1) as sum,avg(name1) as avg,count(name1) as count
from
(Select name, name1 FROM ptTeller LATERAL VIEW explode(bills) subs as name1)a
group by name;

--Q3: The teller or the tellers’ names that have collected the maximum amount of money. 
select name, sum(name1) as sum
from
(Select name, name1 FROM ptTeller LATERAL VIEW explode(bills) subs as name1)a
group by name
sort by sum desc limit 1;


