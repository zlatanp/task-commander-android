
insert into user values ('1', 'dad@mail.com', '123');
insert into user values ('2', 'mum@mail.com', '125');
insert into user values ('3', 'kid@mail.com', '127');

insert into task_group values ('1', 'Home');
insert into task_group values ('2', 'Office');
insert into task_group values ('3', 'Car');
insert into task_group values ('4', 'Kids');
insert into task_group values ('5', 'Market');

insert into task_groups_members values (1, 1);
insert into task_groups_members values (1, 2);
insert into task_groups_members values (1, 3);
insert into task_groups_members values (1, 4);

insert into task_groups_members values (2, 1);
insert into task_groups_members values (2, 4);
insert into task_groups_members values (2, 5);

insert into task_groups_members values (3, 1);
insert into task_groups_members values (3, 4);

insert into task values (1, 1, 0, NOW() ,'Take the garbage out', -1000, -1000, 'Garbage', 1);
insert into task values (2, 1, 0, NOW(), 'Wash the sink',  -1000, -1000,'Kitchen', 2);
insert into task values (3, 1, 0, NOW(), 'Repair the washing machine', -1000, -1000, 'Washing machine', 1);
insert into task values (4, 1, 0, NOW(), 'Write the January report', -1000, -1000, 'Jan report', 1);
insert into task values (5, 1, 0, NOW(), 'Ask for a raise',  -1000, -1000, 'Bos', 1);
insert into task values (6, 1, 0, NOW(), 'Check the engine',  -1000, -1000,'Engine', 1);
insert into task values (7, 1, 0, NOW(), 'Do the math homework', -1000, -1000, 'Math', 3);
insert into task values (8, 1, 0, NOW(), 'Check the math homework',  -1000, -1000,'Homework', 2);
insert into task values (9, 1, 0, NOW(), '', -1000, -1000, 'Eggs', 2);
insert into task values (10, 1, 0, NOW(), '',-1000, -1000, 'Milk', 2);
insert into task values (11, 1, 0, NOW(), '', -1000, -1000, 'Fish', 2);

insert into task_groups_tasks values (1, 1);
insert into task_groups_tasks values (1, 2);
insert into task_groups_tasks values (1, 3);
insert into task_groups_tasks values (2, 4);
insert into task_groups_tasks values (2, 5);
insert into task_groups_tasks values (3, 6);
insert into task_groups_tasks values (4, 7);
insert into task_groups_tasks values (4, 8);
insert into task_groups_tasks values (5, 9);
insert into task_groups_tasks values (5, 10);
insert into task_groups_tasks values (5, 11);

insert into message values (1, "dad@mail.com", "Poruka od tate", NOW());
insert into message values (2,  "mum@mail.com",  "Poruka od mame", NOW());
insert into message values (3, "kid@mail.com", "Poruka od deteta", NOW());
insert into message values (4, "dad@mail.com", "Poruka od tate2", NOW());
insert into message values (5, "mum@mail.com", "Poruka od mame2", NOW());

insert into group_messages values (1, 1);
insert into group_messages values (1, 2);
insert into group_messages values (1, 3);
insert into group_messages values (1, 4);
insert into group_messages values (1, 5);
