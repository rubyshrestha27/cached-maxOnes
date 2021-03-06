#!/bin/bash

function genRScript { 
	cat > results.R <<EOT
	library(matrixStats); #install.packages('matrixStats');
	
	gens = read.table("results.all");
	alldata = subset(gens, select=V1:V50)
	
	gens\$means 	<- rowMeans(alldata, na.rm = TRUE);
	write.table(subset(gens, select=c(means)), file = "results.sum", sep = " ");
EOT
}

function display { 
	echo -n "========== "
	echo -n $1
	echo " ========="
}

function fail {
	echo 
	echo "=================================="
	echo $1
	echo "=================================="
	echo
	exit
}

FORGE="/home/ruby/Desktop/is/ecj"
FORGE_ECJ=$FORGE/original/ecj
FORGE_RUBY=$FORGE/cached
	
if [[ $# -lt 1 ]]
then 
	fail "Specify the name of the experiment folder"
fi

if [[ ! -e "$1.params" ]]
then 
	fail "params file not found: $1.params"
fi

mkdir $1
cd $1
cp ../$1.params ./

display "Building RUBY classes"
javac -cp "$FORGE_ECJ:$FORGE_RUBY" $FORGE_RUBY/ec/fitnesscache/*.java

if [ $? != 0 ] 
then 
	fail "Take another look at the source..."
fi

display "Running Experiment"
java -cp $FORGE_RUBY/ecj.22.jar:$FORGE_RUBY ec.Evolve -file $1.params #2> /dev/null

if [ $? != 0 ] 
then 
	fail "Experiment went Kaflouy..."
fi

display "Analyzing Experiment"

# getting best fitness data from all exps into its own file 
for i in *.out.stat
do 
	cut -f2 -d' ' $i > $i.bstonly
done
# getting all these files together into results.all
paste *.out.stat.bstonly > results.all

genRScript
R -q -e 'source("results.R")' > /dev/null 2>&1

# removing the title from the resulting file 
tail -n +2 results.sum > results.sum.tmp 
rm results.sum 
mv results.sum.tmp results.sum

# removing the first column
cut --complement -f1 -d' ' results.sum > results.sum.tmp 
rm results.sum 
mv results.sum.tmp results.sum
rm results.R

cd ..

find $FORGE_ECJ -name "*.class" -delete
find $FORGE_RUBY -name "*.class" -delete


