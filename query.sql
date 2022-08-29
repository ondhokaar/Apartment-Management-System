create database apt2;
use apt2;
drop database apt

select * from Flats

drop table Flats
select SYSDATETIME() SET IDENTITY_INSERT Flats ON;
alter table 
DROP CONSTRAINT  pk_flats

drop table _o
create  table Flats(
	sl INT identity(1,1),
	
	 
	--Flat + YY + month + dd # aptNo + sl
	flatID as ('Flat_' + cast(Datepart( yyyy, completionDate ) as varchar(255))
						+ cast(Datename( mm, completionDate  ) as varchar(255))
						+ cast(Datepart( dd, completionDate  ) as varchar(255))
						+ '#'
						+ apt_no
						+ cast (sl as varchar(255)) ),
	
	---------------------------------------------------------
	entryTimeStamp datetime not null,
	completionDate varchar(255) not null,
	apt_no varchar(255) unique not null,
	
	
	level_ int not null,
	specification text,
	
	area decimal(10,2),
	docs text,
	
	-----
	constraint pk_flats primary key(completionDate, apt_no )
	 
);
select * from apt2.dbo.O

select COUNT(*) from Flats where apt_no = 'apt_no'
select count(*) from Flats where apt_no = 'apt_no'
select * from Flats
truncate table Flats
select COUNT(*) from Flats
--add new flat
insert into truncate table  Flats values( GETDATE(), '2222-12-12', 'apt_no', 3, 'details', 3333003.3, 'docs' );
select * from _ 
alter table Owners add primary key(sl, phone)
create  table Owners(
	sl int identity(1, 1) UNIQUE,
	
	ownerID as 'OWNER_' + phone + '_' + cast (sl as varchar(10)),  -- 
	
	---------------------------------------------------------
	name varchar(255),
	phone varchar(255),
	email varchar(255),
	memberSince datetime,--now()
	status_ varchar(255), --current/former
	
	leavingDate datetime,
	
	nidFile text,
	
	constraint pk_owners primary key(phone,name)
	
);

USE apt2  
GO  
ALTER TABLE _
NOCHECK CONSTRAINT FK_PurchaseOrderHeader_Employee_EmployeeID;  
GO



select * from  truncate table Owners
select * from  truncate table Owners
select * from drop table _ownerXflat
insert into Owners values('name', 'phone', 'email', GETDATE(), 'present', null, '  asd')
insert into Owners values(  'asd', 'phone', 'd@f.com', GETDATE(), 'present', null, 'D:\Study\32\ISD LAB\PROJECT\AptMgmtSys\src\bilibili.gif')
(name, phone, email, memberSince, status, leavingDate, nidFile) 

-------------------------------------------   EMPLOYEE
use apt2
create table Employees(
	sl int identity(1, 1) UNIQUE,
	
	empID as 'EMP_' + cast(year(joiningDate) as varchar(255)) + phone + cast(sl as varchar(11)), 
	
	---------------------------------------------------------
	name varchar(255),
	phone varchar(255) UNIQUE,
	email varchar(255),
	joiningDate datetime,--now()
	designation varchar(255),
	salary decimal(10,2),
	status_ varchar(255), --current/former
	
	leavingDate datetime,
	
	nidFile text,
	
	constraint pk_emp primary key(phone, name)
);
insert into Employees values('adfa', '324', 'asdf', getdate(), 'asdf', 324, 'present', null, 'D:\Study\32\SD\project_progress-1.pdf')
select * from Employees
select sum(salary) from Employees where status_ = 'present';
insert into Employees values('name', 'phone', 'mail', GETDATE(), 'designation', 123.123, 'present', null, 'docs')

create table Payments(
	sl int identity(1, 1),
	entryTimeStamp datetime default now(),
	
	payID as 'PAY_' + cast(sl as varchar(11)),	-- concat meaningful sth instead
	---------------------------------------------------------
	amount decimal(20,4) not null,
	
	constraint fk_payment foreign key(entryTimeStamp)
	
	
);
use apt2
create  table Transactions(
	sl int identity(1, 1) unique,

	
	trxID as 'TRX' + cast(year(entryTimeStamp) as varchar(255)) + cast(sl as varchar(10)),  -- concat date/time/... instead
	
	---------------------------------------------------------
	entryTimeStamp datetime not null,
	trxtype varchar(255), --bill/pay
	trxAmount decimal(20, 4) not null,
	latestAvailableAmount decimal(30,4),
	
	constraint pk_trx primary key(sl,entryTimeStamp)
	
);

insert into Transactions values(GETDATE(), 'pay', 123445.32, calc )
select * from Transactions

--------------------------------------SERVICE
select * from ServiceProviders
create table ServiceProviders(
	sl int identity(1, 1) UNIQUE,
	
	
	spID as 'SP_' + phone + cast(sl as varchar(10)),
	
	---------------------------------------------------------
	details text,
	phone varchar(255) UNIQUE,
	name varchar(255),
	email varchar(255) UNIQUE,
	entryTimeStamp datetime,  --getdate()
	
	
	constraint pk_sp primary key(name, phone)
);
alter table ServiceProviders
add unique(email)
insert into ServiceProviders values('details', 'phone', 'name', 'email', GETDATE())
use apt2
create  table Billings(
	sl int identity(1,1) UNIQUE,

	
	
	billID as 'Bill_' + cast(Datename( mm, entrydate  ) as varchar(255)) + cast(year(entryDate) as varchar(5)) + '_' + cast(sl as varchar(10)),
	
	---------------------------------------------------------
	entryDate date,
	deadline date,
	
	amount decimal(20, 4),
	status_ varchar(255), --pending/paid
	
	phone varchar(255),
	name varchar(255),
	
	constraint pk_billings primary key(sl, entrydate), 
	constraint fk_billings foreign key(phone, name) references Owners(phone, name)
	
);
select max(entryDate) from Billings where name = 'shabbir' and phone = '01756060071s' a
select entryDate from Billings where sl = MAX(sl)
insert into Billings values('2022-05-10', '2022-9-9', 65, 'status_', '01756060071', 'shabbir')
delete from Billings where sl>1
alter dr table Billings

modify column  billID as 'Bill_' + cast(Datename( mm, entryDate  ) as varchar(255)) + cast(year(entryDate) as varchar(255)) + '_' + cast(sl as varchar(10))
delete drop table Billings

sele
use apt2
truncate table Billings 
add unique(sl)
select * from Billings

select @@IDENTITY from Billings
select billID from Billings where sl = (select SCOPE_IDENTITY())
create table _payXserv(
	payID varchar(255),
	spID varchar(255),

	constraint fk_payID foreign key (payID) references Payments(payID),
	constraint fk_spID foreign key (spID) references ServiceProviders(spID)
);
select billID from Billings where sl = (select scope_identity())
create table _payXtrx(
	payID varchar(255),
	trxID varchar(255),

	constraint fk_payID foreign key (payID) references Payments(payID),
	constraint fk_trxID foreign key (trxID) references Transactions(trxID)
);

create table _billXowner(
	billID varchar(255),
	ownerID varchar(255),

	constraint fk_billID foreign key (billID) references Billings(billID),
	constraint fk_ownerID foreign key (ownerID) references Owners(ownerID)
);

create table _billXtrx(
	billID varchar(255),
	trxID varchar(255),

	constraint fk_billID foreign key (billID) references Billings(billID),
	constraint fk_trxID foreign key (trxID) references Transactions(trxID)
);
select @@identity from Owners


use apt2
select * from Owners
select * from Owners
select * from _ownerXflat
insert into _ownerXflat( completionDate, apt_no, phone, name) values(   't_2022August10#SA41', '_2022August10#SA41', 'fasfd', 'fadsf')
insert into _ownerXflat values()
create  table _ownerXflat(
	name varchar(255),
	completionDate varchar(255) not null,
	apt_no varchar(255) unique not null,
	phone varchar(255),
	
	
	constraint fk_ownerID foreign key (phone, name) references Owners(phone, name),
	constraint fk_flatID foreign key (completionDate, apt_no) references Flats(completionDate, apt_no)
);
select * from truncate table Owners
create table _payXemp(
	payID varchar(255),
	empID varchar(255),



	
	constraint fk_payID foreign key (payID) references Payments(payID),
	constraint fk_empID foreign key (empID) references Employees(empID)
);

select sum(trxAmount) from Transactions where trxtype = 'pay' and Datepart( yyyy, entryTimeStamp) = '2022'
and datename(month, entryTimeStamp) like '%aug%'
select * from Transactions where Datepart( yyyy, entryTimeStamp) = '2022'
and datename(month, entryTimeStamp) like '%Aug%'
-------------------------------------------------------------------------
use apt2
select * from _ownerXflat
select  count(*) ct, name, phone from _ownerXflat  group by phone, name

create table	DateTrack (sl int identity(1, 1) unique, lastEmpPayment date);
drop table DateTrack
Modify column lastEmpPayment varchar(255)
select * from 

 truncate table DateTrack
use apt2
