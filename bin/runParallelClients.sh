#!/bin/bash
#runs command in all the nodes
act_exec -r node01..node16 'cd /home/nmharjan/projects/FinalProject/dist/; ./runClient.sh head 3000'
