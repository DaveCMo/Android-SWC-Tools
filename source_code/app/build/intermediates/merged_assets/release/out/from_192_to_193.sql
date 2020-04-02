CREATE TABLE IF NOT EXISTS layouttoptemp (_id INTEGER PRIMARY KEY AUTOINCREMENT, layout_id INTEGER, player_id TEXT);
INSERT INTO layouttoptemp SELECT * FROM player_layout_top WHERE player_layout_top.layout_id IN (SELECT player_layouts._id FROM player_layouts);
DROP TABLE player_layout_top;
CREATE TABLE player_layout_top AS SELECT * FROM layouttoptemp;