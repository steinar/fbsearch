# Facebook Post Search

Search for Facebook posts within your groups and pages created within an arbritary number of days.
Facebook UI only allows one to select the target year. This script creates a link with a filter 
where the resolution is brought down to days.

Usage: `sbt --error "run [+days] phrase"` where days is the number of backward days to include (default 0) and phrase is the search phrase.

Wrap with `open $(...)` to open the link directy.

