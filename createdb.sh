#!/bin/sh

sqlite3 foodbase.db "create table FOOD_DES (
NDB_No INTEGER PRIMARY KEY,
FdGrp_Cd INTEGER,
Long_Desc TEXT,
Short_Desc TEXT,
ComName TEXT,
ManufacName TEXT,
Survey TEXT,
Ref_desc TEXT,
Refuse FLOAT,
SciName TEXT,
N_Factor FLOAT,
Pro_Factor FLOAT,
Fat_Factor FLOAT,
CHO_Factor FLOAT);"

sqlite3 foodbase.db "create table FD_GROUP (
FdGrp_Cd INTEGER PRIMARY KEY, FdGrp_Desc TEXT);"

sqlite3 foodbase.db "create table NUT_DATA (
NDB_No INTEGER,
Nutr_No INTEGER,
Nutr_Val FLOAT,
Num_Data_Pts FLOAT,
Std_Error FLOAT,
Src_Cd INTEGER,
Deriv_Cd INTEGER,
Ref_NDB_No INTEGER,
Add_Nutr_Mark TEXT,
Num_Studies INTEGER,
Min FLOAT,
Max FLOAT,
DF INTEGER,
Low_EB FLOAT,
Up_EB FLOAT,
Stat_cmt TEXT,
CC INTEGER);"
