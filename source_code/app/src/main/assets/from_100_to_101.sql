DROP TABLE troops;
DROP TABLE armoury_equipment;
DROP TABLE buildings;
DROP TABLE swc_http_headers;
CREATE TABLE IF NOT EXISTS armoury_equipment (_id INTEGER PRIMARY KEY AUTOINCREMENT, game_name TEXT, ui_name TEXT, faction TEXT, available_on TEXT, level INTEGER, cap INTEGER);
CREATE TABLE IF NOT EXISTS troops (_id INTEGER PRIMARY KEY AUTOINCREMENT, game_name TEXT, ui_name TEXT, faction TEXT, level INTEGER, cap INTEGER);
CREATE TABLE IF NOT EXISTS buildings (_id INTEGER PRIMARY KEY AUTOINCREMENT, game_name TEXT, ui_name TEXT, faction TEXT, isTrap INTEGER, level INTEGER, cap INTEGER);
CREATE TABLE IF NOT EXISTS swc_http_headers (_id INTEGER PRIMARY KEY AUTOINCREMENT, header_group TEXT, _key TEXT, _value TEXT);