## Multiuser time-tracker
Task: to develop a backend service Multiuser time-tracker that can be accessed via REST. 
Time-tracker (Time-tracker or Time-tracking software) is a category of computer software that allows employees working at their computers to record time spent on tasks or projects, and employers to monitor them.
## Query types:
  1. create a tracking user;
  2. change user data;
  3. start time-tracking on task X;
  4. stop time-tracking on task X;
  5. show all time spent by user Y for the period N. .M as a linked list Task - Sum of time spent in the form (hh:mm) sorted from more to less (to answer the question, On which tasks I spent more time);
  6. show all time intervals spent working for the period N..M as a linked list Time interval (hh:mm) - Task (to answer the question, What did my week spend on or Where were the 'holes' in the past week when I did nothing);
  7. show the sum of labor inputs for all tasks of user Y for the period N..M (as if to display on the Worked this week panel);\
  8. clear tracking data of user Z;
  9. delete all information about user Z.

### CI and maintainability status

[![Actions Status](https://github.com/sergi-Jr/time-tracker/actions/workflows/main.yml/badge.svg)](https://github.com/sergi-Jr/time-tracker/actions)
[![Maintainability](https://api.codeclimate.com/v1/badges/12b45cc1b263ef3160bf/maintainability)](https://codeclimate.com/github/sergi-Jr/time-tracker/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/12b45cc1b263ef3160bf/test_coverage)](https://codeclimate.com/github/sergi-Jr/time-tracker/test_coverage)
