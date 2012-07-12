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

