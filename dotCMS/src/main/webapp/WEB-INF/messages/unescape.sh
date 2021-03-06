#!/bin/sh

# this will replace the escaped symbols generated by POEditor.com
# which include #, !, =, : and spaces in the keys

# it also requires gnu-sed to do the replacement
# brew install gnu-sed

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

for entry in "$DIR"/*.properties
do
   gsed  's/\\#/#/g' -i $entry
   gsed  's/\\!/!/g' -i $entry
   gsed  's/\\=/=/g' -i $entry
   gsed  's/\\:/:/g' -i $entry
   gsed  's/\\ / /g' -i $entry
done

