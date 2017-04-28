#!/bin/bash
function genRScript { 
	cat > results.R <<EOT
	library(matrixStats); #install.packages('matrixStats');
	
	gens = read.table("results.all");
	alldata = subset(gens, select=V1:V50)
	
	gens\$means 	<- rowMeans(alldata, na.rm = TRUE);
#	gens\$std 	<- transform(alldata, SD=rowSds(alldata, na.rm=TRUE))
#	gens\$std 	<- rowSds(alldata, na.rm=TRUE)
#	write.table(subset(gens, select=c(means,std)), file = "results.sum", sep = " ");
	write.table(subset(gens, select=c(means)), file = "results.sum", sep = " ");
EOT
}

function genGnuplotScript { 
	cat > gp <<EOT
	set key at 190,0.65
	set style data lines
	plot "results.sum" using 1 title "avg bst fit"
	#replot "results.sum" using 2 title "std best fit"
	pause mouse key "..."
EOT
}
 
function wipeTmpFiles {
	# remove all temporary files 
	rm *.out.stat.bstonly
	rm results.R
	rm gp
	rm results.all
	rm results.sum
}

if [ $# -lt 1 ] 
then	
	echo 'Specify exp folder...'
	exit -1
fi 


cd $1 

# getting best fitness data from all exps into its own file 
for i in *.out.stat
do 
	cut -f2 -d' ' $i > $i.bstonly
done
# getting all these files together into results.all
paste *.out.stat.bstonly > results.all

genRScript
genGnuplotScript

R -q -e 'source("results.R")'
# removing the title from the resulting file 
tail -n +2 results.sum > results.sum.tmp 
rm results.sum 
mv results.sum.tmp results.sum

# removing the first column
cut --complement -f1 -d' ' results.sum > results.sum.tmp 
rm results.sum 
mv results.sum.tmp results.sum

gnuplot < gp
#wipeTmpFiles

cd -


