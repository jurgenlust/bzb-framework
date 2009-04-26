#!/bin/bash

for file in `find . -iname "pom.xml"`
do
	ed $file <<doit
1,\$s/ersion>$1</ersion>$2</
.
w
q
doit
done
