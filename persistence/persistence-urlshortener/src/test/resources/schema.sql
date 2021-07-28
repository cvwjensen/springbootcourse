drop table if exists token CASCADE;
drop table if exists user CASCADE;
create table token (token varchar(255) not null, created_date timestamp, protect_token varchar(255), target_url varchar(255), updated_date timestamp, user_username varchar(255), primary key (token));
create table user (username varchar(255) not null, created_date timestamp, password varchar(255), updated_date timestamp, primary key (username));
alter table token add constraint FKpe6rqghhlj1x4f42m2i0vq4hc foreign key (user_username) references user;
