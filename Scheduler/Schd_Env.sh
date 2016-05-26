#! /bin/bash

########################### W A R N I N G ################################
# This file needs to be locked down and protected as it has the password #
########################### W A R N I N G ################################

#Assign the port number of the Database
Port=***fill port***
#Assign the user id to be used. This should be unique to an application (could be team/organization/application).
User=***fill username***
#Password corresponding to the above user.
Password=***fill password***
#The database where the scheduler metadata will be setup for the application. The above user alone will have access to 
#this database and will manage everything within it.
Database=***fill database***
#The URL of the Database server
Server=***fill server URL***
#Configure the following variable to set the polling interval for the scheduler and the runner
Sleep_time=10
#The scheduler and the runner will constantly run as deamon process. The below value (in seconds) will determine how ofte would
#they both ping their status to the log files.
loginterval=10
#Log file for the Scheduler
Schedulerlog=Scheduler_Details.log
#Log file for the Runner
Runnerlog=Runner_Details.log

