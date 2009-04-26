#!/bin/bash

for file in `find . -name "*"`
do
	if [[ $file != *.svn* && $file != */src* && -d $file ]]
	then
		svn propset svn:ignore -F .svnignore $file
	fi	
done

for file in `find . -name "*.java"`
do
	svn propset svn:keywords "Date Author Revision" $file
done
