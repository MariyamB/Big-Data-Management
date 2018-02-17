//Creating the Tellers Table
Create external table Teller(Name string,Bills array<int>)
Row format delimited
Fields terminated by ','
Collection items terminated by ' '
Lines terminated by '\n'
Stored as textfile
LOCATION '/user/training/hive/TellersBills';

//Loading The Bank Data inot Tellers Table
load data inpath '/user/training/hive/bankdata' into table Teller;

//Creating the Partioned Table pt_teller
Create table pt_teller(Bills array<int>) 
Partitioned by(Name String) 
Row format delimited 
Fields terminated by ','
LOCATION'/user/training/hive/pt_teller';

//Setting partition to non strict and TRUE
hive> set hive.exec.dynamic.partition.mode=nonstrict;
hive> set hive.exec.dynamic.partition=true;

//Inserting into the partiton table
INSERT OVERWRITE TABLE pt_teller PARTITION(Name)SELECT Bills,Name FROM Teller;

//Loding the exploded bills 
select subs from Teller lateral view explode(bills) subs as children;
1
5
10
20

//Count of the Bills for each teller in External table
select name,sum(a.bills) 
from
(select * from Teller lateral View explode(bills)sub as children)
a group by name,bills;

Jon     11
Jon     30
Jon     14
Jon     15
Mo      10
Mo      12
Mo      24
Mo      15
Mo      15
Ron     12
Ron     15
Saeed   15
Saeed   13
Steve   24
Todd    14
Todd    13
Todd    5

//Count of the bills for all tellers
Select name, count(name1) as count FROM Teller LATERAL VIEW explode(bills)
subs as name1 group by name;

Jon     70
Mo      76
Ron     27
Saeed   28
Steve   24
Todd    32

//Gives the sum of all the counts from above query:(i.e) Total Bills by all Tellers
select sum(sub) as sum from(
select Name,Bills from Teller lateral view explode(bills)sub as bills1)a;

257


//Teller Name and exploded bills :
Select name, name1 FROM Teller LATERAL VIEW explode(bills) subs as name1;

Saeed   20
Saeed   5
Saeed   5
Saeed   10
Saeed   1
Saeed   1
Saeed   1
Saeed   1
Saeed   1
Saeed   5
Saeed   10
Saeed   10
Saeed   10
Steve   1
Steve   1
Steve   1
Steve   1
Steve   1
Steve   5
Steve   10
Steve   20
Steve   10
Steve   10
Steve   10
Steve   10
Mo      1
Mo      1
Mo      5
Mo      1
Mo      10
Mo      5
Mo      10
Mo      20
Mo      10
Mo      20
Mo      1
Mo      1
Mo      10
Mo      10
Mo      10
Todd    1
Todd    1
Todd    1
Todd    1
Todd    1
Todd    5
Todd    10
Todd    20
Todd    10
Todd    10
Todd    10
Todd    10
Todd    10
Todd    10
Jon     1
Jon     1
Jon     1
Jon     1
Jon     1
Jon     5
Jon     10
Jon     20
Jon     10
Jon     10
Jon     10
Jon     10
Jon     5
Jon     20
Jon     1
Jon     20
Jon     5
Jon     5
Jon     10
Jon     1
Jon     1
Jon     1
Jon     1
Jon     1
Jon     5
Jon     5
Jon     5
Jon     5
Jon     5
Jon     5
Steve   1
Steve   1
Steve   1
Steve   1
Steve   1
Steve   5
Steve   10
Steve   20
Steve   10
Steve   10
Steve   10
Steve   10
Todd    20
Todd    5
Todd    5
Todd    10
Todd    1
Todd    1
Todd    1
Todd    1
Todd    1
Todd    5
Todd    10
Todd    10
Todd    10
Mo      1
Mo      1
Mo      1
Mo      1
Mo      1
Mo      5
Mo      10
Mo      20
Mo      10
Mo      10
Saeed   1
Saeed   1
Saeed   5
Saeed   1
Saeed   10
Saeed   5
Saeed   10
Saeed   20
Saeed   10
Saeed   20
Saeed   1
Saeed   1
Saeed   10
Saeed   10
Saeed   10
Jon     1
Jon     1
Jon     1
Jon     1
Jon     1
Jon     5
Jon     10
Jon     20
Jon     10
Jon     10
Jon     10
Jon     10
Jon     5
Jon     20
Jon     1
Mo      1
Mo      1
Mo      1
Mo      1
Mo      1
Mo      5
Mo      10
Mo      20
Mo      10
Mo      10
Mo      10
Mo      20
Mo      5
Mo      5
Mo      10
Mo      1
Mo      1
Mo      1
Mo      1
Mo      1
Mo      5
Mo      10
Mo      10
Mo      10
Ron     1
Ron     1
Ron     1
Ron     1
Ron     1
Ron     5
Ron     10
Ron     20
Ron     10
Ron     10
Ron     10
Ron     10
Ron     1
Ron     1
Ron     5
Ron     1
Ron     10
Ron     5
Ron     10
Ron     20
Ron     10
Ron     20
Ron     1
Ron     1
Ron     10
Ron     10
Ron     10
Jon     1
Jon     1
Jon     1
Jon     1
Jon     1
Jon     5
Jon     10
Jon     20
Jon     10
Jon     10
Jon     10
Jon     10
Jon     10
Jon     10
Mo      20
Mo      5
Mo      5
Mo      10
Mo      1
Mo      1
Mo      1
Mo      1
Mo      1
Mo      5
Mo      5
Mo      5
Mo      5
Mo      5
Mo      5
Mo      1
Mo      1
Mo      1
Mo      1
Mo      1
Mo      5
Mo      10
Mo      20
Mo      10
Mo      10
Mo      10
Mo      10
Jon     1
Jon     1
Jon     1
Jon     1
Jon     1
Jon     5
Jon     5
Jon     5
Jon     5
Jon     5
Jon     5
Todd    20
Todd    10
Todd    10
Todd    10
Todd    10

//Sum of the Bills for each teller:
select distinct Teller.name, agg.sum
from (
  select name, sum(name1) as sum
  from (Select name, name1 FROM Teller LATERAL VIEW explode(bills) subs as name1)a
  group by name
) agg
join Teller
on agg.name = Teller.name;

Jon     422
Mo      480
Ron     195
Saeed   195
Steve   160
Todd    240



/*PART A
1)Prints the total number of bills, sum of the bills for all tellers*/

SELECT distinct c.sum,c.count 
 FROM (Select name, name1 FROM Teller LATERAL VIEW explode(bills) subs as name1)a
 JOIN
(SELECT SUM(b.name1) sum,count(b.name1) count 
	FROM 
	(Select name, name1 FROM Teller LATERAL VIEW explode(bills) subs as name1)b)c;

1692    257

//2)Prints the total number of bills, sum of the bills and avg of the bills for each teller
select name, sum(name1) as sum,avg(name1) as avg,count(name1) as count
from 
(Select name, name1 FROM Teller LATERAL VIEW explode(bills) subs as name1)a
group by name;

Jon     422     6.0285714285714285      70
Mo      480     6.315789473684211       76
Ron     195     7.222222222222222       27
Saeed   195     6.964285714285714       28
Steve   160     6.666666666666667       24
Todd    240     7.5     32

//3)Same Count of the bills for all tellers
select a.name,a.count1 from
(select c.name,count(name3) as count1 FROM Teller c LATERAL VIEW explode(c.bills)subs as name3 group by c.name)a
join
(select d.name,count(name3) as count1 FROM Teller d LATERAL VIEW explode(d.bills)subs as name3 group by d.name)b
on a.count1=b.count1
where
a.name!=b.name;

NULL

//Same Sum of bills for tellers
select a.name,a.count1 from
(select c.name,sum(name3) as count1 FROM Teller c LATERAL VIEW explode(c.bills)subs as name3 group by c.name)a
join
(select d.name,sum(name3) as count1 FROM Teller d LATERAL VIEW explode(d.bills)subs as name3 group by d.name)b
on a.count1=b.count1
where
a.name!=b.name;

Ron     195
Saeed   195


/*PART 3

Q1)Number of Partitions*/
select count(distinct Name) from pt_tellers1;

6

//Q2)Prints the total number of bills, sum of the bills and avg of the bills for each teller
select name, sum(name1) as sum,avg(name1) as avg,count(name1) as count
from 
(Select name, name1 FROM pt_teller LATERAL VIEW explode(bills) subs as name1)a
group by name;

Jon     422     6.0285714285714285      70
Mo      480     6.315789473684211       76
Ron     195     7.222222222222222       27
Saeed   195     6.964285714285714       28
Steve   160     6.666666666666667       24
Todd    240     7.5     32

//Q3)Print the name of the teller with maximum sum of the bills

select name, sum(name1) as sum
from 
(Select name, name1 FROM pt_teller LATERAL VIEW explode(bills) subs as name1)a
group by name
sort by sum desc limit 1;

Mo      480
