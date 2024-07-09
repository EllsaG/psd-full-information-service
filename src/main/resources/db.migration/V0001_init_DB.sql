create table full_information
(
    full_information_id int2 primary key,
    busbar_name varchar(255) not null,
    amount int2 not null,
    avg_daily_active_power float4 not null,
    avg_daily_reactive_power float4 not null,
    effective_amount_of_equipment int2 not null,
    coefficient_max float4 not null,
    max_active_power float4 not null,
    max_reactive_power float4 not null,
    max_full_power float4 not null,
    max_electric_current float4 not null,
    power_of_group float4 not null,
    cos_f float4 not null,
    tg_f float4 not null,
    k_i float4 not null,
    module float4 not null
);

create table full_start_information
(
    full_start_information_id int2 primary key,
    full_information_id int2 not null,
    start_information_id int2 not null,
    name varchar(255) not null,
    power float4 not null,
    power_of_group float4 not null,
    amount int2 not null,
    k_i float4 not null,
    cos_f float4 not null,
    tg_f float4 not null,
    avg_daily_active_power float4 not null,
    avg_daily_reactive_power float4 not null
);