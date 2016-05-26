Deploying the Scheduler components:
-----------------------------------

1) The scheduler is intended to be managed by an application team that would handle a set of batch data pipelines in the cloud.
2) The scheduler would manage the various pipelines form a single server I.e. One EC2 instance.
3) Create a folder (for the scheduler) in the desired location and place the following 4 scripts in them.
	runner.sh
	scheduler.sh
	Schd_Env.sh
	Schd_Clientlib.sh
4) Set the appropriate environment variables in the script – Schd_Env.sh. There are detailed comments for each of them in the script.
	a) Ensure the user who would execute the scheduler and runner processes in the background have write privileges to append log files (Schedulerlog & Runnerlog variables in this file)
	b) Ensure the user who would execute the scheduler and runner processes in the background have write privileges to the folder where their source code is present. This is required to allow the scheduler and runner create temporary files required as part of the execution.
	c)Choose the appropriate time interval (loginterval variable)  for the scheduler and runner to send the heartbeat values to the log file. Currently all the log information across days will be captured in a single file (one for scheduler and another one for runner). Be watchful of the possible file size. A loginterval of 10 would result in a 4KB increase per day for both the log files.
	d) This file needs to be locked down to only the user who would execute the runner and the scheduler scripts. This has the password to the DB.
5) Create a new schema (configured through the variable ‘Database’ in Schd_Env.sh file)
	a) Execute the DDLs present in the file Schd_Database_DDL.txt
	b) This schema needs to be one specific to the application.
	c) The configured user id (User in Schd_Env.sh) to have the needed privileges to perform the required DML operations to the various objects in this database. No other user in the database to have visibility into this schema and this application user to have no visibility into the other schemas in this database.
	d) Set all the needed metadata present in the various tables in this schema (job_details, job_status, job_triggers, job_constraints). The current version of the scheduler supports the following triggers
		i) As appropriate, a job in a pipeline would trigger one or more unique jobs.
		ii)The first step in the data pipeline could be event triggered (file watcher) or time triggered (through cron) and the subsequent steps to be triggered by the previous step.
6) Make sure the following scripts are running as daemon processes (no hangup)  in the EC2 instance. Also in case the EC2 is not configured to run 24 hours, ensure these statements are added as part of the bootstrap process.
	a) $ nohup ./scheduler.sh &
	b) $ nohup ./runner.sh & 


Changes to the actual jobs that handle the data processing logic in a data pipeline, which is part of the scheduler:
--------------------------------------------------------------------------------------------------------------------

1) Find below the skeleton of the first job that will kick off the pipeline.
	Through a common script check if this job is runnable i.e. is there another instance of this job currently in Error, Scheduled or Executing status.
	If the job is runnable, mark the corresponding job as 'Ready To run'
	If the job is not runnable, sleep for a specific duration and check for the runnable status. If runnable then, mark the corresponding job as 'Ready To RUn'. If still not runnable, capture the status of the instance as 'Timed Out' and exit.
2) Refer to the following skeleton script for the actual jobs that will be triggered in the pipeline.
	Capture the start time of the job
	Execute the needed daa processsing logic
	Capture the end time of the job
	In case the current processing is a success, perform the following
		Capture Status along with the start/end time for the current instance of the job run
		Trigger the next job 
		Mark the current job as success.
	If the crrent processing is a failure
		Capture Status along with the start/end time for the current instance of the job run
		Mark the current job as error
3) Ensure the user that executes the jobs has the needed privileges to create temporary files in the parent folder for each of these jobs.

