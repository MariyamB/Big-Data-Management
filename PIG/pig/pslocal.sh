#!/bin/bash

rm -rf output
pig -x local ps.pig
cat output/part-m-00000