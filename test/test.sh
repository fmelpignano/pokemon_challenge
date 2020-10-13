#!/usr/bin/env sh

echo "Start!"

while IFS= read -r line; do
    echo $line
    http GET http://127.0.0.1:8080/pokemon/$line &
done < list.txt
