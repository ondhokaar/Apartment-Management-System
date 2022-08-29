create  table _ownerXflat(
	name varchar(255),
	completionDate varchar(255) not null,
	apt_no varchar(255) unique not null,
	phone varchar(255),
	
	
	constraint fk_ownerID foreign key (phone, name) references Owners(phone, name),
	constraint fk_flatID foreign key (completionDate, apt_no) references Flats(completionDate, apt_no)
);
use apt2
select * from Flats
select * from Owners
update Owners
set status_ = 'present'
where name = 'mmmm'
select * from _ownerXflat
insert into _ownerXflat values('mmmm', '2022-08-08', 'S3'  , 'aaaa')

select count(*), phone from _ownerXflat where phone = (select phone from _ownerXflat where apt_no ='A55') group by phone

          truncate table Flats
          truncate table _ownerXflat






select i from b where i = (select MAX(i) from b)

create table b (i int)


create database mdb
use mdb
select * from apt2.dbo.Billings
create table aaa (
i int identity(1,1) unique,
	ori datetime,
	ori2 varchar(244),
	ori3 date,

	xx as 'a' + i+ori2 not null
	)
	alter table aaa add (xx)
	insert into tt values('2022-12-12', '2022-12-12', GETDATE())
	
	select * from t
	
	INSERT tt SELECT 1;
SELECT SCOPE_IDENTITY() ;

alter table tht add pri
	
	
	
	create table ii (
	i int)
	insert into ii values(2)
	select * from ii
	update ii
	set i = 3
	