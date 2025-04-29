create table if not exists days (
    id bigint generated always as identity,
    name varchar(255)
);

create table if not exists muscles (
    id bigint generated always as identity,
    name varchar(255)
);

create table if not exists gender (
    id bigint generated always as identity,
    name varchar(255)
);

create table if not exists roles (
    id bigint generated always as identity,
    name varchar(255)
);

create table if not exists training_type (
    id bigint generated always as identity,
    name varchar(255)
);

create table if not exists roles (
    id bigint generated always as identity,
    name varchar(255)
);

create table if not exists users (
    id bigint generated always as identity,
    name varchar(255),
    email varchar(255),
    password varchar(255),
    weight double precision,
    role_id bigint,
    gender_id bigint,
    age int,
    foreign key (gender_id) references gender(id),
    foreign key (role_id) references roles(id)
);

create table if not exists exercises (
    id bigint generated always as identity,
    name varchar(255),
    muscle_id bigint,
    day_id bigint,
    gender_id bigint,
    foreign key (muscle_id) references muscles(id),
    foreign key (day_id) references days(id),
    foreign key (gender_id) references gender(id)
);

create table if not exists recommendations(
    id bigint generated always as identity,
    exercise_id bigint,
    user_age int,
    user_weight double precision,
    recommended_sets bigint,
    recommended_repeats bigint,
    recommended_weight_min integer,
    recommended_weight_max integer,
    foreign key (exercise_id) references exercises(id)
)
