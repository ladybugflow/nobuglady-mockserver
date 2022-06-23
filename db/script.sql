CREATE TABLE `category` (
  `id` varchar(100) NOT NULL,
  `text` varchar(400) NOT NULL,
  `type` varchar(100) NOT NULL,
  `parent` varchar(100) NOT NULL,
  `disabled` varchar(1000) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
;

CREATE TABLE `api` (
  `api_id` varchar(100) NOT NULL,
  `api_status` varchar(10) DEFAULT NULL,
  `req_path` varchar(4000) DEFAULT NULL,
  `req_path_query` varchar(4000) DEFAULT NULL,
  `req_method` varchar(10) DEFAULT NULL,
  `dynamic_url_flag` int(11) DEFAULT NULL,
  `res_script` text DEFAULT NULL,
  `res_status` varchar(10) DEFAULT NULL,
  `res_content_type` varchar(100) DEFAULT NULL,
  `res_character_encoding` varchar(100) DEFAULT NULL,
  `res_body` text DEFAULT NULL,
  `res_headers` varchar(4000) DEFAULT NULL,
  `res_cookies` varchar(4000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
;

CREATE TABLE `tuser` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(400) DEFAULT NULL,
  `email` varchar(400) DEFAULT NULL,
  `passwd` varchar(100) DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  `admin_flag` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
;

CREATE TABLE `trole` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(400) DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
;

CREATE TABLE `permission` (
  `permission_id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(400) DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
;

CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
;

CREATE TABLE `role_permission` (
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
;

CREATE TABLE `permission_category` (
  `permission_id` int(11) NOT NULL,
  `category_id` varchar(100) NOT NULL,
  `can_read` int(11) NOT NULL,
  `can_update` int(11) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`permission_id`,`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
;

insert into tuser(user_name,email,passwd,remarks,admin_flag,create_time,update_time) values ('admin','admin','admin','',1,now(),now());

insert into category(id,text,type,parent,disabled,create_time,update_time) values ('1','root','default','#','false',now(),now());
insert into category(id,text,type,parent,disabled,create_time,update_time) values ('17b1fbef5b43b0','demo','default','1','false',now(),now());
insert into category(id,text,type,parent,disabled,create_time,update_time) values ('17b1fbf2cb013a','demo','file','17b1fbef5b43b0','false',now(),now());

insert into api(api_id,api_status,req_path,req_path_query,req_method,dynamic_url_flag,res_script,res_status,res_content_type,res_character_encoding,res_body,res_headers,res_cookies,create_time,update_time) values ('17b1fbf2cb013a','on','demo','demo','GET',0,'response.body = {};','200','application/json','UTF-8','',null,null,now(),now());
