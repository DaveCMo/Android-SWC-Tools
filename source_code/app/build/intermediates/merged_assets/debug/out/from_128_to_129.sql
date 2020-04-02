alter table player_layouts add COLUMN layout_version_default_set TEXT;
UPDATE table player_layouts SET layout_version_default_set = 'N';
