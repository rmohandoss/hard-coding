#! /bin/bash

source Schd_Env.sh


Capture_Run_Details() {
# This function can be leveraged by any job to capture the execution details of a specific instance of that job. This function 
# expects 3 arguments. 
# 1. unique job_name as configured in job_detils table.
# 2. start time of the execution in the format yyyy-mm-dd|hh:mm:ss
# 2. end time of the execution in the format yyyy-mm-dd|hh:mm:ss
# Notice the | in the start and end time that separates the date from the time.
# The caller could sue the following function in their bash scripts to get to this format.
# Start_Time="$(date +'%Y-%m-%d|%H:%M:%S')"
# Returns nothing

Curr_Job=$1;
Start_time=$2;
End_time=$3;
Status=$4;

Start_time=${Start_time/|/ }
End_time=${End_time/|/ }

#echo $Start_time;
#echo $End_time;

mysql -N -u $User -p $Database -P $Port -p$Password -h $Server -se \
      "insert into job_run_status(job_id, job_start_time, job_end_time, job_run_status) \
       select js.job_id,\
              '$Start_time',\
              '$End_time',\
              '$Status'\
       from   job_status js,\
              job_details jd \
       where  jd.job_id=js.job_id \
       and    jd.job_name = '$Curr_Job';commit;";   

}

Trigger_Next() {

# This function can be leveraged by any job to trigger the next job after successful completion. For instance, the job that loads the landing zone
# can trigger the next job that would load the facts/dimensions.
# This function expects one argument.
# 1. unique job_name as configured in job_details table.
# 2. path where the execiting script has previliges to create temporary files. This function would prepare the next job list in this file.
# Returns nothing

Curr_Job=$1;
Curr_Path=$2;

list="list";
Next_Joblist=$Curr_Path$Curr_Job$list;

mysql -N -u $User -p $Database -P $Port -p$Password -h $Server -se \
      "select next_job_id \
       from   job_status js, \
              job_details jd, \
              job_triggers jt \
       where  js.job_id=jd.job_id \
       and    js.job_id=jt.job_id \
       and    jd.job_name = '$Curr_Job';" > $Next_Joblist;

cat $Next_Joblist | while read nextjob
do
       mysql -N -u $User -p $Database -P $Port -p$Password -h $Server -se \
             "update job_status  \
              set    job_status='Ready To Run' \
              where  job_id = $nextjob \
              and    job_status = 'Await Event';commit;";
done

rm $Next_Joblist

}

Runnable() {

# This function can be leveraged by a specific job to check its 'runnable's status i.e. if there are any constraints that would prevent it from running
# This will most likely be utilized by the first step in teh data pipeline. 

Curr_Job=$1;
conflict_cnt=`mysql -N -u $User -p $Database -P $Port -p$Password -h $Server -se \
                     "select count(*) \
                      from   job_status js,\
                             job_details jdet\
                      where  js.job_id=jdet.job_id\
                      and    jdet.job_name = '$Curr_Job' \
                      and    js.job_status in ('Error', 'Executing', 'Scheduled');"`;

echo $conflict_cnt

}


Mark_Ready() {

# This function can be called by any job to mark itself ready to run.

Curr_Job=$1;
mysql -N -u $User -p $Database -P $Port -p$Password -h $Server -se \
      "update job_status js, \
              job_details jd \
       set    js.job_status='Ready To Run' \
       where  jd.job_name = '$Curr_Job'\
       and    jd.job_id=js.job_id \
       and    js.job_status = 'Await Event';commit;";

echo "Mark Ready";

}

Mark_Complete() {

# This function can be called by any job to mark itself complete after a successful completeion.

Curr_Job=$1;
mysql -N -u $User -p $Database -P $Port -p$Password -h $Server -se \
      "update job_status js, \
              job_details jd \
       set    js.job_status='Await Event' \
       where  jd.job_name = '$Curr_Job'\
       and    jd.job_id=js.job_id \
       and    js.job_status = 'Executing';commit;";

echo "Mark Complete";

}

Mark_Error() {

# This function can be called by any job to mark its state as 'error' state after an unsuccessful run.

Curr_Job=$1;
mysql -N -u $User -p $Database -P $Port -p$Password -h $Server -se \
      "update job_status js, \
              job_details jd \
       set    js.job_status='Error' \
       where  jd.job_name = '$Curr_Job'\
       and    jd.job_id=js.job_id \
       and    js.job_status = 'Executing';commit;";

}

