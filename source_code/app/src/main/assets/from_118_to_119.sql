DELETE FROM layout_tag_assignment;
INSERT INTO layout_tag_assignment (layout_id, tag_id) SELECT _id, layout_tag FROM player_layouts;
INSERT INTO layout_tags (layout_tags, was_tag) SELECT layout_type, 1 FROM layout_types;
UPDATE layout_types SET new_id = (SELECT _id FROM layout_tags WHERE (layout_types.layout_type = layout_tags.layout_tags) AND (layout_tags.was_tag = 1));
INSERT INTO layout_tag_assignment (layout_id, tag_id) SELECT player_layouts._id, tags._id FROM player_layouts
INNER JOIN layout_types ON layout_types._id = player_layouts.layout_type
INNER JOIN (SELECT _id FROM layout_tags WHERE was_tag = 1) As tags ON tags._id = layout_types.new_id;
