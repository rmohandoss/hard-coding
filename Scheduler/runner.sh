#!/bin/bash

source Schd_Env.sh

echo `date`  "Runner booting up .." >> $Runnerlog;
prev_logtime=`date +"%s"`;

while [ 1 -eq 1 ]
do

mysql -N -u $User -p $Database -P $Port -p$Password -h $Server -se "SELECT jd.job_name FROM job_status js, job_details jd where jd.job_id=js.job_id and js.job_status = 'Scheduled' order by last_status_change;" > Scheduledlist;

cat Scheduledlist | while read jobname
do


	mysql -N -u $User -p $Database -P $Port -p$Password -h $Server -se \
                     "update job_status js, \
                             job_details jd \
                      set    js.job_status='Executing' \
                      where  js.job_id=jd.job_id \
                      and    jd.job_name = '$jobname'; commit;";

	echo `date`  "Executing job $jobname .." >> $Runnerlog;

        Script_name=`mysql -N -u $User -p $Database -P $Port -p$Password -h $Server -se "SELECT jd.path FROM job_details jd where job_name='$jobname';"`;
        #echo $Script_name;	
        $Script_name &
done

curr_logtime=`date +"%s"`;

logdiff=`expr $curr_logtime - $prev_logtime`;

if [ $logdiff -gt $loginterval ]
then
	echo `date` "Runner executing .." >> $Runnerlog;
	prev_logtime=`date +"%s"`;
fi

Job_cnt=`wc -l Scheduledlist | cut -d " " -f 1`;
sleep 	$Sleep_time;

done

