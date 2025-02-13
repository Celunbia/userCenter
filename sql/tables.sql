create table user
(
    id            bigint auto_increment comment '用户id'
        primary key,
    user_name     varchar(128)                       null comment '用户昵称',
    user_account  varchar(128)                       null comment '用户账号',
    avatar_url    varchar(256)                       null comment '头像url',
    gender        tinyint                            null comment '性别 1-male 0-female',
    user_password varchar(128)                       null comment '用户密码',
    phone         varchar(128)                       null comment '手机号码',
    email         varchar(128)                       null comment '用户邮箱',
    user_status   tinyint  default 0                 not null comment '用户状态 0-正常 1-异常',
    user_role     tinyint  default 0                 not null comment '用户角色 0-普通用户 1-管理员',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint  default 0                 not null comment '删除状态 0 -未删除 1-删除',
    planet_code   varchar(512)                       null comment '星球编号用于鉴权'
)
    comment '用户信息';

