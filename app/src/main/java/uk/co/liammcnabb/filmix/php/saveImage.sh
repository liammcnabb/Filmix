#!/bin/bash

sudo wget --referer=$1 $2 -P /var/www/imgMovies &> sample.s
long_path=$2
last_part=${long_path##*/}
last_part+=" "
first_part='/var/www/imgMovies/'
img=$first_part$last_part
img+=$img
echo "$img"
sudo convert -resize 300x445 $img
