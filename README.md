# WebScraper
The WebScraper does the following:
1. parses the site to find the latest dataset version http://data.nalog.ru/opendata/7707329152-rafp/
2. extracts the required parameters from the downloaded xml file
3. provides the output in the form of 3 columns' csv:
  - The country of headquarters location;
  - A branch office status;
  - #such companies.
 
 My assumptions:
 1. A branch office status is "СостАк" attribute in xml and not "ПризАк"
 2. #companies equals #entries as "Документ" tag in xml. In that case companies mean branch offices as separate records in RAFP. 
 In contract, it could be assumed that #companies mean #headquarters ("НаимИЮЛПолн"), #branch offices ("НаимАФППолн") either distinct or with duplicates.
 
 The current code does not include tests and can be improved by adding them.
