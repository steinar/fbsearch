# Facebook Post Search

Search for Facebook posts within your groups and pages created within an arbritary number of days.
Facebook UI only allows one to select the target year. This script creates a link with a filter 
where the resolution is brought down to days.

## Usage
`open $(java -jar target/scala-2.13/fbsearch-assembly-1.0.jar [+days] phrase)` 

where days is the number of backward days to include (default 0) and phrase is the search phrase.

## Build

- 'sbt assembly'
- `sbt --error "run 1 phrase"`


