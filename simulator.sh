#! /bin/sh

if test $# -lt 5; then
    echo "Not enough arguments"
    echo "Usage: simulator inst.txt data.txt reg.txt config.txt result.txt"
    echo
    echo "Also check order of files"
    echo
    exit 1
fi
java -jar simulator.jar "$1" "$2" "$3" "$4" "$5"