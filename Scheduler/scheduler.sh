#!/bin/bash

source Schd_Env.sh

echo `date`  "Scheduler booting up .." >> $Schedulerlog;
prev_logtime=`date +"%s"`;

while [ 1 -eq 1 ]
do

mysql -N -u $User -p $Database -P $Port -p$Password -h $Server -se "SELECT jd.job_name FROM job_status js, job_details jd where jd.job_id = js.job_id and js.job_status = 'Ready To Run';" > Joblist;

cat Joblist | while read job
do
	conflict_cnt=`mysql -N -u $User -p $Database -P $Port -p$Password -h $Server -se \
                     "rollback;select count(*) \
                      from   job_status js,\
                             job_details jdet,\
                             job_constraints jd,\
                             job_status js1\
                      where  js.job_id=jd.job_id1\
                      and    js.job_id=jdet.job_id\
                      and    jdet.job_name = '$job' \
                      and    jd.job_id2=js1.job_id\
                      and    js1.job_status in ('Executing', 'Scheduled');"`;

	if [ $conflict_cnt -eq 0 ]
	then

                echo `date`  "Scheduling job $job .." >> $Schedulerlog;

		mysql -N -u $User -p $Database -P $Port -p$Password -h $Server -se \
                     "update job_status js, \
                             job_details jdet \
                      set    js.job_status='Scheduled' \
                      where  jdet.job_name = '$job' \
                      and    js.job_id=jdet.job_id \
                      and    js.job_status = 'Ready To Run';commit;";
	fi
done

curr_logtime=`date +"%s"`;

logdiff=`expr $curr_logtime - $prev_logtime`;

if [ $logdiff -gt $loginterval ]
then
        echo `date` "Scheduler executing .." >> $Schedulerlog;
        prev_logtime=`date +"%s"`;
fi

Job_cnt=`wc -l Joblist | cut -d " " -f 1`;

sleep 	$Sleep_time;

done

